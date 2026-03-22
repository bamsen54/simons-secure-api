package com.simon.simonssecureapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 64)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 64)
    private String lastName;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id",  nullable = false)
    @JsonIgnoreProperties("members")
    private Address address;

    @Column(name = "email", nullable = false, length = 128)
    private String email;

    @Column(name = "phone", nullable = true, length = 24)
    private String phone;

    @Column(name = "date_of_birth", unique = true, nullable = false, length = 20)
    private String dateOfBirth;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, mappedBy = "member")
    @JsonIgnoreProperties("member")
    private AppUser appUser;

    public Member() {
    }

    public Member(String firstName, String lastName, Address address, String email, String phone, String dateOfBirth) {
        this.firstName   = firstName;
        this.lastName    = lastName;
        this.address     = address;
        this.email       = email;
        this.phone       = phone;
        this.dateOfBirth = dateOfBirth;
    }

    public Long getId() {
        return id;
    }

    public Member setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Member setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Member setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Member setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Member setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public Member setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public Address getAddress() {
        return address;
    }

    public Member setAddress(Address address) {
        this.address = address;
        return this;
    }

    public AppUser getAppUser() {
        return appUser;
    }

    public Member setAppUser(AppUser appUser) {
        this.appUser = appUser;
        return this;
    }
}