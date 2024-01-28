package com.adlternative.tinyhacknews.controller;

import com.adlternative.tinyhacknews.entity.UserRegister;
import com.adlternative.tinyhacknews.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired UserService userService;

  @PostMapping("/register")
  public ResponseEntity<Boolean> registerUser(@RequestBody UserRegister user) {
    userService.register(user);
    return new ResponseEntity<>(true, HttpStatus.OK);
  }
}
