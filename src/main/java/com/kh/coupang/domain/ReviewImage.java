package com.kh.coupang.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert

// 테이블 명이 _ 일때 추가해야한다.
@Table(name = "review_image")
public class ReviewImage {
    @Id
    @Column(name = "revi_img_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviImgCode;

    @Column(name = "revi_url")
    private String reviUrl;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "revi_code")
    private Review review;
}
