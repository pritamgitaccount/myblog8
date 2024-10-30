package com.myblog8.payload;

import lombok.*;

@Getter
@Setter
public class SignUpDto {
    private String name;
    private String username;
    private String email;
    private String password;
}

