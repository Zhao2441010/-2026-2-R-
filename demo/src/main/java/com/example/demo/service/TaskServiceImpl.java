package com.example.demo.service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service

public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public int getVolunteernums(Long tid) {
        return taskRepository.getHaveVolunteerById(tid);
    }


    @Override
    @Transactional
    public void addTask(String description, Date eventDate,Long need) {
        taskRepository.save(new Task(description, eventDate,need));
    }

    @Override
    @Transactional
    public void updateEvemtTime(Long id, Date newDate) {
        taskRepository.updateEventdateById(id, newDate);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> querryAllEvent() {
        return taskRepository.findAllEvent();
    }

    @Override
    public List<Task> querryTodayEvent(Date today) {
        return taskRepository.findByEventdate(today);
    }

    @Override
    public List<Task> querryFutureEvent(Date eventdate) {
        return taskRepository.findFutureEvent(eventdate);
    }

    @Override
    public List<Task> querryActiveEvent(Date eventdate) {
        return taskRepository.findActiveEvent(eventdate);
    }

    @Override
    public Task findTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public List<Task> findTop10Active(){
        List<Task>l1= findActive();

        List<Task>l2= new ArrayList<>();

        for(int i=0;i<l1.size();i++){
            if(i<10)l2.add(l1.get(i));
            else break;
        }
        return l2;

    }

    @Override
    public List<Task> findActive(){
        Date today = new Date();
        today.setTime(today.getTime()-1*60*60*1000);
        return taskRepository.findActiveEvent(today);
    }

}
