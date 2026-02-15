package com.asus.Collision.Catcher.Controller;

import com.asus.Collision.Catcher.Entity.User;
import com.asus.Collision.Catcher.Service.HardwareService;
import com.asus.Collision.Catcher.Service.UserDetailsServiceImpl;
import com.asus.Collision.Catcher.Service.UserService;
import com.asus.Collision.Catcher.Utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private HardwareService hardwareService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;


    @GetMapping("/healthCheck")
    public ResponseEntity<?> healthCheck()
    {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user)
    {
        try
        {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            if(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                String jwtToken = jwtUtils.generateToken(userDetails.getUsername());
                return new ResponseEntity<>(jwtToken, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
        catch (AuthenticationException e) {
            log.error("Error while generating token",e);
            return new ResponseEntity<>("Incorrect Username or Password",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login-admin")
    public ResponseEntity<?> loginAdmin(@RequestBody User user)
    {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUserName());
            if(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
            {
                String jwtToken = jwtUtils.generateToken(userDetails.getUsername());
                return new ResponseEntity<>(jwtToken,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        catch (AuthenticationException e)
        {
            log.error("Error while generating token",e);
            return new ResponseEntity<>("Incorrect Username or Password",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?>signup(@RequestBody User user)
    {
        boolean res = false;
        res = userService.saveNewUser(user);
        if(res)
        {
            return  new ResponseEntity<>(user,HttpStatus.CREATED);
        }
        return new ResponseEntity<>(user,HttpStatus.ALREADY_REPORTED);
    }
}
