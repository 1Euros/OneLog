package com.oneeuros.onelog.post;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 닉네임 필수값 검증

    // 비밀번호 필수값

}
