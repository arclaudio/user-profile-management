package com.aaron.claudio.user.service.impl;

import com.aaron.claudio.user.constant.Messages;
import com.aaron.claudio.user.dto.UserProfileDto;
import com.aaron.claudio.user.entity.UserProfile;
import com.aaron.claudio.user.enums.Gender;
import com.aaron.claudio.user.enums.UserRole;
import com.aaron.claudio.user.exception.EmailAlreadyExistsException;
import com.aaron.claudio.user.exception.ResourceNotFoundException;
import com.aaron.claudio.user.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceImplTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    private UserProfile user;
    private UserProfileDto userDto;

    @BeforeEach
    void setUp() {
        user = new UserProfile();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmailAddress("john@example.com");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setDeleted(false);

        userDto = new UserProfileDto(
                "John Doe",
                "john1@example.com",
                Gender.MALE,
                LocalDate.of(1990, 1, 1),
                UserRole.USER
        );
    }
        @Test
        void testCreateUser_Success() {
            when(userProfileRepository.existsByEmailAddress(userDto.emailAddress())).thenReturn(false);
            when(userProfileRepository.save(any(UserProfile.class))).thenReturn(user);

            UserProfileDto result = userProfileService.createUser(userDto);

            assertNotNull(result);
            assertEquals(userDto.name(), result.name());
            verify(userProfileRepository, times(1)).save(any(UserProfile.class));
        }

        @Test
        void testCreateUser_EmailAlreadyExists() {
            when(userProfileRepository.existsByEmailAddress(userDto.emailAddress())).thenReturn(true);

            EmailAlreadyExistsException exception = assertThrows(
                    EmailAlreadyExistsException.class,
                    () -> userProfileService.createUser(userDto)
            );

            assertEquals(String.format(Messages.EMAIL_ALREADY_TAKEN, userDto.emailAddress()), exception.getMessage());
        }

        @Test
        void testGetUserProfileById_Found() {
            when(userProfileRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(user));

            UserProfileDto result = userProfileService.getUserProfileById(1L);

            assertNotNull(result);
            assertEquals(user.getName(), result.name());
        }

        @Test
        void testGetUserProfileById_NotFound() {
            when(userProfileRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> userProfileService.getUserProfileById(1L)
            );

            assertEquals(String.format(Messages.USER_NOT_FOUND, 1L), exception.getMessage());
        }

        @Test
        void testGetUserProfiles() {
            when(userProfileRepository.findAllByIsDeletedFalse()).thenReturn(List.of(user));

            List<UserProfileDto> result = userProfileService.getUserProfiles();

            assertEquals(1, result.size());
        }

        @Test
        void testUpdateUserProfile_Success() {
            when(userProfileRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(user));
            when(userProfileRepository.existsByEmailAddress(anyString())).thenReturn(false);
            when(userProfileRepository.save(any(UserProfile.class))).thenReturn(user);

            UserProfileDto updatedDto = new UserProfileDto("Jane Doe", "jane@example.com", userDto.gender(), userDto.birthDate(), userDto.role());
            UserProfileDto result = userProfileService.updateUserProfile(1L, updatedDto);

            assertNotNull(result);
            assertEquals("Jane Doe", result.name());
            verify(userProfileRepository, times(1)).save(any(UserProfile.class));
        }

        @Test
        void testUpdateUserProfile_UserNotFound() {
            when(userProfileRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> userProfileService.updateUserProfile(1L, userDto)
            );

            assertEquals(String.format(Messages.USER_NOT_FOUND, 1L), exception.getMessage());
        }

        @Test
        void testUpdateUserProfile_EmailAlreadyExists() {
            when(userProfileRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(user));
            when(userProfileRepository.existsByEmailAddress(userDto.emailAddress())).thenReturn(true);

            EmailAlreadyExistsException exception = assertThrows(
                    EmailAlreadyExistsException.class,
                    () -> userProfileService.updateUserProfile(1L, userDto)
            );

            assertEquals(String.format(Messages.EMAIL_ALREADY_TAKEN, userDto.emailAddress()), exception.getMessage());
        }

        @Test
        void testDeleteUserProfile_Success() {
            when(userProfileRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(user));

            userProfileService.deleteUserProfile(1L);

            assertTrue(user.isDeleted());
            verify(userProfileRepository, times(1)).save(any(UserProfile.class));
        }

        @Test
        void testDeleteUserProfile_UserNotFound() {
            when(userProfileRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(
                    ResourceNotFoundException.class,
                    () -> userProfileService.deleteUserProfile(1L)
            );

            assertEquals(String.format(Messages.USER_NOT_FOUND, 1L), exception.getMessage());
        }

}
