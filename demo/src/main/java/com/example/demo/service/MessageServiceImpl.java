package com.example.demo.service;

import com.example.demo.entity.Message;
import com.example.demo.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class MessageServiceImpl implements MessageService {
    
    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional
    public void saveMessage(Message msg){
        messageRepository.save(msg);
    }

    @Override
    @Transactional
    public void saveMessage(Long receiverId, String content){
        Message message = new Message(receiverId, content);
        messageRepository.save(message);
    }

    @Override
    @Transactional
    public void deleteMessage(Long id){
        messageRepository.deleteById(id);
    }
}
