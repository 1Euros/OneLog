package com.oneeuros.onelog.board;

import com.oneeuros.onelog.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public Board(String name) {
        this.name = name;
        // 앞 뒤 공백 방지
    }
}
