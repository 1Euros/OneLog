package com.oneeuros.onelog.comment;

import com.oneeuros.onelog.common.BaseEntity;
import com.oneeuros.onelog.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments",indexes = {    //groupId와 grouptOrder로 인덱스 걸기
        @Index(
                name = "idx_group_order",
                columnList = "group_id, group_order"
        )
})
@Getter
@NoArgsConstructor
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length =30)
    private String nickname;

    @Column(nullable = false, length = 80)
    private String password;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;    //부모 댓글 아이디

    @Column(nullable = false)
    private Integer depth;    // 댓글 깊이

    @Column(nullable = false)
    private Long groupId;      // 정렬을 위한 최상위 댓글 아이디

    @Column(nullable = false)
    private Integer groupOrder;   // 최상위 댓글 기준 정렬 순서


    public Comment (String nickname, String password, String content, Post post, Comment parent, int depth,Long groupId,  int groupOrder){
        this.nickname = nickname;
        this.password = password;
        this.content = content;
        this.post = post;
        this.parent = parent;
        this.depth = depth;
        this.groupId = groupId;
        this.groupOrder = groupOrder;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void pushGroupOrder() {
        this.groupOrder = groupOrder+1;
    }

    public void registerGroupId(Long groupId) {
        this.groupId = groupId;
    }

}
