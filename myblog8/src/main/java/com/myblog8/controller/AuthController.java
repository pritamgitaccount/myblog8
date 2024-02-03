package com.myblog8.controller;

import com.myblog8.entity.*;
import com.myblog8.payload.*;
import com.myblog8.repository.*;
import com.myblog8.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepo;  //data saving in database done by Repository
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody SignUpDto signUpDto) {
        if (userRepo.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
        }

        if (userRepo.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("Email already exist", HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        // Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        Role roles = roleRepository.findByName("ROLE_USER").get();
        Set<Role> role = new HashSet<>();
        role.add(roles);
        user.setRoles(role);
        // user.setRoles(Collections.singleton(roles));
        User savedUser = userRepo.save(user);
        //Here if we return savedUser the password also will be return, but it is a security thread
        //So, we will copy the data back to SignUpDto
        SignUpDto dto = new SignUpDto();
        dto.setName(savedUser.getName());
        dto.setUsername(savedUser.getUsername());
        dto.setEmail(savedUser.getEmail());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }


    //http://localhost:8080/api/auth/signin
    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto) {
        // 1. Authenticate the user using the provided credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        // 2. Set the authenticated user in the Spring Security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generate a JWT token using the tokenProvider
        String token = tokenProvider.generateToken(authentication);

        // 4. Create a new JWTAuthResponse object with the generated token
        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse(token);

        // 5. Return a ResponseEntity with the JWTAuthResponse object and HTTP status OK
        return ResponseEntity.ok(jwtAuthResponse);


//        Authentication authentication = authenticationManager.authenticate
//                (new UsernamePasswordAuthenticationToken(loginDto.getUsernameOrEmail(), loginDto.getPassword()));
//        //UsernamePasswordAuthenticationToken - this method verify username and password if these are valid then generate a
//        // token if not valid then not generate token
//        //and the generated token is pointed to 'authentication' this.
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        //SecurityContextHolder is a class in Spring Security that provides access to the security context of an application.
//        // It allows you to retrieve the currently authenticated user's security information, such as their authentication details
//        return new ResponseEntity<>("User signed in successfully!", HttpStatus.OK);
    }
}