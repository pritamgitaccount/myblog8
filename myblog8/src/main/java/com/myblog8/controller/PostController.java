package com.myblog8.controller;

import com.myblog8.payload.PostDto;
import com.myblog8.payload.PostResponse;
import com.myblog8.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // create blog post rest api
    //http://localhost:8080/api/posts
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createPost(@Valid @RequestBody PostDto postDto, BindingResult result) {
        //<?> - This becomes a dynamic return type depending on what I am returning
        //adding @Valid annotation dose that, whatever data goes to PostDto,that data will be checked, @Size or @NotEmpty
        //BindingResult- It binds error with Postman, This has a potential of checking is in 'PostDto' here any error or not
        if (result.hasErrors()) {
            return new ResponseEntity<>(result.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            PostDto dto = postService.createPost(postDto);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        }
    }

    @DeleteMapping("{postId}")
    public ResponseEntity<String> deletePostById(@PathVariable int postId) {
        postService.deletePostById(postId);
        return new ResponseEntity<>("Post has deleted with Id" + postId, HttpStatus.OK);
    }

    @GetMapping("{postId}")
    public ResponseEntity<PostDto> getPostByPostId(@PathVariable int postId) {
        //  PostDto dto = postService.getPostByPostId(postId);
        return new ResponseEntity<>(this.postService.getPostByPostId(postId), HttpStatus.OK);
    }

    //http://localhost:8080/api/posts?pageNo=0&pageSize=3&sortBy=content&sortDir=desc
    @GetMapping
    public PostResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PostResponse postResponse = postService.getAllPosts(pageNo, pageSize, sortBy, sortDir);
        return postResponse;
    }
}