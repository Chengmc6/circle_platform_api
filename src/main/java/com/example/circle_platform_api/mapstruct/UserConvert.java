package com.example.circle_platform_api.mapstruct;

import com.example.circle_platform_api.entity.User;
import com.example.circle_platform_api.model.dto.UserRegisterDTO;
import com.example.circle_platform_api.model.dto.UserUpdateDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface UserConvert {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User registerToUser(UserRegisterDTO dto);

}
