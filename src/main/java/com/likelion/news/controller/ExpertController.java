package com.likelion.news.controller;

import com.likelion.news.dto.ExpertCommentDto;
import com.likelion.news.dto.ExpertRequest;
import com.likelion.news.service.ExpertService;
import io.jsonwebtoken.lang.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/expert")
public class ExpertController {
    private final ExpertService expertService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> registerExpert( ExpertRequest req,
                                                 @RequestParam(value = "images") MultipartFile[] images) throws IOException {
        String uid = getUid().get();
        List<MultipartFile> imageList = Collections.arrayToList(images);
        expertService.registerExpert(uid, req, imageList);


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value="/news/{newsId}/comment")
    public ResponseEntity<Void> writeComment(ExpertCommentDto comment,
                                             @PathVariable Long newsId){
        String uid = getUid().get();
        expertService.writeComment(uid, newsId, comment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping(value="/news/{newsId}/comment/{commentId}")
    public ResponseEntity<Void> updateComment(ExpertCommentDto comment,
                                              @PathVariable Long newsId,
                                              @PathVariable Long commentId){
        String uid = getUid().get();
        expertService.updateComment(uid, newsId, commentId, comment);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping(value="/news/{newsId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long newsId,
                                              @PathVariable Long commentId){
        String uid = getUid().get();
        expertService.deleteComment(uid, newsId, commentId);
        return  ResponseEntity.ok().build();
    }

    /**
     * @author minseok kim
     * @description SecurityContextHolder에서 uid를 가져오는 메서드
     * @param
     * @return SecurityContextHolder에서 조회된 uid
     */
    private Optional<String> getUid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.isAuthenticated()){
            return Optional.of((String)authentication.getPrincipal());
        }

        return Optional.empty();
    }


}
