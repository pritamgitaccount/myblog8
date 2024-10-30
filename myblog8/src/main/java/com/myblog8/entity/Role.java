package com.myblog8.entity;


import lombok.*;
import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=60 , nullable = false)
    private String name;

    @ManyToMany(mappedBy = "roles")//here ManyToMany is actually don't require here, it has automatically done here
    private Set<User> users = new HashSet<>();


}
