package com.kh.coupang.controller;

import com.kh.coupang.domain.*;
import com.kh.coupang.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/*")

public class ReviewController {
    @Autowired
    private ReviewService review;

    @Value("${spring.selvelt.multipart.location}")
    private String uploadPath; // D:\\upload

    @PostMapping("/review")
    public ResponseEntity<Review> create(ReviewDTO dto) throws IOException {

        Review vo = new Review();
        vo.setId(dto.getId());
        vo.setProdCode(dto.getProdCode());
        vo.setReviTitle(dto.getReviTitle());
        vo.setReviDesc(dto.getReviDesc());
        Review result = review.create(vo);
        // review 부터 추가 하여 - revi_code가 담긴 review!
        // review_image에는 revi_code가 필요!
        for (MultipartFile file : dto.getFiles()) {
            ReviewImage imgVo = new ReviewImage();

            String fileName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String saveName = uploadPath + File.separator + "review" + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);
            file.transferTo(savePath); // 파일 업로드 실제로 일어나고 있음

            imgVo.setReviUrl(saveName);
            imgVo.setReview(result);
            review.createImg(imgVo);
        }
        return result != null ?
                ResponseEntity.status(HttpStatus.CREATED).body(result) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }
    @GetMapping("/review")
    public ResponseEntity<List<Review>>viewAll(){
        return ResponseEntity.status(HttpStatus.OK).body(review.viewAll());
    }
}