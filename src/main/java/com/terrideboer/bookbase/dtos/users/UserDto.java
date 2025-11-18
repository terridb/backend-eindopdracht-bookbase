package com.terrideboer.bookbase.dtos.users;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.terrideboer.bookbase.models.Role;

import java.util.Set;

public class UserDto {
    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;

    @JsonSerialize
    public Set<Role> roles;
}
