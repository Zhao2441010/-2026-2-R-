package com.example.demo.service;

import com.example.demo.entity.Task;
import com.example.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Task findTaskById(Long id) {
        return taskRepository.findById(id);
    }
}
