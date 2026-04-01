package com.example.demo.service;


import com.example.demo.entity.Task;

import java.util.Date;
import java.util.List;

public interface TaskService {

    public void addTask(String description, Date eventDate);

    public void updateEvemtTime(Long id, Date newDate);

    public void volunteerRegister(Long id);
    public void volunteerUnRegister(Long id);

    public void deleteEvent(Long id);

    public List<Task> querryAllEvent();

    public List<Task> querryTodayEvent(Date today);

    public List<Task> querryFutureEvent(Date eventdate);

}
