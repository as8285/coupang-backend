package com.kh.coupang.service;

import com.kh.coupang.domain.Review;
import com.kh.coupang.domain.ReviewImage;
import com.kh.coupang.repo.ReviewDAO;
import com.kh.coupang.repo.ReviewImgDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ReviewService {
    @Autowired
    private ReviewDAO review;
    @Autowired
    private ReviewImgDAO image;
    public Review create(Review vo) {
        return review.save(vo);
    }
    public ReviewImage createImg(ReviewImage vo){
       return image.save(vo);
    }
    public List<Review>viewAll(){
        return review.findAll();
    }
}

