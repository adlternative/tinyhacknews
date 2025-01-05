package com.adlternative.tinyhacknews.controller;

import com.adlternative.tinyhacknews.auth.JwtUtils;
import com.adlternative.tinyhacknews.context.RequestContext;
import com.adlternative.tinyhacknews.entity.NewsData;
import com.adlternative.tinyhacknews.entity.UpdateUserInfoDTO;
import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.entity.UserLoginInputDTO;
import com.adlternative.tinyhacknews.entity.UserRegister;
import com.adlternative.tinyhacknews.service.NewsService;
import com.adlternative.tinyhacknews.service.UserService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.time.Duration;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
@Slf4j
public class UsersController {
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
   * @param updateUserInfoDTO
   * @return
   */
  @PutMapping
  public UserInfo updateUserInfo(@RequestBody UpdateUserInfoDTO updateUserInfoDTO) {
    return userService.updateUserInfo(updateUserInfoDTO);
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
  public IPage<NewsData> getAllNewsOfUser(
      @PathVariable(name = "id") Long id,
      @RequestParam(name = "page_num", defaultValue = "1") Long pageNum,
      @RequestParam(name = "page_size", defaultValue = "10") Long pageSize) {
    return newsService.getAllNewsOfUser(id, pageNum, pageSize);
  }

  /**
   * 遍历所有用户，有分页
   *
   * @param pageNum
   * @param pageSize
   * @return
   */
  @GetMapping("/all")
  public IPage<UserInfo> getAllUsersInfo(
      @RequestParam(name = "page_num", defaultValue = "1") Long pageNum,
      @RequestParam(name = "page_size", defaultValue = "10") Long pageSize) {
    return userService.getAllUsersInfo(pageNum, pageSize);
  }

  /**
   * 用户登录
   *
   * @param userLoginInputDTO
   * @param response
   * @return
   */
  @PostMapping("/login")
  public ResponseEntity<UserInfo> login(
      @RequestBody UserLoginInputDTO userLoginInputDTO, HttpServletResponse response) {
    UserInfo userInfo =
        userService.validateUser(userLoginInputDTO.getUsername(), userLoginInputDTO.getPassword());
    // 假设用户名和密码验证成功，生成 JWT
    String token = JwtUtils.generateToken(userInfo.getId(), userLoginInputDTO.getUsername());

    ResponseCookie cookie =
        ResponseCookie.from("jwt", token)
            .httpOnly(false) // 禁止js读取
            .secure(false) // 在http下也传输
            .domain("localhost") // 域名
            .path("/") // path
            .maxAge(Duration.ofDays(1))
            .build();

    response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

    return ResponseEntity.ok(userInfo);
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout() {
    // TODO: 也许应该记录用户登录登出状态之类的...
    Long userId = RequestContext.getUserId();
    UserInfo userInfo = userService.getSingleUserInfo(userId);
    log.info(String.format("User logout: %s", userInfo.getName()));

    return ResponseEntity.ok().build();
  }
}
