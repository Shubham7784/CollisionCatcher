package com.asus.Collision.Catcher.Controller;

import com.asus.Collision.Catcher.Entity.ApiResponse;
import com.asus.Collision.Catcher.Entity.User;
import com.asus.Collision.Catcher.Service.AlertService;
import com.asus.Collision.Catcher.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AlertService alertService;

    @GetMapping("/check-login")
    public ResponseEntity<?> checkLogin()
    {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if(username!=null)
        {
            User byName = userService.findByName(username);
            return new ResponseEntity<>(byName,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getUserDetails")
    public ResponseEntity<?>getUserDetails()
    {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userName!=null)
        {
            User byName = userService.findByName(userName);
            return new ResponseEntity<>(byName,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody String email)
    {
        boolean deleted = false;
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userName!=null)
        {
            User byName = userService.findByName(userName);
            if(email.equals(byName.getUserName()))
            {
                deleted = userService.deleteByName(userName);

            }
        }
        if(deleted)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody User user)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        User userDb = userService.findByName(userName);
        userDb.setUserName((user.getUserName()!=null && !user.getUserName().isEmpty())?user.getUserName():userDb.getUserName());
        userDb.setAge((user.getAge()!=null && !user.getAge().isEmpty())?user.getAge():userDb.getAge());
        userDb.setName(!user.getName().isEmpty() ?user.getName():userDb.getName());
        userDb.setPhoneNo(!user.getPhoneNo().isEmpty() ?user.getPhoneNo():userDb.getPhoneNo());
        userDb.setHardware((user.getHardware()!=null)?user.getHardware():userDb.getHardware());
        User user1 = userService.saveUser(userDb);
        if(user1!=null)
            return new ResponseEntity<>(userDb,HttpStatus.ACCEPTED);
        return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
    }


    @PostMapping("/save-token")
    public ResponseEntity<?> saveFcmToken(
            @RequestParam String token) {

        String userName =
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getName();

        User user = userService.findByName(userName);
        user.setFcmToken(token);
        userService.saveUser(user);

        return ResponseEntity.ok(new ApiResponse<String>(true,"FCM Token Saved",null));
    }

    @DeleteMapping("/cancel-alert")
    public ResponseEntity<?> cancelAlert() {
        String userName = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        boolean deleted = alertService.deleteAlertByUserName(userName);
        if(deleted)
            return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/send-emergency-alert")
    public ResponseEntity<?> sendEmergencyAlert(){
        return ResponseEntity.ok(new ApiResponse<String>(true,"Emergency Alert Generated",null));
    }
}

