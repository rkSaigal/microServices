package com.lcwd.user.user.service.service;

import com.lcwd.user.user.service.entities.User;

import java.util.List;

public interface UserService {

    //user operation

    //create
    User saveUser(User user);

    //get all user

    List<User> getAllUser();


    //get single user of give UserId

    User getUser(String userId);


    //TODO:delete
    //TODO:update

}
