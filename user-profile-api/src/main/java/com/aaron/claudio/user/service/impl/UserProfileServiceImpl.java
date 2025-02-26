package com.aaron.claudio.user.service.impl;

import com.aaron.claudio.user.constant.Messages;
import com.aaron.claudio.user.dto.UserProfileDto;
import com.aaron.claudio.user.entity.UserProfile;
import com.aaron.claudio.user.exception.EmailAlreadyExistsException;
import com.aaron.claudio.user.exception.ResourceNotFoundException;
import com.aaron.claudio.user.mapper.UserProfileMapper;
import com.aaron.claudio.user.repository.UserProfileRepository;
import com.aaron.claudio.user.service.UserProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public UserProfileDto createUser(UserProfileDto userProfileDto) {

        if(userProfileRepository.existsByEmailAddress(userProfileDto.emailAddress())){
            throw new EmailAlreadyExistsException(
                    String.format(Messages.EMAIL_ALREADY_TAKEN, userProfileDto.emailAddress())
            );
        }
        UserProfile userProfile = UserProfileMapper.mapToEntity(userProfileDto);

        UserProfile savedUserProfile = userProfileRepository.save(userProfile);

        return UserProfileMapper.mapToDto(savedUserProfile);
    }

    @Override
    public UserProfileDto getUserProfileById(Long userId) {
        return userProfileRepository.findByIdAndIsDeletedFalse(userId)
                .map(UserProfileMapper::mapToDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(Messages.USER_NOT_FOUND, userId)
                ));

    }

    @Override
    public List<UserProfileDto> getUserProfiles() {
        List<UserProfile> userProfiles = userProfileRepository.findAllByIsDeletedFalse();

        return userProfiles
                .stream()
                .map(UserProfileMapper::mapToDto)
                .toList();
    }

    @Override
    public UserProfileDto updateUserProfile(Long userId, UserProfileDto userProfileDto) {
        UserProfile userProfile = userProfileRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(Messages.USER_NOT_FOUND, userId)
                ));

        if (!userProfile.getEmailAddress().equals(userProfileDto.emailAddress()) &&
                userProfileRepository.existsByEmailAddress(userProfileDto.emailAddress())) {
            throw new EmailAlreadyExistsException(
                    String.format(Messages.EMAIL_ALREADY_TAKEN, userProfileDto.emailAddress())
            );
        }

        Optional.ofNullable(userProfileDto.name()).ifPresent(userProfile::setName);
        Optional.ofNullable(userProfileDto.emailAddress()).ifPresent(userProfile::setEmailAddress);
        Optional.ofNullable(userProfileDto.gender()).ifPresent(userProfile::setGender);
        Optional.ofNullable(userProfileDto.birthDate()).ifPresent(userProfile::setBirthDate);
        Optional.ofNullable(userProfileDto.role()).ifPresent(userProfile::setRole);

        UserProfile updatedUserProfile = userProfileRepository.save(userProfile);

        return UserProfileMapper.mapToDto(updatedUserProfile);
    }

    @Override
    public void deleteUserProfile(Long userId) {
        UserProfile userProfile = userProfileRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(Messages.USER_NOT_FOUND, userId)
                ));

        userProfile.setDeleted(true);

        userProfileRepository.save(userProfile);
    }
}
