package com.skoles.socialNetwork.repository;

import com.skoles.socialNetwork.entity.Comment;
import com.skoles.socialNetwork.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);

    Comment findByIdAndUserId(Long commentId, Long userId);
}
