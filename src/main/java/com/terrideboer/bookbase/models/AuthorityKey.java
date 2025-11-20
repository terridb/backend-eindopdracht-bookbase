package com.terrideboer.bookbase.models;

import com.terrideboer.bookbase.models.enums.RoleName;

import java.io.Serializable;

public class AuthorityKey implements Serializable {
    private Long user;
    private RoleName role;
}
