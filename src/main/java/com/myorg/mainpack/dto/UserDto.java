package com.myorg.mainpack.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myorg.mainpack.model.AbstractBaseEntity;
import com.myorg.mainpack.model.Role;
import com.myorg.mainpack.model.User;
import javax.validation.constraints.Size;
import java.util.Set;


public class UserDto extends AbstractBaseEntity {

    private boolean active;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password2;

    private Set<Role> roles;

    private int balance;

    private String updatePass;



    public UserDto(){}

    public UserDto(User user){
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.active = user.isActive();
        this.roles = user.getRoles();
        this.balance = user.getBalance();
    }



    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getUpdatePass() {
        return updatePass;
    }

    public void setUpdatePass(String updatePass) {
        this.updatePass = updatePass;
    }


}
