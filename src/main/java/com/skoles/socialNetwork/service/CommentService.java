package com.skoles.socialNetwork.service;

import com.skoles.socialNetwork.dto.CommentDTO;
import com.skoles.socialNetwork.entity.Comment;
import com.skoles.socialNetwork.entity.Post;
import com.skoles.socialNetwork.entity.User;
import com.skoles.socialNetwork.exceptions.NotFoundException;
import com.skoles.socialNetwork.repository.CommentRepository;
import com.skoles.socialNetwork.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    public static final Logger LOG = LoggerFactory.getLogger(CommentService.class);
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public Comment saveComment(Long id, CommentDTO commentDTO, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post cannot be found for username: " + user.getEmail()));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDTO.getMessage());
        LOG.info("Saving comment for Post: {}", post.getId());
        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Post cannot be found"));
        return commentRepository.findAllByPost(post);
    }

    public void deleteComment(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        comment.ifPresent(commentRepository::delete);
    }
}
