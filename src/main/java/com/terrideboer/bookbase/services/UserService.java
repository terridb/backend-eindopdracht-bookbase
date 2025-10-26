package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.users.UserDto;
import com.terrideboer.bookbase.dtos.users.UserInputDto;
import com.terrideboer.bookbase.dtos.users.UserPatchDto;
import com.terrideboer.bookbase.exceptions.InvalidInputException;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.UserMapper;
import com.terrideboer.bookbase.models.User;
import com.terrideboer.bookbase.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtoUsers = new ArrayList<>();

        for (User user : users) {
            dtoUsers.add(UserMapper.toDto(user));
        }

        return dtoUsers;
    }

    public UserDto getUserById(Long id) {
        return UserMapper.toDto(
                userRepository.findById(id)
                        .orElseThrow(() -> new RecordNotFoundException("User with id " + id + " not found")));
    }

    public UserDto postUser(UserInputDto userInputDto) {
        User user = UserMapper.toEntity(userInputDto, null);

        User savedUser = userRepository.save(user);

        return UserMapper.toDto(savedUser);
    }

    public UserDto putUser(Long id, UserInputDto userInputDto) {
        User existingUser =   userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + id + " not found"));

        User updatedUser = UserMapper.toEntity(userInputDto, existingUser);
        User savedUser = userRepository.save(updatedUser);
        return UserMapper.toDto(savedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + id + " not found"));

        userRepository.delete(user);
    }

    public UserDto patchUser(Long id, UserPatchDto userPatchDto) {
        User existingUser =   userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + id + " not found"));

        if (userPatchDto.firstName != null) {
            if (userPatchDto.firstName.length() < 2 || userPatchDto.firstName.length() > 128) {
                throw new InvalidInputException("First name must be between 2 and 128 characters");
            }
            existingUser.setFirstName(userPatchDto.firstName);
        }

        if (userPatchDto.lastName != null) {
            if (userPatchDto.lastName.length() < 2 || userPatchDto.lastName.length() > 128) {
                throw new InvalidInputException("Last name must be between 2 and 128 characters");
            }
            existingUser.setLastName(userPatchDto.lastName);
        }

        if (userPatchDto.email != null) {
            if (!userPatchDto.email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new InvalidInputException("Email must be a valid email address");
            }
            existingUser.setEmail(userPatchDto.email);
        }

        if (userPatchDto.phoneNumber != null) {
            if (!userPatchDto.phoneNumber.matches("^\\+?[0-9]{8,15}$")) {
                throw new InvalidInputException("Phone number must be a valid phone number, e.g. +31612345678");
            }
            existingUser.setPhoneNumber(userPatchDto.phoneNumber);
        }

        if (userPatchDto.password != null) {
            if (userPatchDto.password.equals(existingUser.getPassword())) {
                throw new InvalidInputException("Password cannot be the same as previous password");
            }
            existingUser.setPassword(userPatchDto.password);
        }

        User savedUser = userRepository.save(existingUser);
        return UserMapper.toDto(savedUser);
    }
}
