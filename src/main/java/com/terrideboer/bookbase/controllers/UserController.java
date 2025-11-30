package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.fines.FineDto;
import com.terrideboer.bookbase.dtos.loans.LoanWithFineDto;
import com.terrideboer.bookbase.dtos.reservations.ReservationDto;
import com.terrideboer.bookbase.dtos.users.UserDto;
import com.terrideboer.bookbase.dtos.users.UserInputDto;
import com.terrideboer.bookbase.dtos.users.UserPatchDto;
import com.terrideboer.bookbase.models.enums.RoleName;
import com.terrideboer.bookbase.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //       Endpoint to get all users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    //       Endpoint to get a user by user-id
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);

        return ResponseEntity.ok(userDto);
    }

    //       Endpoint to create a user
    @PostMapping
    public ResponseEntity<UserDto> postUser(@Valid @RequestBody UserInputDto userInputDto) {
        UserDto userDto = userService.postUser(userInputDto);

        URI uri = URI.create("/users/" + userDto.id);

        return ResponseEntity.created(uri).body(userDto);
    }

    //       Endpoint to delete a user by user-id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    //       Endpoint to adjust a user by user-id
    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserPatchDto userPatchDto) {
        UserDto userDto = userService.updateUser(id, userPatchDto);

        return ResponseEntity.ok(userDto);
    }

    //       Endpoint to get all loans by user-id
    @GetMapping("{id}/loans")
    public ResponseEntity<List<LoanWithFineDto>> getLoansByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getLoansByUserId(id));
    }

    //       Endpoint to get all reservations by user-id
    @GetMapping("{id}/reservations")
    public ResponseEntity<List<ReservationDto>> getReservationsByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getReservationsByUserId(id));
    }

    //       Endpoint to get all fines by user-id
    @GetMapping("{id}/fines")
    public ResponseEntity<List<FineDto>> getFinesByUserId(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getFinesByUserId(id));
    }

    //       Endpoint to assign a new role to an existing user by user-id
    @PostMapping("/{id}/roles/{role}")
    public ResponseEntity<UserDto> addUserRole(@PathVariable Long id, @PathVariable("role") RoleName role) {
        UserDto user = userService.addRole(id, role);

        return ResponseEntity.ok(user);
    }

    //       Endpoint to delete a role from an existing user by user-id
    @DeleteMapping(value = "/{id}/roles/{role}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable Long id, @PathVariable("role") RoleName role) {
        userService.removeRole(id, role);
        return ResponseEntity.noContent().build();
    }
}
