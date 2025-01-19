package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.models.UserInfo;
import com.adlternative.tinyhacknews.models.input.UpdateUserInfoInputDTO;
import com.adlternative.tinyhacknews.models.input.UserRegisterInputDTO;
import com.adlternative.tinyhacknews.models.output.SimpleUserInfoOutputDTO;
import com.adlternative.tinyhacknews.models.output.UserInfoOutputDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface UserService {

  /**
   * 注册用户
   *
   * @param user
   * @return
   */
  UserInfo register(UserRegisterInputDTO user);

  /**
   * 获取单个用户信息
   *
   * @param userId
   * @return
   */
  SimpleUserInfoOutputDTO getSingleUserInfo(Long userId);

  /**
   * 更新用户信息
   *
   * @param userInfo
   * @return
   */
  UserInfo updateUserInfo(UpdateUserInfoInputDTO userInfo);

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
  SimpleUserInfoOutputDTO findByUserName(String name);

  /**
   * 获取所有用户信息
   *
   * @param pageNum
   * @param pageSize
   * @return
   */
  IPage<SimpleUserInfoOutputDTO> getAllUsersInfo(Long pageNum, Long pageSize);

  /**
   * 验证用户登录信息
   *
   * @param username
   * @param password
   * @return
   */
  UserInfo validateUser(String username, String password);

  /**
   * 获取当前用户信息
   *
   * @return
   */
  UserInfoOutputDTO getCurrentUserInfo();
}
