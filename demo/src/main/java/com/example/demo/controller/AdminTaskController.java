package com.example.demo.controller;

import com.example.demo.entity.Task;
import com.example.demo.entity.TU;
import com.example.demo.entity.User;
import com.example.demo.service.MessageService;
import com.example.demo.service.TaskService;
import com.example.demo.service.TUService;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/tasks")
public class AdminTaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TUService tuService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> getAllActiveTasks() {
        List<Task> tasks = taskService.findActive();
        List<Map<String, Object>> result = tasks.stream()
                .map(this::convertTaskToMap)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{tid}/detail")
    public ResponseEntity<Map<String, Object>> getTaskDetail(@PathVariable Long tid) {
        Task task = taskService.findTaskById(tid);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> result = new HashMap<>();
        result.put("task", convertTaskToMap(task));

        List<TU> tus = tuService.findTUByTaskId(tid);
        List<Map<String, Object>> volunteers = new ArrayList<>();
        for (TU tu : tus) {
            User user = userService.getUserById(tu.getUserId());
            if (user != null) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("uid", user.getId());
                userMap.put("username", user.getUsername());
                userMap.put("realname", user.getRealname());
                userMap.put("phoneNumber", user.getPhoneNumber());
                userMap.put("gender", user.getGender());
                volunteers.add(userMap);
            }
        }
        result.put("volunteers", volunteers);
        result.put("volunteerCount", volunteers.size());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createTask(
            @RequestBody Map<String, Object> request) {

        Map<String, Object> result = new HashMap<>();
        try {
            String description = (String) request.get("description");
            String eventDateStr = (String) request.get("eventDate");
            Long need = Long.valueOf(request.get("need").toString());

            Date eventDate = DATE_FORMAT.parse(eventDateStr);
            taskService.addTask(description, eventDate, need);

            result.put("success", true);
            result.put("message", "任务创建成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "创建失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/{tid}/update")
    public ResponseEntity<Map<String, Object>> updateTask(
            @PathVariable Long tid,
            @RequestBody Map<String, Object> request) {

        Map<String, Object> result = new HashMap<>();
        Task task = taskService.findTaskById(tid);
        if (task == null) {
            result.put("success", false);
            result.put("message", "任务不存在");
            return ResponseEntity.badRequest().body(result);
        }

        try {
            String description = (String) request.get("description");
            String eventDateStr = (String) request.get("eventDate");
            Long need = Long.valueOf(request.get("need").toString());

            Date oldDate = task.getEventdate();
            Date newDate = DATE_FORMAT.parse(eventDateStr);

            taskService.updateTask(tid, description, newDate, need);

            // 如果日期发生变化，通知已报名用户
            if (!isSameDay(oldDate, newDate)) {
                List<TU> tus = tuService.findTUByTaskId(tid);
                String taskDesc = task.getDescription();
                String oldDateStr = DATE_FORMAT.format(oldDate);
                String newDateStr = DATE_FORMAT.format(newDate);

                for (TU tu : tus) {
                    Long uid = tu.getUserId();
                    String msgContent = String.format(
                            "您报名的任务【%s】日期已由 %s 修改为 %s，请留意最新安排。",
                            taskDesc, oldDateStr, newDateStr
                    );
                    messageService.saveMessage(uid, msgContent, new Date());
                }
            }

            result.put("success", true);
            result.put("message", "任务更新成功");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "更新失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @DeleteMapping("/{tid}")
    public ResponseEntity<Map<String, Object>> deleteTask(@PathVariable Long tid) {
        Map<String, Object> result = new HashMap<>();
        Task task = taskService.findTaskById(tid);

        if (task == null) {
            result.put("success", false);
            result.put("message", "任务不存在");
            return ResponseEntity.badRequest().body(result);
        }

        try {
            Calendar calToday = Calendar.getInstance();
            calToday.set(Calendar.HOUR_OF_DAY, 0);
            calToday.set(Calendar.MINUTE, 0);
            calToday.set(Calendar.SECOND, 0);
            calToday.set(Calendar.MILLISECOND, 0);
            Date today = calToday.getTime();

            Calendar calTask = Calendar.getInstance();
            calTask.setTime(task.getEventdate());
            calTask.set(Calendar.HOUR_OF_DAY, 0);
            calTask.set(Calendar.MINUTE, 0);
            calTask.set(Calendar.SECOND, 0);
            calTask.set(Calendar.MILLISECOND, 0);
            Date taskDate = calTask.getTime();

            boolean isExpired = taskDate.before(today);

            if (!isExpired) {
                List<TU> tus = tuService.findTUByTaskId(tid);
                String taskDesc = task.getDescription();
                String dateStr = DATE_FORMAT.format(task.getEventdate());

                for (TU tu : tus) {
                    Long uid = tu.getUserId();

                    Calendar calMsg = Calendar.getInstance();
                    calMsg.setTime(today);
                    calMsg.add(Calendar.HOUR_OF_DAY, -12);
                    Date msgTime = calMsg.getTime();

                    String msgContent = String.format(
                            "您报名的任务【%s（%s）】已被管理员取消，该任务记录已删除。",
                            taskDesc, dateStr
                    );
                    messageService.saveMessage(uid, msgContent, msgTime);
                    tuService.deleteByTU(tid, uid);
                }
            }

            taskService.deleteEvent(tid);

            result.put("success", true);
            result.put("message", isExpired ? "过期任务已删除（保留报名记录）" : "任务已删除，已通知报名用户");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @GetMapping("/{tid}/export")
    public void exportTaskExcel(@PathVariable Long tid, HttpServletResponse response) throws IOException {
        Task task = taskService.findTaskById(tid);
        if (task == null) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "任务不存在");
            return;
        }

        List<TU> tus = tuService.findTUByTaskId(tid);
        List<User> volunteers = new ArrayList<>();
        for (TU tu : tus) {
            User user = userService.getUserById(tu.getUserId());
            if (user != null) {
                volunteers.add(user);
            }
        }

        Workbook workbook = new XSSFWorkbook();

        // 只保留报名用户Sheet，字段为：姓名、ID、手机号
        Sheet userSheet = workbook.createSheet("已报名用户");
        createVolunteerSheet(userSheet, volunteers);

        String fileName = URLEncoder.encode(
                "任务报名用户_" + task.getId() + "_" + DATE_FORMAT.format(task.getEventdate()) + ".xlsx",
                StandardCharsets.UTF_8
        ).replaceAll("\\+", "%20");

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setHeader("Content-Transfer-Encoding", "binary");

        workbook.write(response.getOutputStream());
        workbook.close();
        response.getOutputStream().flush();
    }

    private Map<String, Object> convertTaskToMap(Task task) {
        Map<String, Object> map = new HashMap<>();
        map.put("tid", task.getId());
        map.put("description", task.getDescription());
        map.put("need", task.getNeed());
        map.put("eventDate", DATE_FORMAT.format(task.getEventdate()));
        map.put("ended", task.isEnded());

        int currentNum = tuService.countVolunteerByTaskId(task.getId());
        map.put("currentNum", currentNum);
        map.put("full", currentNum >= task.getNeed());

        return map;
    }

    private boolean isSameDay(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)
                && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH);
    }

    private void createVolunteerSheet(Sheet sheet, List<User> volunteers) {
        Workbook wb = sheet.getWorkbook();

        CellStyle headerStyle = wb.createCellStyle();
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);

        CellStyle contentStyle = wb.createCellStyle();
        contentStyle.setBorderBottom(BorderStyle.THIN);
        contentStyle.setBorderLeft(BorderStyle.THIN);
        contentStyle.setBorderRight(BorderStyle.THIN);
        contentStyle.setBorderTop(BorderStyle.THIN);

        

        // 表头：姓名、ID、手机号
        String[] headers = {"姓名", "用户ID", "手机号"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        for (int i = 0; i < volunteers.size(); i++) {
            User user = volunteers.get(i);
            Row row = sheet.createRow(i + 1);

            row.createCell(0).setCellValue(user.getUsername());
            row.createCell(1).setCellValue(user.getId());
            row.createCell(2).setCellValue(user.getPhoneNumber());

            for (int j = 0; j < headers.length; j++) {
                row.getCell(j).setCellStyle(contentStyle);
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}