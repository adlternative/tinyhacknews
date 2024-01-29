package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.entity.UpdateUserInfoDTO;
import com.adlternative.tinyhacknews.entity.User;
import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.entity.UserRegister;
import com.adlternative.tinyhacknews.mapper.UserMapper;
import com.adlternative.tinyhacknews.service.UserService;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
  @Autowired UserMapper userMapper;

  @Override
  public UserInfo register(UserRegister userRegister) {
    User user =
        User.builder()
            .username(userRegister.getName())
            .email(userRegister.getEmail())
            .password(userRegister.getPassword())
            .build();
    log.info(user.getEmail());
    log.info(user.getPassword());
    log.info(user.getUsername());

    int affectedRows = userMapper.insert(user);
    if (affectedRows == 0) {
      // throw exception
    }
    return new UserInfo(user);
  }

  @Override
  public UserInfo getSingleUserInfo(Long userId) {
    User user = userMapper.selectById(userId);
    if (user == null) {
      // throw exception
    }

    return new UserInfo(user);
  }

  @Override
  public UserInfo updateUserInfo(Long userId, UpdateUserInfoDTO updateUserInfoDTO) {
    User.UserBuilder userBuilder = User.builder();

    if (userId != null) {
      userBuilder.id(userId);
    }

    if (!StringUtils.isNullOrEmpty(updateUserInfoDTO.getName())) {
      userBuilder.username(updateUserInfoDTO.getName());
    }
    if (!StringUtils.isNullOrEmpty(updateUserInfoDTO.getEmail())) {
      userBuilder.email(updateUserInfoDTO.getEmail());
    }

    int affectedRows = userMapper.update(userBuilder.build());
    if (affectedRows == 0) {
      // throw exception
    }
    return UserInfo.builder()
        .id(userId)
        .name(updateUserInfoDTO.getName())
        .email(updateUserInfoDTO.getEmail())
        .build();
  }

  @Override
  public void deleteUser(Long id) {
    int affectedRows = userMapper.delete(id);
    if (affectedRows == 0) {
      // throw exception
    }
  }

  @Override
  public UserInfo findByUserName(String name) {
    User user = userMapper.findByUserName(name);
    return new UserInfo(user);
  }
}
