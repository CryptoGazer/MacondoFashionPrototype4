package com.macondo_cs.MacondoFashionPrototype4.models;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserCreationDTO {
    private String name;
    private String email;
    private String password;
    private ZonedDateTime datetimeRegirested;
    private int totalBought;
    private double totalSpent;
    private String roles;
}