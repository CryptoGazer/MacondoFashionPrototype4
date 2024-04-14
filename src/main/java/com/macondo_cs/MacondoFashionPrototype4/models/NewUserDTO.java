package com.macondo_cs.MacondoFashionPrototype4.models;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDTO {
    private String name;
    private String email;
    private ZonedDateTime datetimeRegirested;
    private int totalBought;
    private double totalSpent;
    private String roles;
}