package com.myblog8.service;

import com.myblog8.payload.PostDto;
import com.myblog8.payload.PostResponse;

public interface PostService {
    PostDto createPost(PostDto postDto);

    void deletePostById(int postId);

    PostDto getPostByPostId(int postId);


    PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);
}
