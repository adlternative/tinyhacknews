package com.adlternative.tinyhacknews.mapper;

import com.adlternative.tinyhacknews.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {

  @Insert(
      "INSERT INTO Users (username, email, password) VALUES (#{username}, #{email}, #{password})")
  int insert(User user);

  @Select("SELECT * FROM Users WHERE id = #{id}")
  User selectById(Long id);
}
