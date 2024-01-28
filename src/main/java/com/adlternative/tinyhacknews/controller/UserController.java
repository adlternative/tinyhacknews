package com.adlternative.tinyhacknews.controller;

import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.entity.UserRegister;
import com.adlternative.tinyhacknews.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
  @Autowired UserService userService;

  @PostMapping
  public UserInfo registerUser(@RequestBody UserRegister user) {
    return userService.register(user);
  }

  @GetMapping
  public UserInfo getUserInfo(@RequestParam(name = "id") Long id) {
    return userService.getSingleUserInfo(id);
  }
}
