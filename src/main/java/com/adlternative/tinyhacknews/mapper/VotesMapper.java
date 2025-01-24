package com.adlternative.tinyhacknews.mapper;

import com.adlternative.tinyhacknews.entity.Votes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Mapper 接口
 *
 * @author adlternative
 * @since 2025-01-24
 */
@Mapper
@Component
public interface VotesMapper extends BaseMapper<Votes> {}
