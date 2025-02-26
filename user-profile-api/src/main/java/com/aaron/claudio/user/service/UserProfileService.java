package com.aaron.claudio.user.service;

import com.aaron.claudio.user.dto.UserProfileDto;

import java.util.List;

public interface UserProfileService {

    UserProfileDto createUser(UserProfileDto userProfileDto);

    UserProfileDto getUserProfileById(Long userId);

    List<UserProfileDto> getUserProfiles();

    UserProfileDto updateUserProfile(Long userId, UserProfileDto userProfileDto);

    void deleteUserProfile(Long userId);
}
