package com.neondomain.technicalexam.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.neondomain.technicalexam.model.DeleteResponse;
import com.neondomain.technicalexam.model.RegistrationResponse;
import com.neondomain.technicalexam.model.User;
import com.neondomain.technicalexam.model.UserDTO;
import com.neondomain.technicalexam.service.UserService;

@Controller
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    /****** Vistas ******/
    @GetMapping("/")
    public String viewHome(Model model){
        return "login";
    }

    @GetMapping("/public")
    public String viewPublic(Model model){
        return "login";
    }
    @GetMapping("/public/users")
    public String viewUsers(Model model){
        model.addAttribute("listUser", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/public/login")
    public String viewLogin(Model model){
        return "login";
    }
    
    /****** API ******/
    @GetMapping("/api/users")
    @ResponseBody
    public List<UserDTO> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .map(user -> new UserDTO(user.getId(), user.getUserName(), user.getFirstName(), user.getLastName(), user.getAge()))
                .collect(Collectors.toList());
    }

    @PostMapping("/api/users")
    @ResponseBody
    public ResponseEntity<RegistrationResponse> saveUser(@ModelAttribute("user") User user){
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.saveUser(user);
        return ResponseEntity.ok(new RegistrationResponse("User created successfully"));
    }

    @PatchMapping("/api/users/{userId}")
    @ResponseBody
    public ResponseEntity<RegistrationResponse> updateUser(@PathVariable Long userId, @ModelAttribute("user") User updatedUser) {
        User existingUser = userService.findUserById(userId);

        if (updatedUser.getUserName() != null) {
            existingUser.setUserName(updatedUser.getUserName());
        }

        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(new BCryptPasswordEncoder().encode(updatedUser.getPassword()));
        }

        if (updatedUser.getFirstName() != null) {
            existingUser.setFirstName(updatedUser.getFirstName());
        }

        if (updatedUser.getLastName() != null) {
            existingUser.setLastName(updatedUser.getLastName());
        }

        userService.saveUser(existingUser);
        return ResponseEntity.ok(new RegistrationResponse("User modified successfully"));
    }

    @DeleteMapping("/api/users/{userId}")
    @ResponseBody
    public ResponseEntity<DeleteResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteById(userId);
            return ResponseEntity.ok(new DeleteResponse("User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new DeleteResponse("Error deleting user: " + e.getMessage()));
        }
    }
}