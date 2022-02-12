package com.ray.server.service.impl;

import com.ray.server.pojo.PoliticsStatus;
import com.ray.server.mapper.PoliticsStatusMapper;
import com.ray.server.service.IPoliticsStatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Service
public class PoliticsStatusServiceImpl extends ServiceImpl<PoliticsStatusMapper, PoliticsStatus> implements IPoliticsStatusService {

}
