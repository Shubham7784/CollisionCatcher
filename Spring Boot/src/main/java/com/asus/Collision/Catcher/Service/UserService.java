package com.asus.Collision.Catcher.Service;

import com.asus.Collision.Catcher.Entity.User;
import com.asus.Collision.Catcher.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User saveUser(User userEntry)
    {
        return userRepository.save(userEntry);
    }
    public boolean saveNewUser(User userEntry)
    {
        boolean res = false;
        User byuserName = userRepository.findByuserName(userEntry.getUserName());
        if(byuserName==null)
        {
            userEntry.setPassword(passwordEncoder.encode(userEntry.getPassword()));
            ArrayList<String> role = userEntry.getRole();
            role.add("ROLE_USER");
            userEntry.setRole(role);
            saveUser(userEntry);
            res = true;
        }
        return res;
    }
    public boolean deleteByName(String userName) {
        User user = userRepository.findByuserName(userName);
        userRepository.delete(user);
        return true;
    }

    public User findByName(String userName) {
        return userRepository.findByuserName(userName);
    }

    public List<User> getAllUsers()
    {
        return userRepository.findAll();
    }
}
