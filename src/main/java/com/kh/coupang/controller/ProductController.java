package com.kh.coupang.controller;


import com.kh.coupang.domain.Category;
import com.kh.coupang.domain.Product;
import com.kh.coupang.domain.ProductDTO;
import com.kh.coupang.service.ProductService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;// Slf4j 잘 오는지 확인할때 sysout 이란 비슷한의미

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/*")
public class ProductController {
    // 메서드명은 Service랑 동일
    @Autowired
    private ProductService product;

    @Value("${spring.selvelt.multipart.location}")
    private String uploadPath; // D:\\upload

    @PostMapping("/product")
    public ResponseEntity<Product> create(ProductDTO dto) throws IOException {

        // 파일 업로드
        String fileName = dto.getFile().getOriginalFilename();


        // UUID
        String uuid = UUID.randomUUID().toString();
        String saveName = uploadPath + File.separator + "product" + File.separator + uuid + "_" + fileName;
        Path savePath = Paths.get(saveName);
        dto.getFile().transferTo(savePath); // 파일 업로드 실제로 일어나고 있음
        Product vo = new Product();

        vo.setProdName(dto.getProdName());
        vo.setPrice(dto.getPrice());
        vo.setProdPhoto(saveName);

        Category category = new Category();
        category.setCateCode(dto.getCateCode());
        vo.setCategory(category);
        Product result = product.create(vo);
        if (result != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    @GetMapping("/product")
    public ResponseEntity<List<Product>> viewAll(@RequestParam(name = "category", required = false) Integer category) {
        log.info("category : " + category);
        List<Product> list = product.viewAll();
        return category == null ? ResponseEntity.status(HttpStatus.OK).body(list) :
                ResponseEntity.status(HttpStatus.OK).body(product.viewCategory(category));
    }


    @GetMapping("/product/{code}")
    public ResponseEntity<Product> view(@PathVariable(name = "code") int code) {
        Product vo = product.view(code);
        return ResponseEntity.status(HttpStatus.OK).body(vo);
    }


    @SneakyThrows
    @PutMapping("/product")
    public ResponseEntity<Product> update(ProductDTO dto) {
        // 기존 사진은 삭제하고 새로운 사진을 추가
        Product vo = new Product();
        vo.setProdCode(dto.getProdCode());
        vo.setPrice(dto.getPrice());
        vo.setProdName(dto.getProdName());
        Category category = new Category();
        category.setCateCode(dto.getCateCode());
        vo.setCategory(category);
        Product prev = product.view(dto.getProdCode());
        if (dto.getFile().isEmpty()) {
// 만약 새로운 사진이 없는 경우 -> 기존 사진 경로 그대로 vo 담아내기
            vo.setProdPhoto(prev.getProdPhoto());
        } else {
            // 기존 데이터를 가져와야 하는 상황
            File file = new File(prev.getProdPhoto());
            file.delete();
            // 파일 업로드
            String fileName = dto.getFile().getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String saveName = uploadPath + File.separator + "product" + File.separator + uuid + "_" + fileName;
            Path savePath = Paths.get(saveName);
            dto.getFile().transferTo(savePath); // 파일 업로드 실제로 일어나고 있음
            vo.setProdPhoto(saveName);

        }
         Product result = product.delete(dto.getProdCode());
            return (result != null) ? ResponseEntity.status(HttpStatus.ACCEPTED).body(result)
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }


    // 글자 정렬 ctrl+ alt+shift+l
    @DeleteMapping("/product/{code}")
    public ResponseEntity<Product> delete(@PathVariable(name = "code") int code) {
        Product prev = product.view(code);
        File file = new File(prev.getProdPhoto());
        file.delete();
        Product result = product.delete(code);
        return (result != null) ? ResponseEntity.status(HttpStatus.ACCEPTED).body(result) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }
}
