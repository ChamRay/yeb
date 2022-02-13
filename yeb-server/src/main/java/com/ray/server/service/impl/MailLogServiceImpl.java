package com.ray.server.service.impl;

import com.ray.server.pojo.MailLog;
import com.ray.server.mapper.MailLogMapper;
import com.ray.server.service.IMailLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author ray
 * @since 2021-04-16
 */
@Service
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements IMailLogService {

}
