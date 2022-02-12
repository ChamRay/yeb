package com.ray.server.service.impl;

import com.ray.server.pojo.Oplog;
import com.ray.server.mapper.OplogMapper;
import com.ray.server.service.IOplogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author cdh
 * @since 2021-04-16
 */
@Service
public class OplogServiceImpl extends ServiceImpl<OplogMapper, Oplog> implements IOplogService {

}
