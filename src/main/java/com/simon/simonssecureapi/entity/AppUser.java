package com.simon.simonssecureapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.simon.simonssecureapi.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 50)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    @JsonIgnoreProperties("appUser")
    private Member member;

    @Column(name = "role", nullable = false, length = 24)
    @Enumerated(EnumType.STRING)
    private Role role;


    public AppUser() {
    }

    public AppUser(String username, String password, Role role, Member member) {
        this.username = username;
        this.password = password;
        this.role     = role;
        this.member   = member;
    }

    public Long getId() {
        return id;
    }

    public AppUser setId(Long id) {
        this.id = id;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public AppUser setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AppUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public AppUser setRole(Role role) {
        this.role = role;
        return this;
    }

    public Member getMember() {
        return member;
    }

    public AppUser setMember(Member member) {
        this.member = member;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AppUser{");
        sb.append("id=").append(id);
        sb.append(", username='").append(username).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", member=").append(member);
        sb.append(", role=").append(role);
        sb.append('}');
        return sb.toString();
    }
}