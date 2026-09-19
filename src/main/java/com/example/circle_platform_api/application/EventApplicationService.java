package com.example.circle_platform_api.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.circle_platform_api.common.exception.BusinessException;
import com.example.circle_platform_api.common.response.PageResult;
import com.example.circle_platform_api.common.response.ResultCode;
import com.example.circle_platform_api.entity.Circle;
import com.example.circle_platform_api.entity.CircleMember;
import com.example.circle_platform_api.entity.Event;
import com.example.circle_platform_api.mapstruct.EventConvert;
import com.example.circle_platform_api.model.dto.EventCreateDTO;
import com.example.circle_platform_api.model.dto.EventQueryDTO;
import com.example.circle_platform_api.model.vo.EventVo;
import com.example.circle_platform_api.security.utils.SecurityUtils;
import com.example.circle_platform_api.service.CircleMemberService;
import com.example.circle_platform_api.service.CircleService;
import com.example.circle_platform_api.service.EventService;
import com.example.circle_platform_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventApplicationService {

    private final UserService userService;
    private final CircleService circleService;
    private final EventService eventService;
    private final CircleMemberService memberService;
    private final EventConvert eventConvert;


    public PageResult<EventVo> getPublicEventList(EventQueryDTO dto) {

        Page<Event> page = Page.of(dto.getPageNum(), dto.getPageSize());

        boolean selectedCircle = Objects.nonNull(dto.getCircleId());

        eventService.lambdaQuery()
                .like(StringUtils.hasText(dto.getEventTitle()), Event::getTitle, dto.getEventTitle())
                .eq(selectedCircle, Event::getCircleId, dto.getCircleId())
                .eq(Event::getIsPublic, 1)
                .page(page);

        if (page.getRecords().isEmpty()) {
            return PageResult.empty(dto.getPageNum(), dto.getPageSize());
        }

        List<EventVo> vos;
        if (selectedCircle) {
            Circle circle = circleService.getById(dto.getCircleId());
            vos = page.getRecords().stream()
                    .filter(Objects::nonNull)
                    .map(e -> {
                        Integer remain = null;
                        if (e.getMaxCapacity() != null) {
                            remain = e.getMaxCapacity() - e.getCurrentCount();
                        }
                        return new EventVo(
                                e.getId(),
                                e.getTitle(),
                                e.getCircleId(),
                                circle.getName(),
                                e.getLocation(),
                                e.getStartTime(),
                                remain
                        );
                    })
                    .toList();
            return PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), vos);
        }

        Set<Long> circleIds = page.getRecords().stream()
                .map(Event::getCircleId)
                .collect(Collectors.toSet());

        List<Circle> circles = circleService.listByIds(circleIds);
        Map<Long, Circle> circleMap = circles.stream().collect(Collectors.toMap(Circle::getId, c -> c));

        vos = page.getRecords().stream()
                .filter(Objects::nonNull)
                .map(e -> {
                    Circle circle = circleMap.get(e.getCircleId());
                    Integer remain = null;
                    if (e.getMaxCapacity() != null) {
                        remain = e.getMaxCapacity() - e.getCurrentCount();
                    }
                    return new EventVo(
                            e.getId(),
                            e.getTitle(),
                            e.getCircleId(),
                            circle.getName(),
                            e.getLocation(),
                            e.getStartTime(),
                            remain
                    );
                })
                .toList();

        return PageResult.of(page.getTotal(), page.getCurrent(), page.getSize(), page.getPages(), vos);
    }

    public void createEvent(Long circleId, EventCreateDTO dto){

        Circle circle=circleService.getById(circleId);

        if (circle==null){
            throw new BusinessException(ResultCode.CIRCLE_NOT_FOUND);
        }

        CircleMember member=memberService.lambdaQuery()
                .eq(CircleMember::getCircleId,circleId)
                .eq(CircleMember::getUserId, SecurityUtils.getCurrentUserId())
                .one();

        if (member==null){
            throw new BusinessException(ResultCode.MEMBER_NOT_FOUND);
        }

        Event event=eventConvert.createToEntity(dto);
        event.setCreatorId(SecurityUtils.getCurrentUserId());
        event.setCircleId(circleId);

        eventService.save(event);

    }

}
