package com.terrideboer.bookbase.mappers;

import com.terrideboer.bookbase.dtos.users.UserDto;
import com.terrideboer.bookbase.dtos.users.UserInputDto;
import com.terrideboer.bookbase.dtos.users.UserSummaryDto;
import com.terrideboer.bookbase.models.User;

public class UserMapper {

    public static User toEntity(UserInputDto userInputDto, User user) {
        if (user == null) {
            user = new User();
        }

        user.setEmail(userInputDto.email);
        user.setFirstName(userInputDto.firstName);
        user.setLastName(userInputDto.lastName);
        user.setPassword(userInputDto.password);
        user.setPhoneNumber(userInputDto.phoneNumber);
        user.setRole(userInputDto.role);

        return user;
    }

    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();

        userDto.id = user.getId();
        userDto.email = user.getEmail();
        userDto.firstName = user.getFirstName();
        userDto.lastName = user.getLastName();
        userDto.phoneNumber = user.getPhoneNumber();
        userDto.role = user.getRole();

        return userDto;
    }

    public static UserSummaryDto toSummaryDto(User user) {
        UserSummaryDto userSummaryDto = new UserSummaryDto();

        userSummaryDto.id = user.getId();
        userSummaryDto.firstName = user.getFirstName();
        userSummaryDto.lastName = user.getLastName();

        return userSummaryDto;
    }

}
