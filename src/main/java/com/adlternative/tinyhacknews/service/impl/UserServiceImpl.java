package com.adlternative.tinyhacknews.service.impl;

import com.adlternative.tinyhacknews.context.RequestContext;
import com.adlternative.tinyhacknews.entity.Users;
import com.adlternative.tinyhacknews.exception.InternalErrorException;
import com.adlternative.tinyhacknews.exception.InvalidArgException;
import com.adlternative.tinyhacknews.exception.UnauthorizedException;
import com.adlternative.tinyhacknews.exception.UserNotFoundException;
import com.adlternative.tinyhacknews.exception.UsernameExistsException;
import com.adlternative.tinyhacknews.mapper.UsersMapper;
import com.adlternative.tinyhacknews.models.UserInfo;
import com.adlternative.tinyhacknews.models.input.UpdateUserInfoInputDTO;
import com.adlternative.tinyhacknews.models.input.UserRegisterInputDTO;
import com.adlternative.tinyhacknews.models.output.SimpleUserInfoOutputDTO;
import com.adlternative.tinyhacknews.models.output.UserInfoOutputDTO;
import com.adlternative.tinyhacknews.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import java.time.LocalDateTime;
import java.util.Objects;
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
  public UserInfo register(UserRegisterInputDTO userRegisterInputDTO) {
    LocalDateTime date = LocalDateTime.now();
    Users user =
        Users.builder()
            .username(userRegisterInputDTO.getName())
            .email(userRegisterInputDTO.getEmail())
            .password(userRegisterInputDTO.getPassword())
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
  public SimpleUserInfoOutputDTO getSingleUserInfo(Long userId) {
    return SimpleUserInfoOutputDTO.from(getUserInfo(userId));
  }

  private UserInfo getUserInfo(Long userId) {
    return new UserInfo(
        Optional.ofNullable(usersMapper.selectById(userId))
            .orElseThrow((() -> new UserNotFoundException("User not found for id: " + userId))));
  }

  @Override
  public UserInfo updateUserInfo(UpdateUserInfoInputDTO updateUserInfoInputDTO) {
    Long userId = RequestContext.getUserId();
    if (userId == null) {
      throw new InvalidArgException("UserId is null");
    }

    Users user =
        Optional.ofNullable(usersMapper.selectById(userId))
            .orElseThrow((() -> new UserNotFoundException("User not found for id: " + userId)));

    if (!StringUtils.isNullOrEmpty(updateUserInfoInputDTO.getName())) {
      user.setUsername(updateUserInfoInputDTO.getName());
    }
    if (!StringUtils.isNullOrEmpty(updateUserInfoInputDTO.getEmail())) {
      user.setEmail(updateUserInfoInputDTO.getEmail());
    }
    user.setAbout(updateUserInfoInputDTO.getAbout());
    user.setUpdatedAt(LocalDateTime.now());

    int affectedRows = usersMapper.update(user, new QueryWrapper<Users>().eq("id", userId));
    if (affectedRows == 0) {
      throw new InternalErrorException("Failed to update user");
    }
    return new UserInfo(user);
  }

  @Override
  public void deleteUser(Long id) {
    // TODO: admin 才有删除权限
    int affectedRows = usersMapper.delete(new QueryWrapper<Users>().eq("id", id));
    if (affectedRows == 0) {
      throw new UserNotFoundException("User not found for id: " + id);
    }
  }

  @Override
  public SimpleUserInfoOutputDTO findByUserName(String name) {
    if (StringUtils.isNullOrEmpty(name)) {
      throw new InvalidArgException("Name is empty");
    }

    UserInfo userInfo =
        new UserInfo(
            usersMapper
                .findByUserName(name)
                .orElseThrow(
                    (() -> new UserNotFoundException("User not found for name: " + name))));
    return SimpleUserInfoOutputDTO.from(userInfo);
  }

  @Override
  public IPage<SimpleUserInfoOutputDTO> getAllUsersInfo(Long pageNum, Long pageSize) {
    return usersMapper
        .selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<>())
        .convert(UserInfo::convertFrom)
        .convert(SimpleUserInfoOutputDTO::from);
  }

  @Override
  public UserInfo validateUser(String username, String password) {
    // TODO: 密码需要加密
    if (StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(password)) {
      throw new InvalidArgException("Username or password is empty");
    }
    Users user =
        usersMapper
            .findByUserName(username)
            .orElseThrow(() -> new UserNotFoundException("User not found for name: " + username));
    if (!Objects.equals(user.getPassword(), password)) {
      throw new UnauthorizedException("Username or password is incorrect");
    }
    return UserInfo.convertFrom(user);
  }

  @Override
  public UserInfoOutputDTO getCurrentUserInfo() {
    return UserInfoOutputDTO.from(getUserInfo(RequestContext.getUserId()));
  }
}
