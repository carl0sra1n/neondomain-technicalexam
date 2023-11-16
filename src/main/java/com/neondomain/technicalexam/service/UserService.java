package com.neondomain.technicalexam.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.neondomain.technicalexam.model.User;

@Service
public interface UserService {
    List<User> getAllUsers();
    User findByUserName(String username);
    User findUserById(Long userId);
    void deleteById(Long userId);
    void saveUser(User user);
}
