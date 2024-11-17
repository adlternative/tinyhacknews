package com.adlternative.tinyhacknews.controller;

import com.adlternative.tinyhacknews.entity.UpdateUserInfoDTO;
import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.entity.UserRegister;
import com.adlternative.tinyhacknews.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping
  public UserInfo registerUser(@RequestBody UserRegister user) {
    return userService.register(user);
  }

  @GetMapping("/{id}")
  public UserInfo getUserInfo(@PathVariable("id") Long id) {
    return userService.getSingleUserInfo(id);
  }

  @GetMapping
  public UserInfo getUserInfoByUserName(@RequestParam(name = "name") String name) {
    return userService.findByUserName(name);
  }

  @PutMapping
  public UserInfo updateUserInfo(
      @RequestParam(name = "id") Long id, @RequestBody UpdateUserInfoDTO updateUserInfoDTO) {
    return userService.updateUserInfo(id, updateUserInfoDTO);
  }

  @DeleteMapping
  public void deleteUser(@RequestParam(name = "id") Long id) {
    userService.deleteUser(id);
  }
}
