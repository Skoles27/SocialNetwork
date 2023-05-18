package com.skoles.socialNetwork.service;

import com.skoles.socialNetwork.dto.PostDTO;
import com.skoles.socialNetwork.entity.ImageModel;
import com.skoles.socialNetwork.entity.Post;
import com.skoles.socialNetwork.entity.User;
import com.skoles.socialNetwork.exceptions.NotFoundException;
import com.skoles.socialNetwork.repository.ImageRepository;
import com.skoles.socialNetwork.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    public static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserService userService;

    @Autowired
    public PostService(PostRepository postRepository, ImageRepository imageRepository, UserService userService) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
        this.userService = userService;
    }

    public Post createPost(PostDTO postDTO, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        Post post = new Post();
        post.setUser(user);
        post.setCaption(postDTO.getCaption());
        post.setLocation(postDTO.getLocation());
        post.setTitle(postDTO.getTitle());
        post.setLikes(0);

        LOG.info("Saving Post for User: {}", user.getEmail());
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedDateDesc();
    }

    public Post getPostById(Long id, Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        return postRepository.findPostByIdAndUser(id, user)
                .orElseThrow(() -> new NotFoundException("Post cannot be found for username: " + user.getEmail()));
    }

    public List<Post> getAllPostForUser(Principal principal) {
        User user = userService.getUserByPrincipal(principal);
        return postRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public List<Post> getAllPostForOtherUser(String userId) {
        User user = userService.getUserById(Long.valueOf(userId));
        return postRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public Post likePost(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("post cannot be found"));

        Optional<String> userLiked = post.getLikedUsers().stream().filter(u -> u.equals(username)).findAny();

        if (userLiked.isPresent()) {
            post.setLikes(post.getLikes() - 1);
            post.getLikedUsers().remove(username);
        } else {
            post.setLikes(post.getLikes() + 1);
            post.getLikedUsers().add(username);
        }
        return postRepository.save(post);
    }

    public Post updatePost(PostDTO postDTO, String postId, Principal principal) {
        Post post = getPostById(Long.valueOf(postId), principal);
        post.setTitle(postDTO.getTitle());
        post.setLocation(postDTO.getLocation());
        post.setCaption(postDTO.getCaption());
        return postRepository.save(post);
    }

    public void deletePost(Long id, Principal principal) {
        Post post = getPostById(id, principal);
        Optional<ImageModel> imageModel = imageRepository.findByPostId(post.getId());
        postRepository.delete(post);
        imageModel.ifPresent(imageRepository::delete);
    }
}
