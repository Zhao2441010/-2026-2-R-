// package com.example.demo.service;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;  
// import org.mockito.Mock;                           
// import org.mockito.InjectMocks;                    
// import org.mockito.junit.jupiter.MockitoExtension; 

// import com.example.demo.entity.*;
// import com.example.demo.repository.MessageRepository; 
// import com.example.demo.service.MessageServiceImpl;
// @ExtendWith(MockitoExtension.class)  // 使用Mockito扩展
// public class Mytest {
    
//     @Mock
//     private MessageRepository messageRepository;  // 模拟MessageRepository

//     @InjectMocks
//     private MessageServiceImpl messageService;  // 注入MessageService

//     @Test  // 标记这是测试方法
//     void test(){

//         Message message = new Message(1L, "Hello, World!");

//         messageService.saveMessage(message);
        
//         System.out.println("Hello, World!");


//     }
// }



package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import com.example.demo.entity.*;


@Slf4j
@SpringBootTest
@Transactional
@Rollback(false)  //不回滚,测试数据保留
public class Mytest {
    
    @Autowired
    private TaskService taskService;  // ← 改为接口，不是 MessageServiceImpl

    @Test
    void testSaveMessage() {

        Date date = new Date();
        Date datefuture = new Date();
        Date beforedate = new Date();
        Date afterdate = new Date();

        date.setTime(date.getTime());

        datefuture.setTime(date.getTime()+ 1000L * 60 * 60 * 24);
        beforedate.setTime(date.getTime()-1000L * 60 * 60 * 24);
        afterdate.setTime(date.getTime());

//        taskService.addTask("Feed",date,10L);                      今天
//        taskService.addTask("GoHospitol",datefuture,15L);          今天+1
//        taskService.addTask("Shower",beforedate,10L);              今天-1
//        taskService.addTask("Walk",afterdate,5L);                  今天

        System.out.println("all");+
        List<Task> tasks = taskService.querryAllEvent();
        for (Task task : tasks) {
            System.out.println(task.getDescription()+" "+task.getEventdate());
        }

        System.out.println("today");
        List<Task> today = taskService.querryTodayEvent(date);
        for (Task task : today) {
            System.out.println(task.getDescription()+" "+task.getEventdate());
        }

        System.out.println("future");
        List<Task> future = taskService.querryFutureEvent(datefuture);
        for (Task task : future) {
            System.out.println(task.getDescription()+" "+task.getEventdate());
        }

        taskService.updateEvemtTime(4L,new Date(afterdate.getTime()+1000L * 60 * 60 * 24*3));  //今天+3


        Task t= taskService.findTaskById(4L);










    }
}
