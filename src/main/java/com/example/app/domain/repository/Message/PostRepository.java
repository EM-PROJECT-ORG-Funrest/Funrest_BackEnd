package com.example.app.domain.repository.Message;

import com.example.app.domain.entity.Message.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
