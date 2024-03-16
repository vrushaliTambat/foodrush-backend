package com.vrush.service;

import com.vrush.config.JwtProvider;
import com.vrush.model.User;
import com.vrush.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        //extract email from jwt
    String email = jwtProvider.getEmailFromJwtToken(jwt);
    User user = userRepository.findByEmail(email);
      return user;
}

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email);
        if(user!=null) {
            throw new Exception("user not found");
        }
        return user;
    }
}
