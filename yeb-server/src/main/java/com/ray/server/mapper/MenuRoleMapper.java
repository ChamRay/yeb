package com.ray.server.mapper;

import com.ray.server.pojo.MenuRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author ray
 * @since 2021-04-16
 */
@Mapper
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    /**
     * 批量更新角色菜单
     * @param rid
     * @param mid
     * @return
     */
    Integer insetBatch(@Param("rid") Integer rid,@Param("mids") Integer[] mid);
}
