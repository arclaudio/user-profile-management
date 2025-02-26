package com.aaron.claudio.user.dto;

import com.aaron.claudio.user.enums.Gender;
import com.aaron.claudio.user.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.Period;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserProfileDto (
        @NotBlank(message = "Name is required.")
        String name,

        @NotBlank(message = "Email is required.")
        @Email(message = "Invalid email format.")
        String emailAddress,

        @NotBlank(message = "Gender is required.")
        Gender gender,

        @NotNull(message = "Birth date is required.")
        LocalDate birthDate,

        @NotNull(message = "Role is required.")
        UserRole role
){
        public int getAge() {
                return (birthDate != null) ? Period.between(birthDate, LocalDate.now()).getYears() : 0;
        }
}
