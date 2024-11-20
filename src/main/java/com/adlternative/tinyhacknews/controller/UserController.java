package com.adlternative.tinyhacknews.controller;

import com.adlternative.tinyhacknews.entity.NewsData;
import com.adlternative.tinyhacknews.entity.UpdateUserInfoDTO;
import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.entity.UserRegister;
import com.adlternative.tinyhacknews.service.NewsService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;
  private final NewsService newsService;

  /**
   * 注册用户
   *
   * @param user
   * @return
   */
  @PostMapping
  public UserInfo registerUser(@RequestBody UserRegister user) {
    return userService.register(user);
  }

  /**
   * 获取用户信息
   *
   * @param id
   * @return
   */
  @GetMapping("/{id}")
  public UserInfo getUserInfo(@PathVariable("id") Long id) {
    return userService.getSingleUserInfo(id);
  }

  /**
   * 根据用户名找到某个用户
   *
   * @param name
   * @return
   */
  @GetMapping
  public UserInfo getUserInfoByUserName(@RequestParam(name = "name") String name) {
    return userService.findByUserName(name);
  }

  /**
   * 更新用户信息
   *
   * @param id
   * @param updateUserInfoDTO
   * @return
   */
  @PutMapping
  public UserInfo updateUserInfo(
      @RequestParam(name = "id") Long id, @RequestBody UpdateUserInfoDTO updateUserInfoDTO) {
    return userService.updateUserInfo(id, updateUserInfoDTO);
  }

  /**
   * 删除某个用户
   *
   * @param id
   */
  @DeleteMapping
  public void deleteUser(@RequestParam(name = "id") Long id) {
    userService.deleteUser(id);
  }

  /**
   * 获取用户所有新闻
   *
   * @param id
   */
  @GetMapping("/{id}/news")
  public List<NewsData> getAllNewsOfUser(@PathVariable(name = "id") Long id) {
    // TODO: 添加分页逻辑
    return newsService.getAllNewsOfUser(id);
  }
}
