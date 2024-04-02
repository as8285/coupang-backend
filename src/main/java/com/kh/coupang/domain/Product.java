package com.kh.coupang.domain;

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
public class Product {
    @Id

    // 추가를 바로 쓰고 싶을 경우
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "prod_code")
    private int prodCode;

    @Column(name = "prod_name") // prod_name
    private String prodName;

    @Column
    private int price;

    @Column(name="prod_photo")
    private String prodPhoto; // prod_photo

    @ManyToOne
    @JoinColumn(name="cate_code")
    private Category category;
}