package com.swd.smk.controller;

import com.swd.smk.dto.PostDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.services.interfac.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private IPostService postService;

    @PostMapping("/public/create-post/member/{memberId}")
    public ResponseEntity<Response> createPost(PostDTO postDTO, @PathVariable Long memberId) {
        postDTO.setMemberId(memberId);
        Response response = postService.createPost(postDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/public/update-post/{postId}")
    public ResponseEntity<Response> updatePost(@PathVariable Long postId, PostDTO postDTO) {
        Response response = postService.updatePost(postId, postDTO);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/public/delete-post/{postId}")
    public ResponseEntity<Response> deletePost(@PathVariable Long postId) {
        Response response = postService.deletePost(postId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/public/get-post/{postId}")
    public ResponseEntity<Response> getPostById(@PathVariable Long postId) {
        Response response = postService.getPostById(postId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/public/get-all-posts")
    public ResponseEntity<Response> getAllPosts() {
        Response response = postService.getAllPosts();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
