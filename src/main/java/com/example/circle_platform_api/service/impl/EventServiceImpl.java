package com.example.circle_platform_api.service.impl;

import com.example.circle_platform_api.entity.Event;
import com.example.circle_platform_api.mapper.EventMapper;
import com.example.circle_platform_api.service.EventService;
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
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements EventService {

}
