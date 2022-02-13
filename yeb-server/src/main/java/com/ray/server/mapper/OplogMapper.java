package com.ray.server.mapper;

import com.ray.server.pojo.Oplog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author ray
 * @since 2021-04-16
 */
@Mapper
public interface OplogMapper extends BaseMapper<Oplog> {

}
