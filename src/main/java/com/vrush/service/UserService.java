package com.vrush.service;

import com.vrush.model.User;

public interface UserService {
    public User findUserByJwtToken(String jwt) throws Exception;

    public User findUserByEmail(String email) throws Exception;
}
