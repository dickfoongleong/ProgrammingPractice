package com.lafarleaf.leafplanner.models;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.lafarleaf.leafplanner.utils.enums.UserRole;

import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity(name = "user")
public class User implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    private int id;

    @Column(name = "FIRST_NAME")
    private String fName;

    @Column(name = "LAST_NAME")
    private String lName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", length = 255)
    private UserRole role;

    @Column(name = "EXP_DATE")
    private Date expDate;

    @Column(name = "LOCK_DATE")
    private Date lockDate;

    @Column(name = "IS_ENABLED")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean isEnabled;

    public int getId() {
        return id;
    }

    public String getfName() {
        return fName;
    }

    public String getlName() {
        return lName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public Date getExpDate() {
        return expDate;
    }

    public Date getLockDate() {
        return lockDate;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String toString() {
        return this.fName + " " + this.lName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = role.getGrantedAuthorities();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name() + email));
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        Date currentDate = Calendar.getInstance().getTime();
        return currentDate.compareTo(expDate) <= 0;
    }

    @Override
    public boolean isAccountNonLocked() {
        Date currentDate = Calendar.getInstance().getTime();
        return lockDate == null || currentDate.compareTo(lockDate) >= 0;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
