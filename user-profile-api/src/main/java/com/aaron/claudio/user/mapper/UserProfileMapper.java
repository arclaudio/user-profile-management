package com.aaron.claudio.user.mapper;

import com.aaron.claudio.user.dto.UserProfileDto;
import com.aaron.claudio.user.entity.UserProfile;

public class UserProfileMapper {

    public static UserProfile mapToEntity(UserProfileDto userProfileDto){
        UserProfile userProfile = new UserProfile();
        userProfile.setName(userProfileDto.name());
        userProfile.setEmailAddress(userProfileDto.emailAddress());
        userProfile.setGender(userProfileDto.gender());
        userProfile.setBirthDate(userProfileDto.birthDate());
        userProfile.setRole(userProfileDto.role());
        return userProfile;
    }

    public static UserProfileDto mapToDto(UserProfile userProfile){
        if (userProfile == null) {
            return null;
        }
        return new UserProfileDto(
                userProfile.getName(),
                userProfile.getEmailAddress(),
                userProfile.getGender(),
                userProfile.getBirthDate(),
                userProfile.getRole()
        );
    }
}
