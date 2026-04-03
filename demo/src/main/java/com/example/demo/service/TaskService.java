package com.example.demo.service;


import com.example.demo.entity.Task;
import com.example.demo.entity.Volunteer;

import java.util.Date;
import java.util.List;

public interface TaskService {

    public void addTask(String description, Date eventDate,Long need);

    public int getVolunteernums(Long tid);

    public void updateEvemtTime(Long id, Date newDate);

    public void deleteEvent(Long id);

    public List<Task> querryAllEvent();

    public List<Task> querryTodayEvent(Date today);

    public List<Task> querryFutureEvent(Date eventdate);

    public Task findTaskById(Long id);
}
