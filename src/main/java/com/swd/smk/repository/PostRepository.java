package com.swd.smk.repository;

import com.swd.smk.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    // Additional query methods can be defined here if needed
}
