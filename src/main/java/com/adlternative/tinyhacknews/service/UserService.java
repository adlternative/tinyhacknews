package com.adlternative.tinyhacknews.service;

import com.adlternative.tinyhacknews.entity.UserInfo;
import com.adlternative.tinyhacknews.entity.UserRegister;

public interface UserService {

  UserInfo register(UserRegister user);

  UserInfo getSingleUserInfo(Long userId);
}
