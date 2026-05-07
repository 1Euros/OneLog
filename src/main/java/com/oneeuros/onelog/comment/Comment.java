package com.oneeuros.onelog.comment;

import com.oneeuros.onelog.common.BaseEntity;
import com.oneeuros.onelog.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
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
    private Comment parent;

    @Column(nullable = false)
    private Integer depth;

//    private int orderNumber;


    public Comment (String nickname, String password, String content, Post post, Comment parent, int depth){
        this.nickname = nickname;
        this.password = password;
        this.content = content;
        this.post = post;
        this.parent = parent;
        this.depth = depth;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
