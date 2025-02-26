package com.aaron.claudio.user.controller;

import com.aaron.claudio.user.constant.Messages;
import com.aaron.claudio.user.dto.UserProfileDto;
import com.aaron.claudio.user.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<UserProfileDto> createUserProfile(
            @Valid @RequestBody UserProfileDto userProfileDto){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userProfileService.createUser(userProfileDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDto> getUserProfileById(@PathVariable Long id){
        return ResponseEntity.ok(userProfileService.getUserProfileById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserProfileDto>> getUserProfiles(){
        return ResponseEntity.ok(userProfileService.getUserProfiles());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileDto> updateUserProfile(@PathVariable Long id,
                                                            @Valid @RequestBody UserProfileDto userProfileDto){
        return ResponseEntity.ok(userProfileService.updateUserProfile(id, userProfileDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserProfile(@PathVariable Long id){
        userProfileService.deleteUserProfile(id);
        return ResponseEntity.ok(Messages.DELETE_USER_PROFILE_SUCCESS);
    }
}
