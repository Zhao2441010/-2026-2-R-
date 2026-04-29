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
    private Service testService;  // ← 改为接口，不是 MessageServiceImpl



    @Test
    void test() {




        System.out.println("\n\nfinish\n");



    }
}














