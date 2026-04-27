package com.example.demo.service;

import com.example.demo.entity.Message;
import com.example.demo.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

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
    public void saveMessage(Long receiverId, String content,Date time){
        Message message = new Message(receiverId, content,time);
        messageRepository.save(message);
    }

    @Override
    @Transactional
    public void deleteMessage(Long id){
        messageRepository.deleteById(id);
    }

    @Override
    public List<Message> showAllMessages(Long receiverId) {
        return messageRepository.findAllByReceiverId(receiverId);
    }

    @Override
    public List<Message> showUncheckMessage(Long receiverId) {
        return messageRepository.findUnseenByReceiverId(receiverId);
    }

    @Override
    public int countUncheckMessage(Long receiverId) {
        return messageRepository.countUncheckMessage(receiverId);
    }

    @Override
    public int countAllMessage(Long receiverId) {
        return messageRepository.countAllMessage(receiverId);
    }

    @Override
    public void setSeen(Long id) {
        Message message = messageRepository.findById(id).orElse(null);
        if (message != null) {
            message.setSeen(true);
            messageRepository.save(message);
        }
    }


}
