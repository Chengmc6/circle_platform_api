package com.example.circle_platform_api.service;

import com.example.circle_platform_api.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GAOMING
 * @since 2026-08-09
 */
public interface UserService extends IService<User> {
    default User getUserByUsername(String username){
        return lambdaQuery().eq(User::getUsername,username).getEntity();
    }
}
