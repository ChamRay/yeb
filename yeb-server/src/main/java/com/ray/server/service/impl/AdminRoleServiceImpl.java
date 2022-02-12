package com.ray.server.service.impl;

import com.ray.server.pojo.AdminRole;
import com.ray.server.mapper.AdminRoleMapper;
import com.ray.server.service.IAdminRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements IAdminRoleService {

}
