package com.millinch.oauth2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * This guy is lazy, nothing left.
 *
 * @author John Zhang
 */
@Entity
@Table(name = "authorities")
public class Authority implements Serializable {

    private static final long serialVersionUID = 1976827747120166364L;

    private Long id;
    private String code;
    private String name;
    private String remark;

    private List<RoleAuthority> roleAuthorities;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "authority", cascade = CascadeType.REMOVE)
    public List<RoleAuthority> getRoleAuthorities() {
        return roleAuthorities;
    }

    public void setRoleAuthorities(List<RoleAuthority> roleAuthorities) {
        this.roleAuthorities = roleAuthorities;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Authority{");
        sb.append("id=").append(id);
        sb.append(", code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", roleAuthorities=").append(roleAuthorities);
        sb.append('}');
        return sb.toString();
    }
}
