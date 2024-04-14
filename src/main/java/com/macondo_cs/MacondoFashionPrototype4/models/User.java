package com.macondo_cs.MacondoFashionPrototype4.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name="Users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "user_id_generator"
        )
    @SequenceGenerator(
        name = "user_id_generator",
        sequenceName = "id_generator_users",
        allocationSize = 1
    )
    @Column(name = "userID", nullable = false, unique = true)
    private Long userId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password; // of course, it will be saved as a hashed value, but a little bit later

    @Column(name = "datetimeRegirested", nullable = true)
    private ZonedDateTime datetimeRegirested;

    @Column(name = "totalBought", nullable = true)
    private int totalBought;

    @Column(name = "totalSpent", nullable = true)
    private double totalSpent;

    @Column(name = "roles")
    private String roles;

    // @OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    // private Cart cart;

    // this will be fulled by methods, binding the Users and Transactions databases (this functionality will be implemented in a specified method)
    // private ArrayList<Long> transactionIds = new ArrayList<>();


    // this constructor will be used for registration (fields email, password are added to an exemplar of User using standalone methods with their own specific functionality)
    public User(String name) {
        this.name = name;
        this.datetimeRegirested = initDatetimeRegistered();
        this.totalBought = 0;
        this.totalSpent = 0;
        this.roles = "ROLE_USER";
    }

    // @PrePersist
    // private void init() {

    // }

    private ZonedDateTime initDatetimeRegistered() {
        ZonedDateTime datetimeRegirested = ZonedDateTime.now(ZoneId.of("Europe/Vilnius"));
        return datetimeRegirested.truncatedTo(ChronoUnit.MINUTES);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return StringUtils.capitalize(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ZonedDateTime getDatetimeRegirested() {
        return datetimeRegirested;
    }

    public String getFormattedDatetimeRegistered() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDatetimeRegirested = datetimeRegirested.format(formatter);
        return formattedDatetimeRegirested;
    }

    public void setDatetimeRegirested() {
        this.datetimeRegirested = initDatetimeRegistered();
    }

    public int getTotalBought() {
        return totalBought;
    }

    public void setTotalBought(int totalBought) {
        this.totalBought = totalBought;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getName(String mode) throws Exception {
        return ServiceFunctionality.formatTableParameter(name, mode);
    }

    public String getEmail(String mode) throws Exception {
        return ServiceFunctionality.formatTableParameter(email, mode);
    }

    public String getPassword(String mode) throws Exception {
        return ServiceFunctionality.formatTableParameter(password, mode);
    }
}
