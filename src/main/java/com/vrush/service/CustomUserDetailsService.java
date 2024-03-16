package com.vrush.service;

import com.vrush.model.USER_ROLE;
import com.vrush.model.User;
import com.vrush.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//this class is created because we don't want spring security to generate auto password but we want to use our password

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
         User user=userRepository.findByEmail(username);
         if(user==null){
             throw new UsernameNotFoundException("User not found with email"+username);
         }
         //if user with username exists get role
        USER_ROLE role=user.getRole();
         //default role is customer
         List<GrantedAuthority> authorities=new ArrayList<>();
          //adding roles inside authorities
         authorities.add(new SimpleGrantedAuthority(role.toString()));
//User class is provided by Spring Security which implements UserDetails interface hence we are returning
// this object which is used to represent user details during authentication and authorization processes.
        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
    }
}
