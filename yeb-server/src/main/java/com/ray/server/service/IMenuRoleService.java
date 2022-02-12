package com.ray.server.service;

import com.ray.server.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ray.server.pojo.rest.Result;

/**
 * @author cdh
 * @since 2021-04-16
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单
     * @param rid
     * @param mid
     * @return
     */
    Result updateMenuRole(Integer rid, Integer[] mid);
}
