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
@Table(name = "role_authorities")
public class RoleAuthority implements Serializable {

    private static final long serialVersionUID = 8133285489854655043L;

    private Long id;
    private Role role;
    private Authority authority;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "role_id")
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "auth_id")
    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }
}
