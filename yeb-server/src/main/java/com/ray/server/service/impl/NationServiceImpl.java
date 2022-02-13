package com.ray.server.service.impl;

import com.ray.server.pojo.Nation;
import com.ray.server.mapper.NationMapper;
import com.ray.server.service.INationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author ray
 * @since 2021-04-16
 */
@Service
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements INationService {

}
