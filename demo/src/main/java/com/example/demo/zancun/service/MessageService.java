package com.example.demo.service;

import com.example.demo.entity.Message;
import java.util.Date;
import java.util.List;

public interface MessageService {

    void saveMessage(Message message);
    void saveMessage(Long receiverId, String content,Date time);
    void deleteMessage(Long id);

    List<Message> showAllMessages(Long receiverId);
    List<Message> showUncheckMessage(Long receiverId);
    int countUncheckMessage(Long receiverId);
    int countAllMessage(Long receiverId);

    void setSeen(Long id);

}
