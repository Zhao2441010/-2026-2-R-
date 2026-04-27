package com.example.demo.service;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserService {

    void saveUser(String username,String gender,  String phoneNumber,String password);

    void deleteUserByID(Long id);

    User getUserById(Long id);

    List<User> getAllUser();

    boolean existsByPhonenumber(String phonenumber);

    User getUserByPhonenumber(String phonenumber);

}