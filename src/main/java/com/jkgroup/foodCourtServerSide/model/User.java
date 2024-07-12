package com.jkgroup.foodCourtServerSide.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jkgroup.foodCourtServerSide.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;


@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_user")
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(nullable = true)
    private String userName;


    @JsonIgnore
    @Column(nullable = true)
    private String password;


    @Column(nullable = true)
    private String email;

    @Column(nullable = true)
    private String phone;


    @Column(nullable = true)
    private String firstName;


    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private LocalDateTime dateOfBirth;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] avatar;

//    @JsonIgnore
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private UserRoleEnum roleEnum;

    @Column(nullable = true)
    private String resetToken;


    // [OVERRIDE] From UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority(roleEnum.name()));
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + roleEnum.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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


}
