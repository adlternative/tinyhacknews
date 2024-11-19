package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.entity.UpdateUserInfoDTO;
import com.adlternative.tinyhacknews.entity.User;
import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.entity.UserRegister;
import com.adlternative.tinyhacknews.exception.InternalErrorException;
import com.adlternative.tinyhacknews.exception.InvalidArgException;
import com.adlternative.tinyhacknews.exception.UserNotFoundException;
import com.adlternative.tinyhacknews.exception.UsernameExistsException;
import com.adlternative.tinyhacknews.mapper.UserMapper;
import com.adlternative.tinyhacknews.service.UserService;
import com.mysql.cj.util.StringUtils;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserMapper userMapper;

  @Override
  public UserInfo register(UserRegister userRegister) {
    Date date = new Date();
    User user =
        User.builder()
            .username(userRegister.getName())
            .email(userRegister.getEmail())
            .password(userRegister.getPassword())
            .createdAt(date)
            .updatedAt(date)
            .build();
    log.info(user.getEmail());
    log.info(user.getPassword());
    log.info(user.getUsername());

    try {
      int affectedRows = userMapper.insert(user);
      if (affectedRows == 0) {
        throw new InternalErrorException("Failed to insert user");
      }
      log.debug("affectedRows=" + affectedRows);
    } catch (DuplicateKeyException e) {
      // log
      throw new UsernameExistsException(
          "The username '" + user.getUsername() + "' is already taken.");
    }
    return new UserInfo(user);
  }

  @Override
  public UserInfo getSingleUserInfo(Long userId) {
    return new UserInfo(
        userMapper
            .selectById(userId)
            .orElseThrow((() -> new UserNotFoundException("User not found for id: " + userId))));
  }

  @Override
  public UserInfo updateUserInfo(Long userId, UpdateUserInfoDTO updateUserInfoDTO) {
    if (userId == null) {
      throw new InvalidArgException("userId is null");
    }

    User user =
        userMapper
            .selectById(userId)
            .orElseThrow((() -> new UserNotFoundException("User not found for id: " + userId)));

    if (!StringUtils.isNullOrEmpty(updateUserInfoDTO.getName())) {
      user.setUsername(updateUserInfoDTO.getName());
    }
    if (!StringUtils.isNullOrEmpty(updateUserInfoDTO.getEmail())) {
      user.setEmail(updateUserInfoDTO.getEmail());
    }
    user.setUpdatedAt(new Date());

    int affectedRows = userMapper.update(user);
    if (affectedRows == 0) {
      throw new InternalErrorException("Failed to update user");
    }
    return new UserInfo(user);
  }

  @Override
  public void deleteUser(Long id) {
    int affectedRows = userMapper.delete(id);
    if (affectedRows == 0) {
      throw new UserNotFoundException("User not found for id: " + id);
    }
  }

  @Override
  public UserInfo findByUserName(String name) {
    if (StringUtils.isNullOrEmpty(name)) {
      throw new InvalidArgException("name is empty");
    }

    return new UserInfo(
        userMapper
            .findByUserName(name)
            .orElseThrow((() -> new UserNotFoundException("User not found for name: " + name))));
  }
}
