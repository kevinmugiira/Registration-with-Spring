package com.example.springauthentication1.registration;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString

//declaring the class with attributes to be requested via http. Class is then called in the RegistrationService
public class RegistrationRequest {

    private final String firstname;
    private final String lastname;
    private final String email;
    private final String password;
}
