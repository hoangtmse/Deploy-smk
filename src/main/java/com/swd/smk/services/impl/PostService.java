package com.swd.smk.services.impl;

import com.swd.smk.dto.PostDTO;
import com.swd.smk.dto.Response;
import com.swd.smk.enums.Status;
import com.swd.smk.exception.OurException;
import com.swd.smk.model.Member;
import com.swd.smk.model.Post;
import com.swd.smk.repository.MemberRepository;
import com.swd.smk.repository.PostRepository;
import com.swd.smk.services.interfac.IPostService;
import com.swd.smk.utils.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Override
    public Response createPost(PostDTO postDTO) {
        Response response = new Response();
        try {
            Optional<Member> memberOpt = memberRepository.findById(postDTO.getMemberId());
            if (memberOpt.isEmpty()) {
                throw new OurException("Member not found with ID: " + postDTO.getMemberId());
            }
            // Create a new Post object and set its properties
            Post post = new Post();
            post.setMember(memberOpt.get());
            post.setTitle(postDTO.getTitle());
            post.setContent(postDTO.getContent());
            post.setPostDate(LocalDateTime.now());
            post.setDateCreated(LocalDate.now());
            post.setDateUpdated(LocalDate.now());
            post.setStatus(Status.ACTIVE);
            // Save the post to the repository
            post = postRepository.save(post);
            response.setStatusCode(200);
            response.setMessage("Post created successfully");
            response.setPost(Converter.convertPostToDTO(post));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updatePost(Long postId, PostDTO postDTO) {
        Response response = new Response();
        try {
            Optional<Post> postOpt = postRepository.findById(postId);
            if (postOpt.isEmpty()) {
                throw new OurException("Post not found with ID: " + postId);
            }
            Post post = postOpt.get();
            if (postDTO.getTitle() != null) {
                post.setTitle(postDTO.getTitle());
            }
            if (postDTO.getContent() != null) {
                post.setContent(postDTO.getContent());
            }
            post.setPostDate(LocalDateTime.now());
            post.setDateUpdated(LocalDate.now());

            // Save the updated post to the repository
            post = postRepository.save(post);
            response.setStatusCode(200);
            response.setMessage("Post updated successfully");
            response.setPost(Converter.convertPostToDTO(post));
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deletePost(Long postId) {
        Response response = new Response();
        try {
            Optional<Post> postOpt = postRepository.findById(postId);
            if (postOpt.isEmpty()) {
                throw new OurException("Post not found with ID: " + postId);
            }
            Post post = postOpt.get();
            post.setStatus(Status.DELETED);
            post.setDateUpdated(LocalDate.now());
            // Save the updated post to mark it as deleted
            postRepository.save(post);
            response.setStatusCode(200);
            response.setMessage("Post deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getPostById(Long postId) {
        Response response = new Response();
        try {
            Optional<Post> postOpt = postRepository.findById(postId);
            if (postOpt.isEmpty()) {
                throw new OurException("Post not found with ID: " + postId);
            }
            Post post = postOpt.get();
            PostDTO dto = Converter.convertPostToDTO(post);
            response.setStatusCode(200);
            response.setMessage("Post retrieved successfully");
            response.setPost(dto);
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllPosts() {
        Response response = new Response();
        try {
            List<Post> posts = postRepository.findAll();
            List<PostDTO> postDTOs = posts.stream()
                    .map(Converter::convertPostToDTO)
                    .toList();
            response.setPosts(postDTOs);
            response.setStatusCode(200);
            response.setMessage("Posts retrieved successfully");

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal server error: " + e.getMessage());
        }
        return response;
    }

}
