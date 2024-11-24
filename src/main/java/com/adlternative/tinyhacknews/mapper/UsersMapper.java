package com.adlternative.tinyhacknews.mapper;

import com.adlternative.tinyhacknews.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * Mapper 接口
 *
 * @author Baomidou
 * @since 2024-11-24
 */
@Mapper
@Component
public interface UsersMapper extends BaseMapper<Users> {
  @Select("SELECT * FROM users WHERE username=#{name}")
  Optional<Users> findByUserName(@Param("name") String name);
}
