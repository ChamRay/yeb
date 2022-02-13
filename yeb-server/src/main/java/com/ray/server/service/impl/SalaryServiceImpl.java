package com.ray.server.service.impl;

import com.ray.server.pojo.Salary;
import com.ray.server.mapper.SalaryMapper;
import com.ray.server.service.ISalaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author ray
 * @since 2021-04-16
 */
@Service
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements ISalaryService {

}
