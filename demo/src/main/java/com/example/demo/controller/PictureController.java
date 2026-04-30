package com.example.demo.controller;

import com.example.demo.entity.Picture;
import com.example.demo.entity.User;
import com.example.demo.entity.Admin;
import com.example.demo.repository.PictureRepository;
import com.example.demo.service.PictureService;
import com.example.demo.service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pictures")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @Autowired
    private PictureRepository pictureRepository;

    @Autowired
    private UserService userService;

    // ========== 修复：将上传目录移到项目同级，避免Spring Boot DevTools自动重启 ==========
    // 项目根路径: D:\my rubbish\1 homework\1 now\ruangong\-2026-2-R-
    // 上传目录:   D:\my rubbish\1 homework\1 now\ruangong\cat-uploads\pictures
    // 不在classpath中，DevTools不会监控，不会触发自动重启
    private static final String UPLOAD_BASE_DIR =
            "D:/my rubbish/1 homework/1 now/ruangong/cat-uploads/pictures/";

    @PostConstruct
    public void init() {
        createDirIfNotExists(UPLOAD_BASE_DIR);
    }

    private void createDirIfNotExists(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            System.out.println("创建目录 " + (created ? "成功" : "失败") + ": " + dir.getAbsolutePath());
        }
    }

    private void deletePhysicalFile(String url) {
        if (url == null) return;
        String fileName = url.replace("/pictures/", "");
        if (fileName.isEmpty() || fileName.equals(url)) return;

        // 从外部目录删除
        File uploadFile = new File(UPLOAD_BASE_DIR + fileName);
        if (uploadFile.exists()) {
            boolean deleted = uploadFile.delete();
            System.out.println("删除上传目录文件 " + (deleted ? "成功" : "失败") + ": " + uploadFile.getAbsolutePath());
        }
    }

    @GetMapping("/cat/{catId}")
    public ResponseEntity<List<Picture>> getPicturesByCatId(@PathVariable Long catId) {
        List<Picture> pictures = pictureService.findByCatId(catId);
        return ResponseEntity.ok(pictures);
    }

    @GetMapping("/cat/{catId}/display")
    public ResponseEntity<List<Picture>> getDisplayPicturesByCatId(@PathVariable Long catId) {
        List<Picture> pictures = pictureService.selectDisplayPictureByCatid(catId);
        return ResponseEntity.ok(pictures);
    }

    // ========== 新增：根据用户身份返回不同的图片列表 ==========
    @GetMapping("/cat/{catId}/user-view")
    public ResponseEntity<Map<String, Object>> getPicturesByUserView(
            @PathVariable Long catId,
            HttpSession session) {

        Map<String, Object> result = new HashMap<>();
        User currentUser = (User) session.getAttribute("currentUser");
        List<Picture> allPictures = pictureService.findByCatId(catId);
        List<Picture> filteredPictures;

        if (currentUser == null) {
            // 未登录：只返回 display=true
            filteredPictures = allPictures.stream()
                    .filter(p -> Boolean.TRUE.equals(p.getDisplay()))
                    .collect(Collectors.toList());
        } else if (currentUser instanceof Admin) {
            // ADMIN：返回所有
            filteredPictures = allPictures;
        } else {
            // 已登录普通用户：display=true + 自己上传的（无论display状态）
            Long userId = currentUser.getId();
            filteredPictures = allPictures.stream()
                    .filter(p -> Boolean.TRUE.equals(p.getDisplay())
                            || (p.getUploaderId() != null && p.getUploaderId().equals(userId)))
                    .collect(Collectors.toList());
        }

        result.put("success", true);
        result.put("data", filteredPictures);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}/display")
    public ResponseEntity<Map<String, Object>> setDisplay(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {
        Map<String, Object> result = new HashMap<>();
        try {
            Boolean display = body.get("display");
            pictureService.setDisplaySituationById(id, display);
            result.put("success", true);
            result.put("message", display ? "图片已显示" : "图片已隐藏");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePicture(@PathVariable Long id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        try {
            Picture picture = pictureRepository.findById(id).orElse(null);

            if (picture != null) {
                deletePhysicalFile(picture.getUrl());
                pictureService.deletePictureById(id);
                result.put("success", true);
                result.put("message", "图片已删除");
            } else {
                result.put("success", false);
                result.put("message", "图片不存在");
            }
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadPicture(
            @RequestParam("file") MultipartFile file,
            @RequestParam("catId") Long catId,
            HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // 1. 检查登录状态
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            result.put("success", false);
            result.put("message", "请先登录");
            return ResponseEntity.status(401).body(result);
        }

        // 2. ADMIN 不限量，普通用户每天最多3张
        boolean isAdmin = currentUser instanceof Admin;
        if (!isAdmin) {
            LocalDate today = LocalDate.now();
            LocalDate lastUploadDate = currentUser.getLastUploadDate();
            Integer dailyCount = currentUser.getDailyUploadCount();

            if (lastUploadDate == null || !lastUploadDate.equals(today)) {
                dailyCount = 0;
            }

            if (dailyCount != null && dailyCount >= 3) {
                result.put("success", false);
                result.put("message", "今日上传次数已达上限（3张），请明天再试");
                result.put("limitReached", true);
                return ResponseEntity.badRequest().body(result);
            }
        }

        if (file.isEmpty()) {
            result.put("success", false);
            result.put("message", "文件为空");
            return ResponseEntity.badRequest().body(result);
        }

        try {
            createDirIfNotExists(UPLOAD_BASE_DIR);

            String originalName = file.getOriginalFilename();
            String ext = "";
            if (originalName != null && originalName.contains(".")) {
                ext = originalName.substring(originalName.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + ext;

            // 写入外部目录（项目同级，不在classpath中）
            Path uploadPath = Paths.get(UPLOAD_BASE_DIR + fileName);
            Files.write(uploadPath, file.getBytes());

            String url = "/pictures/" + fileName;

            // 保存时记录上传者ID
            pictureService.addPicture(catId, url, currentUser.getId());

            // 更新用户上传统计（仅普通用户）
            if (!isAdmin) {
                LocalDate today = LocalDate.now();
                LocalDate lastUploadDate = currentUser.getLastUploadDate();
                int newCount = 1;

                if (lastUploadDate != null && lastUploadDate.equals(today)) {
                    newCount = (currentUser.getDailyUploadCount() != null ? currentUser.getDailyUploadCount() : 0) + 1;
                }

                userService.updateUploadStats(currentUser.getId(), today, newCount);

                currentUser.setLastUploadDate(today);
                currentUser.setDailyUploadCount(newCount);
                session.setAttribute("currentUser", currentUser);


            }

            result.put("success", true);
            result.put("message", "上传成功");
            result.put("url", url);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "文件保存失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }
}