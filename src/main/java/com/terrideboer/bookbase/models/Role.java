package com.terrideboer.bookbase.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.terrideboer.bookbase.models.enums.RoleName;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@IdClass(AuthorityKey.class)
@Table(name = "roles")
public class Role implements Serializable {

    @Id
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName role;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public RoleName getRole() {
        return role;
    }

    public void setRole(RoleName role) {
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
