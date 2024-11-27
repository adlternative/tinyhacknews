package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.entity.UpdateUserInfoDTO;
import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.entity.UserRegister;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface UserService {

  /**
   * 注册用户
   *
   * @param user
   * @return
   */
  UserInfo register(UserRegister user);

  /**
   * 获取单个用户信息
   *
   * @param userId
   * @return
   */
  UserInfo getSingleUserInfo(Long userId);

  /**
   * 更新用户信息
   *
   * @param userInfo
   * @return
   */
  UserInfo updateUserInfo(UpdateUserInfoDTO userInfo);

  /**
   * 删除用户
   *
   * @param id
   */
  void deleteUser(Long id);

  /**
   * 根据用户名查找用户
   *
   * @param name
   * @return
   */
  UserInfo findByUserName(String name);

  /**
   * 获取所有用户信息
   *
   * @param pageNum
   * @param pageSize
   * @return
   */
  IPage<UserInfo> getAllUsersInfo(Long pageNum, Long pageSize);

  /**
   * 验证用户登录信息
   *
   * @param username
   * @param password
   * @return
   */
  UserInfo validateUser(String username, String password);
}
