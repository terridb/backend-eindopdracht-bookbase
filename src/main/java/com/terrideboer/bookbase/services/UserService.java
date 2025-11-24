package com.terrideboer.bookbase.services;

import com.terrideboer.bookbase.dtos.fines.FineDto;
import com.terrideboer.bookbase.dtos.loans.LoanWithFineDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationDto;
import com.terrideboer.bookbase.dtos.users.UserDto;
import com.terrideboer.bookbase.dtos.users.UserInputDto;
import com.terrideboer.bookbase.dtos.users.UserPatchDto;
import com.terrideboer.bookbase.exceptions.AlreadyExistsException;
import com.terrideboer.bookbase.exceptions.ForbiddenException;
import com.terrideboer.bookbase.exceptions.InvalidInputException;
import com.terrideboer.bookbase.exceptions.RecordNotFoundException;
import com.terrideboer.bookbase.mappers.FineMapper;
import com.terrideboer.bookbase.mappers.LoanMapper;
import com.terrideboer.bookbase.mappers.ReservationMapper;
import com.terrideboer.bookbase.mappers.UserMapper;
import com.terrideboer.bookbase.models.Loan;
import com.terrideboer.bookbase.models.Reservation;
import com.terrideboer.bookbase.models.Role;
import com.terrideboer.bookbase.models.User;
import com.terrideboer.bookbase.models.enums.RoleName;
import com.terrideboer.bookbase.repositories.RoleRepository;
import com.terrideboer.bookbase.repositories.UserRepository;
import com.terrideboer.bookbase.utils.UserUtils;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, PasswordEncoder encoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll(Sort.by("id").ascending());
        List<UserDto> dtoUsers = new ArrayList<>();

        for (User user : users) {
            dtoUsers.add(UserMapper.toDto(user));
        }

        return dtoUsers;
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + id + " not found"));

        if (!UserUtils.isOwnerOrAdmin(user)) {
            throw new ForbiddenException();
        }

        return UserMapper.toDto(user);
    }

    public UserDto postUser(UserInputDto userInputDto) {
        User user = UserMapper.toEntity(userInputDto, null);

        user.setPassword(encoder.encode(userInputDto.password));

        Role defaultRole = new Role();
        defaultRole.setRole(RoleName.ROLE_MEMBER);
        defaultRole.setUser(user);
        user.getRoles().add(defaultRole);

        User savedUser = userRepository.save(user);

        return UserMapper.toDto(savedUser);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + id + " not found"));

        userRepository.delete(user);
    }

    public UserDto patchUser(Long id, UserPatchDto userPatchDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + id + " not found"));

        if (!UserUtils.isOwnerOrAdmin(existingUser)) {
            throw new ForbiddenException();
        }

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
            existingUser.setPassword(encoder.encode(userPatchDto.password));
        }

        User savedUser = userRepository.save(existingUser);
        return UserMapper.toDto(savedUser);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException("User with email address " + email + " not found"));
    }

    public UserDto addRole(Long userId, RoleName roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + userId + " not found"));

        boolean alreadyHasRole = user.getRoles().stream()
                .anyMatch(r -> r.getRole() == roleName);

        if (alreadyHasRole) {
            throw new AlreadyExistsException("User already has role " + roleName);
        }

        Role role = new Role();
        role.setRole(roleName);
        role.setUser(user);

        user.getRoles().add(role);
        userRepository.save(user);
        return UserMapper.toDto(user);
    }

    public void removeRole(Long userId, RoleName roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + userId + " not found"));
        Role role = roleRepository.findByUser_IdAndRole(userId, roleName)
                .orElseThrow(() -> new RecordNotFoundException("User does not have role " + roleName));

        user.removeRole(role);
        userRepository.save(user);
    }

    public List<LoanWithFineDto> getLoansByUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + id + " not found"));

        if (!UserUtils.isOwnerOrAdmin(user)) {
            throw new ForbiddenException();
        }

        List<Loan> loans = user.getLoans();
        List<LoanWithFineDto> dtoLoans = new ArrayList<>();

        for (Loan loan : loans) {
            dtoLoans.add(LoanMapper.toLoanWithFineDto(loan));
        }

        return dtoLoans;
    }

    public List<ReservationDto> getReservationsByUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + id + " not found"));

        if (!UserUtils.isOwnerOrAdmin(user)) {
            throw new ForbiddenException();
        }

        List<Reservation> reservations = user.getReservations();
        List<ReservationDto> dtoReservations = new ArrayList<>();

        for (Reservation reservation : reservations) {
            dtoReservations.add(ReservationMapper.toDto(reservation));
        }

        return dtoReservations;
    }

    public List<FineDto> getFinesByUserId(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("User with id " + id + " not found"));

        if (!UserUtils.isOwnerOrAdmin(user)) {
            throw new ForbiddenException();
        }

        List<Loan> userLoans = user.getLoans();

        if (userLoans == null || userLoans.isEmpty()) {
            return Collections.emptyList();
        }

        List<FineDto> dtoFines = new ArrayList<>();

        for (Loan loan : userLoans) {
            if (loan.getFine() != null) {
                dtoFines.add(FineMapper.toDto(loan.getFine()));
            }
        }

        return dtoFines;
    }
}
