package com.oneeuros.onelog.comment;

import com.oneeuros.onelog.common.BaseEntity;
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
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length =30)
    private String nickname;

    @Column(nullable = false, length = 80)
    private String password;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long postId =1L;  //임의의 부모테이블 아이디


    public Comment (String nickname, String password, String content){
        this.nickname = nickname;
        this.password = password;
        this.content = content;
    }
}
