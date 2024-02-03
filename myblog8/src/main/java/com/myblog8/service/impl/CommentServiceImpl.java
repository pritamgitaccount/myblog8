package com.myblog8.service.impl;

import com.myblog8.entity.Comment;
import com.myblog8.entity.Post;
import com.myblog8.exception.ResourceNotFound;
import com.myblog8.payload.CommentDto;
import com.myblog8.repository.CommentRepository;
import com.myblog8.repository.PostRepository;
import com.myblog8.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public CommentServiceImpl
            (CommentRepository commentRepository,
             PostRepository postRepository,
             ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentDto saveComment(CommentDto dto, long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFound("Post not found with Id" + postId)
        );
//        Comment comment = new Comment();
//        comment.setId(dto.getId());
//        comment.setName(dto.getName());
//        comment.setEmail(dto.getEmail());
//        comment.setBody(dto.getBody());
//        comment.setPost(post);
        Comment comment = mapToComment(dto);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        CommentDto commentDto = new CommentDto();
        commentDto.setId(savedComment.getId());
        commentDto.setName(savedComment.getName());
        commentDto.setEmail(savedComment.getEmail());
        commentDto.setBody(savedComment.getBody());

        return commentDto;
    }

    @Override
    public void deleteByCommentId(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public CommentDto updateByCommentId(CommentDto commentDto, long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFound("Comment not found for Id:" + id));

        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        Comment savedComment = commentRepository.save(comment);

        CommentDto dto = new CommentDto();
        dto.setId(savedComment.getId());
        dto.setName(savedComment.getName());
        dto.setEmail(savedComment.getEmail());
        dto.setBody(savedComment.getBody());
        return dto;
    }

    public Comment mapToComment(CommentDto dto) {
        return modelMapper.map(dto, Comment.class);

    }
}