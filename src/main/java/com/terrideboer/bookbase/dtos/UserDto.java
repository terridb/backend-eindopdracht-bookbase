package com.terrideboer.bookbase.dtos;

import com.terrideboer.bookbase.models.enums.Role;

public class UserDto {
    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public String phoneNumber;
    public Role role;
}
