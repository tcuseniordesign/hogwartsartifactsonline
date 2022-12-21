package edu.tcu.cs.hogwartsartifactsonline.controller.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UserDto {

    private Integer id;
    @NotEmpty(message = "username is required")
    private String username;

    @NotNull(message = "enabled is required")
    private boolean enabled;
    @NotEmpty(message = "user roles are required")
    private String roles; // comma separated strings

    public UserDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
