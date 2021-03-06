package com.ray.server.service.impl;

import com.ray.server.pojo.Role;
import com.ray.server.mapper.RoleMapper;
import com.ray.server.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
