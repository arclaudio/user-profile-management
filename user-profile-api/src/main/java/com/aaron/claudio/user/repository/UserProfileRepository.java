package com.aaron.claudio.user.repository;

import com.aaron.claudio.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findByIdAndIsDeletedFalse(Long userId);

    List<UserProfile> findAllByIsDeletedFalse();

    boolean existsByEmailAddress(String emailAddress);
}
