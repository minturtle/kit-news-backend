package com.likelion.news.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.likelion.news.dto.ExpertCommentDto;
import com.likelion.news.dto.ExpertRequest;
import com.likelion.news.entity.*;
import com.likelion.news.entity.enums.ExpertState;
import com.likelion.news.exception.NotFoundException;
import com.likelion.news.exception.NotFoundException.NoNewsException;
import com.likelion.news.exception.NotFoundException.NoUserException;
import com.likelion.news.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public void registerExpert(String uid, ExpertRequest req, List<MultipartFile> images) throws IOException {
        User findUser = userRepository.findUserByUid(uid).orElseThrow(NoUserException::new);
        ExpertInfo expertInfo = ExpertInfo.builder()
                .user(findUser)
                .job(req.getJob())
                .businessType(req.getBusinessType())
                .company(req.getCompany())
                .education(req.getEducation())
                .expertState(ExpertState.PENDING)
                .build();

        List<Certification> certificationLinks = createCertificationLinks(images, expertInfo);

        expertInfoRepository.save(expertInfo);
        certificationRepository.saveAll(certificationLinks);

    }

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

    private String uploadFileToS3(MultipartFile file) throws IOException {
        String fileName=file.getOriginalFilename();
        String fileUrl= "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" +fileName;
        ObjectMetadata metadata= new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket,fileName,file.getInputStream(),metadata);
        return fileUrl;
    }

}
