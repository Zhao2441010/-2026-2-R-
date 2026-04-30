package com.example.demo.controller;

import com.example.demo.entity.Cat;
import com.example.demo.entity.Picture;
import com.example.demo.repository.CatRepository;
import com.example.demo.service.CatService;
import com.example.demo.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/cats")
public class AdminCatController {

    @Autowired
    private CatService catService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private CatRepository catRepository;  // 直接注入，用于 save/update

    /**
     * 获取所有猫咪的完整信息（管理员用）
     */
    @GetMapping("/all")
    public ResponseEntity<List<Cat>> getAllCatsFull() {
        List<Cat> cats = catService.getAllCat();
        return ResponseEntity.ok(cats);
    }

    /**
     * 根据ID获取单只猫的完整信息
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cat> getCatById(@PathVariable Long id) {
        Cat cat = catService.getCatById(id);
        if (cat != null) {
            return ResponseEntity.ok(cat);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 新增猫咪
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> addCat(@RequestBody Cat cat) {
        Map<String, Object> result = new HashMap<>();
        try {
            catService.saveCat(cat.getName(), cat.getImage(), cat.getAge(),
                    cat.getType(), cat.getHealth(), cat.getJueyu());
            result.put("success", true);
            result.put("message", "猫咪添加成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    /**
     * 更新猫咪信息 - 使用 catRepository.save() 真正保存到数据库
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCat(@PathVariable Long id, @RequestBody Cat cat) {
        Map<String, Object> result = new HashMap<>();
        try {
            Cat existing = catService.getCatById(id);
            if (existing == null) {
                result.put("success", false);
                result.put("message", "猫咪不存在");
                return ResponseEntity.notFound().build();
            }

            // 更新各个字段
            if (cat.getName() != null) existing.setName(cat.getName());
            if (cat.getImage() != null) existing.setImage(cat.getImage());
            existing.setAge(cat.getAge());
            if (cat.getType() != null) existing.setType(cat.getType());
            if (cat.getHealth() != null) existing.setHealth(cat.getHealth());
            existing.setJueyu(cat.getJueyu());
            existing.setFeed(cat.getFeed());

            // ========== 关键修复：真正保存到数据库 ==========
            catRepository.save(existing);

            result.put("success", true);
            result.put("message", "更新成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Map<String, Object>> updateCatImage(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            String image = body.get("image");
            Cat existing = catService.getCatById(id);
            if (existing == null) {
                result.put("success", false);
                result.put("message", "猫咪不存在");
                return ResponseEntity.notFound().build();
            }
            existing.setImage(image);
            catRepository.save(existing);
            result.put("success", true);
            result.put("message", "默认图片已更新");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }


    /**
     * 删除猫咪（级联删除相关图片）
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteCat(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 先删除该猫的所有图片（物理文件 + 数据库）
            List<Picture> pictures = pictureService.findByCatId(id);
            for (Picture pic : pictures) {
                if (pic.getUrl() != null) {
                    String fileName = pic.getUrl().replace("/pictures/", "");
                    // 删除两个目录的文件
                    deleteFile("D:/my rubbish/1 homework/1 now/ruangong/-2026-2-R-/demo/src/main/resources/pictures/" + fileName);
                    deleteFile("D:/my rubbish/1 homework/1 now/ruangong/-2026-2-R-/demo/target/classes/pictures/" + fileName);
                }
                pictureService.deletePictureById(pic.getId());
            }

            // 再删除猫咪
            catService.deleteCatById(id);

            result.put("success", true);
            result.put("message", "猫咪及相关图片已删除");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    private void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            boolean deleted = file.delete();
            System.out.println("删除文件 " + (deleted ? "成功" : "失败") + ": " + path);
        }
    }
}

