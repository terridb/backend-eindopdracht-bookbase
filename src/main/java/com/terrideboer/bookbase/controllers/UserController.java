package com.terrideboer.bookbase.controllers;

import com.terrideboer.bookbase.dtos.loans.LoanDto;
import com.terrideboer.bookbase.dtos.loans.LoanWithFineDto;
import com.terrideboer.bookbase.dtos.users.UserDto;
import com.terrideboer.bookbase.dtos.users.UserInputDto;
import com.terrideboer.bookbase.dtos.users.UserPatchDto;
import com.terrideboer.bookbase.services.LoanService;
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
    private final LoanService loanService;

    public UserController(UserService userService, LoanService loanService) {
        this.userService = userService;
        this.loanService = loanService;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> postUser(@Valid @RequestBody UserInputDto userInputDto) {
        UserDto userDto = userService.postUser(userInputDto);

        URI uri = URI.create("/users/" + userDto.id);

        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> putUser(@PathVariable Long id, @Valid @RequestBody UserInputDto userInputDto) {
        UserDto userDto = userService.putUser(id, userInputDto);

        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> patchUser(@PathVariable Long id, @RequestBody UserPatchDto userPatchDto) {
        UserDto userDto = userService.patchUser(id, userPatchDto);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("{id}/loans")
    public ResponseEntity<List<LoanWithFineDto>> getLoansByUserId(@PathVariable Long id) {

        return ResponseEntity.ok(loanService.getLoansByUserId(id));
    }
}
