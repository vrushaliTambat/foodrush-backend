package com.vrush.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vrush.dto.RestaurantDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     private long id;

    private String fullName;
    private String email;
    //By specifying WRITE_ONLY, we declare that the password property should only be written to JSON and not read from it.
    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    private String password;
    private USER_ROLE role=USER_ROLE.ROLE_CUSTOMER;

    @JsonIgnore
    @OneToMany(cascade=CascadeType.ALL, mappedBy="customer")
    private List<Order> orders=new ArrayList<>();

    @ElementCollection
    private List<RestaurantDto> favorites=new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses=new ArrayList<>();
}
