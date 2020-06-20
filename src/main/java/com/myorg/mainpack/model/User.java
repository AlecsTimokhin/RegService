package com.myorg.mainpack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.myorg.mainpack.dto.UserDto;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.validation.constraints.Size;
import javax.persistence.*;
import java.util.Collection;
import java.util.Set;


@Entity
@Table(name = "USERS", uniqueConstraints = {@UniqueConstraint(columnNames = "USERNAME", name = "users_unique_username_idx")})
public class User extends AbstractBaseEntity implements UserDetails {

    @Column(name = "ACTIVE")
    private boolean active;

    @Column(name = "USERNAME")
    private String username;

    @JsonIgnore
    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "BALANCE")
    private int balance;


    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "USER_ROLES", joinColumns = @JoinColumn(name = "USER_ID"))
    @Enumerated(EnumType.STRING)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Role> roles;



    public User(){}

    public User(Long id){
        this.id = id;
    }

    public User(String username, String password, boolean active,  Set<Role> roles){
        this.username = username;
        this.password = password;
        this.active = active;
        this.roles = roles;
    }

    public User(UserDto userDto){
        this.username = userDto.getUsername();
        this.password = userDto.getPassword();
        this.active = userDto.isActive();
        this.roles = userDto.getRoles();
        this.balance = userDto.getBalance();
    }



    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
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
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles();
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


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}