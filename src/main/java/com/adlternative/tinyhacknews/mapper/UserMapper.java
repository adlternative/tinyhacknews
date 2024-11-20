package com.adlternative.tinyhacknews.mapper;

import com.adlternative.tinyhacknews.entity.User;
import java.util.Optional;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {

  @Insert(
      "INSERT INTO Users (username, email, password, createdAt, updatedAt) VALUES (#{username}, #{email}, #{password}, #{createdAt}, #{updatedAt})")
  @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  int insert(User user);

  @Select("SELECT * FROM Users WHERE id = #{id}")
  Optional<User> selectById(@Param("id") Long id);

  // 更新用户信息
  @Update(
      "UPDATE Users SET username=#{username}, email=#{email}, updatedAt=#{updatedAt} WHERE id=#{id}")
  int update(User user);

  @Delete("DELETE FROM Users WHERE id=#{id}")
  int delete(@Param("id") Long id);

  @Select("SELECT * FROM Users WHERE username=#{name}")
  Optional<User> findByUserName(@Param("name") String name);
}
