package com.oneeuros.onelog.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId);

    Optional<Comment> findFirstByPostIdOrderByCreatedAtDesc(Long postId);

    int countByPostId(Long postId);
}
