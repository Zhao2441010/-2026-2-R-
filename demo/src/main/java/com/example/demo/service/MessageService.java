package com.example.demo.service;

import com.example.demo.entity.Message;

public interface MessageService {

    void saveMessage(Message message);
    void saveMessage(Long receiverId, String content);
    void deleteMessage(Long id);

}
