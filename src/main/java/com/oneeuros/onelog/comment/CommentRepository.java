package com.oneeuros.onelog.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostIdOrderByCreatedAtDesc(Long postId);

    Optional<Comment> findFirstByPostIdOrderByCreatedAtDesc(Long postId);

    int countByPostId(Long postId);

    //해당 부모의 서브트리가 끝나고 처음 지점의 groupOrder
    @Query("""
    select min(c.groupOrder)
    from Comment c
    where c.groupId = :groupId
    and c.groupOrder > :parentOrder
    and c.depth <= :parentDepth
    """)
    Integer findSubtreeBoundary(Long groupId,Integer parentOrder,Integer parentDepth);

    @Query("""
    select coalesce(max(c.groupOrder), 0)
    from Comment c
    where c.groupId = :groupId
""")
    int findMaxOrder(Long groupId);

    List<Comment> findByGroupIdAndGroupOrderGreaterThanEqual(Long groupId, Integer groupOrder);
}
