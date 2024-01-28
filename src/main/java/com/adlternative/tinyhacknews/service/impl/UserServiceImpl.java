package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.entity.User;
import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.entity.UserRegister;
import com.adlternative.tinyhacknews.mapper.UserMapper;
import com.adlternative.tinyhacknews.service.UserService;
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
    return new UserInfo(user);
  }
}
