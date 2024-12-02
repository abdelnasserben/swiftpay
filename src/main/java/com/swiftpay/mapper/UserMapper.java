package com.swiftpay.mapper;

import com.swiftpay.dto.UserDto;
import com.swiftpay.model.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto toDto(User user) {
        UserDto userDto = getUserDtoWithoutAgency(user);
        if (userDto == null) return null;

        userDto.setAgency(AgencyMapper.toDto(user.getAgency()));

        return userDto;
    }

    public static UserDto toDtoWithoutAgency(User user) {
        UserDto userDto = getUserDtoWithoutAgency(user);
        if (userDto == null) return null;
        userDto.setAgency(null);

        return userDto;
    }

    private static UserDto getUserDtoWithoutAgency(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());

        userDto.setRolesAsString(user.getRoles().stream()
                .map(r -> r.getRole().replaceAll("ROLE_", ""))
                .collect(Collectors.joining(", ")));
        return userDto;
    }
}
