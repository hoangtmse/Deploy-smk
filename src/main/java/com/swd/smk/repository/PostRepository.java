package com.swd.smk.repository;

import com.swd.smk.enums.Status;
import com.swd.smk.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Additional query methods can be defined here if needed
    List<Post> findByMemberId(Long memberId);

    List<Post> findByMemberIdAndStatus(Long memberId, Status status);
}
