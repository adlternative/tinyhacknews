package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.entity.UpdateUserInfoDTO;
import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.entity.UserRegister;
import com.adlternative.tinyhacknews.entity.Users;
import com.adlternative.tinyhacknews.exception.InternalErrorException;
import com.adlternative.tinyhacknews.exception.InvalidArgException;
import com.adlternative.tinyhacknews.exception.UserNotFoundException;
import com.adlternative.tinyhacknews.exception.UsernameExistsException;
import com.adlternative.tinyhacknews.mapper.UsersMapper;
import com.adlternative.tinyhacknews.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UsersMapper usersMapper;

  @Override
  public UserInfo register(UserRegister userRegister) {
    LocalDateTime date = LocalDateTime.now();
    Users user =
        Users.builder()
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
      int affectedRows = usersMapper.insert(user);
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
        Optional.ofNullable(usersMapper.selectById(userId))
            .orElseThrow((() -> new UserNotFoundException("User not found for id: " + userId))));
  }

  @Override
  public UserInfo updateUserInfo(Long userId, UpdateUserInfoDTO updateUserInfoDTO) {
    if (userId == null) {
      throw new InvalidArgException("UserId is null");
    }

    Users user =
        Optional.ofNullable(usersMapper.selectById(userId))
            .orElseThrow((() -> new UserNotFoundException("User not found for id: " + userId)));

    if (!StringUtils.isNullOrEmpty(updateUserInfoDTO.getName())) {
      user.setUsername(updateUserInfoDTO.getName());
    }
    if (!StringUtils.isNullOrEmpty(updateUserInfoDTO.getEmail())) {
      user.setEmail(updateUserInfoDTO.getEmail());
    }
    user.setUpdatedAt(LocalDateTime.now());

    int affectedRows = usersMapper.update(user, new QueryWrapper<Users>().eq("id", userId));
    if (affectedRows == 0) {
      throw new InternalErrorException("Failed to update user");
    }
    return new UserInfo(user);
  }

  @Override
  public void deleteUser(Long id) {
    int affectedRows = usersMapper.delete(new QueryWrapper<Users>().eq("id", id));
    if (affectedRows == 0) {
      throw new UserNotFoundException("User not found for id: " + id);
    }
  }

  @Override
  public UserInfo findByUserName(String name) {
    if (StringUtils.isNullOrEmpty(name)) {
      throw new InvalidArgException("Name is empty");
    }

    return new UserInfo(
        usersMapper
            .findByUserName(name)
            .orElseThrow((() -> new UserNotFoundException("User not found for name: " + name))));
  }

  @Override
  public IPage<UserInfo> getAllUsersInfo(Long pageNum, Long pageSize) {
    return usersMapper
        .selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<>())
        .convert(UserInfo::convertFrom);
  }
}
