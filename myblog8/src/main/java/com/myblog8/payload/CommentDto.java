package com.myblog8.payload;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private long id;
    private String body;
    private String email;
    private String name;


}

