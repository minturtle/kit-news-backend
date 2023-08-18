package com.likelion.news.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.likelion.news.dto.ExpertCommentDto;
import com.likelion.news.dto.ExpertRequest;
import com.likelion.news.entity.*;
import com.likelion.news.entity.enums.ExpertState;
import com.likelion.news.entity.enums.UserType;
import com.likelion.news.exception.DuplicateException;
import com.likelion.news.exception.NotFoundException.NoExpertException;
import com.likelion.news.exception.NotFoundException.NoNewsException;
import com.likelion.news.exception.NotFoundException.NoUserException;
import com.likelion.news.exception.NotFoundException.NoCommentException;
import com.likelion.news.exception.UnAuthorizedException;
import com.likelion.news.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class ExpertService {
    private final ExpertInfoRepository expertInfoRepository;
    private final CertificationRepository certificationRepository;
    private final RefinedNewsRepository refinedNewsRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    /*
     * @methodName: registerExpert
     * @author: parkjunha
     * @description:
     *
     * @param: String 사용자 uid
     * @param: ExpertRequest 전문가 정보
     * @param: List<MultipartFile> 증명서 이미지들
     * @return: void
     */
    public void registerExpert(String uid, ExpertRequest req, List<MultipartFile> images) throws IOException {
        User findUser = userRepository.findUserByUid(uid).orElseThrow(NoUserException::new);

        Optional<ExpertInfo> duplicateExpert = expertInfoRepository.findByUserUid(uid);
        if (duplicateExpert.isPresent()){
            throw new DuplicateException.DuplicateExpertInfoException();
        }

        ExpertInfo expertInfo = ExpertInfo.builder()
                .user(findUser)
                .job(req.getJob())
                .businessType(req.getBusinessType())
                .company(req.getCompany())
                .education(req.getEducation())
                .state(ExpertState.PENDING)
                .build();

        List<Certification> certificationLinks = createCertificationLinks(images, expertInfo);

        expertInfoRepository.save(expertInfo);
        certificationRepository.saveAll(certificationLinks);

    }

    /*
     * @methodName: editExpert
     * @author: parkjunha
     * @description:
     *
     * @param: String 사용자 uid
     * @param: ExpertRequest 전문가 정보
     * @param: List<MultipartFile> 증명서 이미지들
     * @return: void
     */
    public void editExpert(String uid, ExpertRequest req, List<MultipartFile> images) throws IOException {
        ExpertInfo findExpertInfo = expertInfoRepository.findByUserUid(uid).orElseThrow(NoExpertException::new);
        certificationRepository.deleteAllByExpertInfo(findExpertInfo);

        findExpertInfo.setJob(req.getJob());
        findExpertInfo.setBusinessType(req.getBusinessType());
        findExpertInfo.setCompany(req.getCompany());
        findExpertInfo.setEducation(req.getEducation());

        List<Certification> certificationLinks = createCertificationLinks(images, findExpertInfo);

        certificationRepository.saveAll(certificationLinks);

    }

    public void deleteExpert(String uid) {
        ExpertInfo findExpertInfo = expertInfoRepository.findByUserUid(uid).orElseThrow(NoExpertException::new);
        expertInfoRepository.deleteById(findExpertInfo.getExpertInfoId());
    }

    /*
     * @methodName: createCertificationLinks
     * @author: parkjunha
     * @description: 증명서 url 리스트를 생성하는 메서드
     *
     * @param: List<MultipartFile> 증명서 파일 여러 장
     * @param: ExpertInfo 전문가 정보
     * @return: List<Certification> 증명서 url 리스트
     */
    private List<Certification> createCertificationLinks(List<MultipartFile> images, ExpertInfo expertInfo) throws IOException {

        List<Certification> certificationLinks = new ArrayList<>();
        for (MultipartFile image : images) {
            String url = uploadFileToS3(image);
            Certification certification = Certification.builder()
                    .expertInfo(expertInfo)
                    .certificationUrl(url)
                    .build();

            certificationLinks.add(certification);
        }
        return certificationLinks;
    }

    /*
     * @methodName: uploadFileToS3
     * @author: parkjunha
     * @description: 이미지 파일을 s3 정적 서버에 저장하는 메서드
     *
     * @param: MultipartFile 저장할 이미지 파일
     * @return: String S3에 저장한 파일 url
     */
    private String uploadFileToS3(MultipartFile file) throws IOException {
        String fileName=file.getOriginalFilename();
        String fileUrl= "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" +fileName;
        ObjectMetadata metadata= new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);
        return fileUrl;
    }

    public void writeComment(String uid, Long newsId, ExpertCommentDto commentDto){
        User findUser = checkExpertRole(uid);
        RefinedNews refinedNews = refinedNewsRepository.findById(newsId).orElseThrow(NoNewsException::new);

        Comment newComment = Comment.builder()
                .user(findUser)
                .refinedNews(refinedNews)
                .content(commentDto.getContent())
                .build();

        commentRepository.save(newComment);
    }

    public void updateComment(String uid,  Long newsId, Long commentId, ExpertCommentDto commentDto){
        User findUser = checkExpertRole(uid);
        Comment findComment = checkCommentAuth(findUser, commentId, newsId);
        findComment.setContent(commentDto.getContent());

    }

    public void deleteComment(String uid, Long newsId, Long commentId){
        User findUser = checkExpertRole(uid);
        Comment findComment = checkCommentAuth(findUser, commentId, newsId);
        commentRepository.delete(findComment);
    }

    /*
     * @methodName: checkCommentAuth
     * @author: parkjunha
     * @description: 수정, 삭제하고자 하는 댓글이 해당 뉴스, 유저 정보와 일치하는지 검사하는 메서드
     *
     * @param: User 찾은 유저
     * @param: Long 댓글 id
     * @param: Long 뉴스 id
     * @return: Comment 검증된 댓글 반환
     */
    private Comment checkCommentAuth(User findUser, Long commentId, Long newsId) {
        RefinedNews findNews = refinedNewsRepository.findById(newsId).orElseThrow(NoNewsException::new);
        Comment findComment = commentRepository.findById(commentId).orElseThrow(NoCommentException::new);

        if (!findComment.getRefinedNews().getRefinedNewsId().equals(findNews.getRefinedNewsId())) {
            throw new UnAuthorizedException("해당 뉴스의 댓글이 아닙니다.");
        }

        if (!findComment.getUser().getId().equals(findUser.getId())) {
            throw new UnAuthorizedException("댓글 작성자와 동일한 유저가 아닙니다.");
        }
        return findComment;
    }

    /*
     * @methodName: checkExpertRole
     * @author: parkjunha
     * @description: 해당 사용자가 전문가인지 체크하는 메서드
     * @param: String 사용자 uid
     * @return: User uid로 찾은 유저
     */
    private User checkExpertRole(String uid) {
        User findUser = userRepository.findUserByUid(uid).orElseThrow(NoUserException::new);
        if (!findUser.getUserType().equals(UserType.ROLE_EXPERT) && !findUser.getUserType().equals(UserType.ROLE_ADMIN)){
            throw new UnAuthorizedException("전문가 or 어드민이 아닙니다.");
        }
        return findUser;
    }

}
