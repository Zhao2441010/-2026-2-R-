package com.example.demo.service;


import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void saveUser(String username, String gender, String phoneNumber,String password) {
        userRepository.save(new User(username,gender,phoneNumber,password));
    }

    @Override
    @Transactional
    public void deleteUserByID(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.getUserById(id);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public boolean existsByPhonenumber(String phonenumber) {
        User u=userRepository.getUserByPhoneNumber(phonenumber);
        if(u!=null){return true;}
        return false;
    }


    @Override
    public User getUserByPhonenumber(String phonenumber) {
        return  userRepository.getUserByPhoneNumber(phonenumber);
    }


}
