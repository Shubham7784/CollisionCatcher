package com.asus.Collision.Catcher.Controller;

import com.asus.Collision.Catcher.Entity.User;
import com.asus.Collision.Catcher.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;


    @GetMapping("/check-admin-login")
    public ResponseEntity<?> checkAdminLogin() {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (userName != null) {
            return new ResponseEntity<>(userName, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userService.getAllUsers().stream().filter(x -> x.getRole().equals("user")).toList();
        if (!allUsers.isEmpty()) {
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getUsers/{userName}")
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName) {
        User byName = userService.findByName(userName);
        if (byName != null) {
            return new ResponseEntity<>(byName, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{userName}")
    public ResponseEntity<?> deleteUserByUserName(@PathVariable String userName)
    {
        boolean deleted = userService.deleteByName(userName);
        if(deleted)
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
