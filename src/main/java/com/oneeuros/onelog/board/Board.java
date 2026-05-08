package com.oneeuros.onelog.board;

import com.oneeuros.onelog.common.BaseEntity;
import com.oneeuros.onelog.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "boards")
@Getter
@NoArgsConstructor
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, length =30, unique = true)
    private String name;

    @OneToMany(mappedBy = "board")
    private List<Post> postlist;

    public Board(String name) {
        this.name = name;
    }

    public void updateName(String name) {
        this.name = name;
    }
}
