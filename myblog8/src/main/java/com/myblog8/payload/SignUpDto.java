package com.myblog8.payload;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
public class SignUpDto {
    private String name;
    private String username;
    private String email;
    private String password;
}

