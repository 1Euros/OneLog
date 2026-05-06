package com.oneeuros.onelog.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 전체 게시글 목록 최신순 조회 및 페이징
    Page<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 특정 게시글에 해당되는 특정 게시판 가져옴 [fetch join]
    @Query(
            value = "select p from Post p join fetch p.board order by p.createdAt desc",
            countQuery = "select count(p) from Post p"
    )
    Page<Post> findAllWithBoardOrderByCreatedAtDesc(Pageable pageable);


}
