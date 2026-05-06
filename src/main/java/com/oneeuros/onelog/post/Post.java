package com.oneeuros.onelog.post;
/*
    Post Entity
 */

import com.oneeuros.onelog.board.Board;
import com.oneeuros.onelog.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@NoArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false, length = 80)
    private String password;

    @Column(nullable = false)
    private int viewCount = 0;

    //게시판
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id", nullable = false)
    private Board board;


    public Post(String title, String content, String nickname, String password, Board board) {
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.password = password;
        this.board = board;
    }

    public void increaseViewCount(){
        this.viewCount++;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}
