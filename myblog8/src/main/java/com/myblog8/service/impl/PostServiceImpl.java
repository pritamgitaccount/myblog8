package com.myblog8.service.impl;

import com.myblog8.entity.Post;
import com.myblog8.exception.ResourceNotFound;
import com.myblog8.payload.PostDto;
import com.myblog8.payload.PostResponse;
import com.myblog8.repository.PostRepository;
import com.myblog8.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;  //It is a sub library of spring

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);

        PostDto dto = new PostDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setContent(post.getContent());
        return dto;
    }

    @Override
    public void deletePostById(int postId) {
        Post post = postRepository.findById((long) postId).orElseThrow(
                () -> new ResourceNotFound("Post is not found with Id" + postId));
        postRepository.deleteById((long) postId);
    }

    @Override
    public PostDto getPostByPostId(int postId) {
        Post post = postRepository.findById((long) postId).orElseThrow(
                () -> new ResourceNotFound("Post not found with Id" + postId)
        );
        return mapToDto(post);
    }

    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {
        // Step 1: Sorting Configuration
        //sortDir stands for sorting direction
        // It typically indicates whether the sorting should be done in ascending (asc) or descending (desc) order.
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // Step 2: Pagination Setup
        // Create Pageable instance to specify the page number, page size, and sorting configuration
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Step 3: Fetching Data
        // Fetch a page of posts from the database based on the pagination and sorting information
        Page<Post> all = postRepository.findAll(pageable);

        // Step 4: Processing Results
        // Get the content (list of posts) from the page object
        List<Post> posts = all.getContent();

        // Map each post to a PostDto using the mapToDto method and collect into a list
        List<PostDto> dtos = posts.stream().map(post -> mapToDto(post)).collect(Collectors.toList());

        // Step 5: Building Response
        // Create a PostResponse object and set its properties
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(dtos);
        postResponse.setPageNo(all.getNumber());
        postResponse.setTotalPages(all.getTotalPages());
        postResponse.setTotalElements((int) all.getNumberOfElements());
        postResponse.setPageSize(all.getSize());
        postResponse.setLast(all.isLast());

        // Step 6: Returns the Response
        // Return the PostResponse object containing the paginated and sorted list of posts
        return postResponse;
    }

    PostDto mapToDto(Post post) {  //purpose of this method convert the entity object to dto and return back a dto
        PostDto dto = modelMapper.map(post, PostDto.class);
//        PostDto dto = new PostDto();
//        dto.setId(post.getId());
//        dto.setTitle(post.getTitle());
//        dto.setDescription(post.getDescription());
//        dto.setContent(post.getContent());
        return dto;
    }
}