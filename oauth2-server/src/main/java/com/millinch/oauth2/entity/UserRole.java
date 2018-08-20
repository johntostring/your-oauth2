package com.millinch.oauth2.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This guy is lazy, nothing left.
 *
 * @author John Zhang
 */
@Entity
@Table(name = "user_role")
public class UserRole implements Serializable {

    private Long id;

    private OAuthUser user;
    private Role role;

    public UserRole(OAuthUser user, Role role) {
        this.user = user;
        this.role = role;
    }

    public UserRole() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    public OAuthUser getUser() {
        return user;
    }

    public void setUser(OAuthUser user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name =  "role_id")
    @JsonBackReference
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
