package com.example.circle_platform_api.service.impl;

import com.example.circle_platform_api.entity.User;
import com.example.circle_platform_api.mapper.UserMapper;
import com.example.circle_platform_api.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GAOMING
 * @since 2026-08-09
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
