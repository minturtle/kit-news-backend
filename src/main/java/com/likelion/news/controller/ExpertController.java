package com.likelion.news.controller;

import com.likelion.news.dto.ExpertCommentDto;
import com.likelion.news.dto.ExpertRequest;
import com.likelion.news.dto.UserDto;
import com.likelion.news.service.ExpertService;
import io.jsonwebtoken.lang.Collections;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/expert")
public class ExpertController {
    private final ExpertService expertService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> registerExpert( ExpertRequest req,
                                                 @RequestParam(value="uid") String uid,
                                                 @RequestParam(value = "images") MultipartFile[] images) throws IOException {

        List<MultipartFile> imageList = Collections.arrayToList(images);
        expertService.registerExpert(uid, req, imageList);


        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
