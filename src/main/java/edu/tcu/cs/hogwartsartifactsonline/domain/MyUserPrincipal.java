package edu.tcu.cs.hogwartsartifactsonline.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserPrincipal implements UserDetails {

    private HogwartsUser hogwartsUser;
    private List<GrantedAuthority> authorities;

    public MyUserPrincipal(HogwartsUser hogwartsUser) {
        this.hogwartsUser = hogwartsUser;

        // Convert a user's roles from space-delimited string to a list of SimpleGrantedAuthority objects
        // E.g., john's roles are stored in a string like "admin user moderator", we need to convert it to a list of GrantedAuthority
        this.authorities = Arrays.stream(StringUtils.tokenizeToStringArray(hogwartsUser.getRoles(), " "))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    public MyUserPrincipal(HogwartsUser hogwartsUser, Collection<? extends GrantedAuthority> authorities) {
        this.hogwartsUser = hogwartsUser;
        this.authorities = (List<GrantedAuthority>) authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return hogwartsUser.getPassword();
    }

    @Override
    public String getUsername() {
        return hogwartsUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return hogwartsUser.isEnabled();
    }

    public HogwartsUser getUser() {
        return hogwartsUser;
    }

}
