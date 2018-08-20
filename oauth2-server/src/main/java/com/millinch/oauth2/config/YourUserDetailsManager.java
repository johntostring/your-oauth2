package com.millinch.oauth2.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.util.List;

/**
 * This guy is busy, nothing left
 *
 * @author John Zhang
 */
public class YourUserDetailsManager extends JdbcUserDetailsManager {

    String authoritiesByUsernameQuery = "";

    private static final String DEF_AUTHORITIES_BY_USERNAME_QUERY =
            "select r.code from user_role ur\n" +
                    "left join users u on u.id = ur.user_id\n" +
                    "left join role r on r.id = ur.role_id\n" +
                    "where u.username = ?\n" +
                    "union\n" +
                    "select a.code from role_authorities ra\n" +
                    "left join authorities a on a.id = ra.auth_id\n" +
                    "left join user_role ur on ur.role_id = ra.role_id\n" +
                    "left join users u on u.id = ur.user_id\n" +
                    "where u.username = ?";

    public YourUserDetailsManager(DataSource dataSource) {
        super(dataSource);
        this.authoritiesByUsernameQuery = DEF_AUTHORITIES_BY_USERNAME_QUERY;
    }

    @Override
    protected List<GrantedAuthority> loadUserAuthorities(String username) {
        return getJdbcTemplate().query(this.authoritiesByUsernameQuery,
                new String[] { username, username }, (rs, rowNum) -> {
                    String roleName =  getRolePrefix()+ rs.getString(1);
                    return new SimpleGrantedAuthority(roleName);
                });
    }
}
