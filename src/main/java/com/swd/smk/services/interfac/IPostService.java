package com.swd.smk.services.interfac;

import com.swd.smk.dto.PostDTO;
import com.swd.smk.dto.Response;

public interface IPostService {
    Response createPost(PostDTO postDTO);
    Response updatePost(Long postId, PostDTO postDTO);
    Response deletePost(Long postId);
    Response getPostById(Long postId);
    Response getAllPosts();
    Response getPostsByMemberId(Long memberId);
}
