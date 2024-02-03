package com.myblog8.controller;

import com.myblog8.payload.CommentDto;
import com.myblog8.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
    //http://localhost:8080/api/comments/{postId}
    @PostMapping("/{postId}")
    public ResponseEntity<CommentDto> saveComment (@RequestBody CommentDto commentDto, @PathVariable long postId) {
        CommentDto dto = commentService.saveComment(commentDto, postId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    //http://localhost:8080/api/comments/{postId}
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteByCommentId(@PathVariable long id) {
        commentService.deleteByCommentId(id);
        return new ResponseEntity<>("Comment has deleted", HttpStatus.OK);
    }

    //    @PutMapping("{id}")
//    public ResponseEntity<String> updateByCommentId(@RequestBody CommentDto commentDto, @PathVariable long id) {
//       CommentDto dto= commentService.updateByCommentId(commentDto,id);
//        return new ResponseEntity<>("Comment has updated", HttpStatus.OK);
//    }
    @PutMapping("{id}")
    public ResponseEntity<CommentDto> updateByCommentId(@RequestBody CommentDto commentDto, @PathVariable long id) {
        CommentDto dto = commentService.updateByCommentId(commentDto, id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<CommentDto> getCommentById(@PathVariable long id) {
//       CommentDto dto= commentService.getCommentById(id);
//        return null;
//    }
}