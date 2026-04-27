package com.example.demo.controller;

import com.example.demo.entity.Cat;
import com.example.demo.service.CatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cats")
@CrossOrigin(origins = "http://localhost:8080")
public class CatController {

    @Autowired
    private CatService catService;

    /**
     * 获取前50只猫（首页展示 - 只含照片、姓名、feed）
     */
    @GetMapping("/featured")
    public ResponseEntity<List<CatSummaryDTO>> getFeaturedCats() {
        List<Cat> cats = catService.getTop10();
        List<CatSummaryDTO> summaries = cats.stream()
                .map(cat -> new CatSummaryDTO(
                        cat.getId(),
                        cat.getName(),
                        cat.getImage(),
                        cat.getFeed()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    /**
     * 获取所有猫（展示更多页面 - 只含照片、姓名、feed）
     */
    @GetMapping("/all")
    public ResponseEntity<List<CatSummaryDTO>> getAllCats() {
        List<Cat> cats = catService.getAllCat();
        List<CatSummaryDTO> summaries = cats.stream()
                .map(cat -> new CatSummaryDTO(
                        cat.getId(),
                        cat.getName(),
                        cat.getImage(),
                        cat.getFeed()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(summaries);
    }

    /**
     * 根据ID获取单只猫的完整详细信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cat> getCatById(@PathVariable Long id) {
        Cat cat = catService.getCatById(id);
        if (cat != null) {
            return ResponseEntity.ok(cat);
        }
        return ResponseEntity.notFound().build();
    }

    // DTO 类（可以单独放在 dto 包中）
    public static class CatSummaryDTO {
        private Long id;
        private String name;
        private String image;
        private Boolean feed;

        public CatSummaryDTO(Long id, String name, String image, Boolean feed) {
            this.id = id;
            this.name = name;
            this.image = image;
            this.feed = feed;
        }

        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getImage() { return image; }
        public Boolean getFeed() { return feed; }
    }
}