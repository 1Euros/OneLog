package com.oneeuros.onelog.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long> {
    boolean existsByName(String name);

    @Query("select distinct b from Board b left join fetch b.postlist where b.id = :id")
    Optional<Board> findByIdWithPosts(@Param("id") Long id);

}
