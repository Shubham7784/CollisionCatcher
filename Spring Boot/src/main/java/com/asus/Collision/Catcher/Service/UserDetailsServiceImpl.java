package com.asus.Collision.Catcher.Service;

import com.asus.Collision.Catcher.Entity.User;
import com.asus.Collision.Catcher.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException
    {
        User user = userRepository.findByuserName(userName);
        if(user!=null)
        {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUserName())
                    .password(user.getPassword())
                    .authorities(new SimpleGrantedAuthority(user.getRole().getFirst()))
                    .build();
        }
        throw new UsernameNotFoundException("User Not Found with UserName :- "+userName);
    }

}
