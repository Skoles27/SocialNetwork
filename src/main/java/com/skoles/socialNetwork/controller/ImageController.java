package com.skoles.socialNetwork.controller;

import com.skoles.socialNetwork.entity.ImageModel;
import com.skoles.socialNetwork.payload.response.MessageResponse;
import com.skoles.socialNetwork.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("api/image")
@CrossOrigin
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadImageForUser(@RequestParam("file") MultipartFile file,
                                                              Principal principal) throws IOException {
        imageService.uploadImageForUser(file, principal);
        return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));
    }

    @PostMapping("/{postId}/upload")
    public ResponseEntity<MessageResponse> uploadImageForPost(@PathVariable("postId") String postId,
                                                              @RequestParam("file") MultipartFile file,
                                                              Principal principal) throws IOException {
        imageService.uploadImageForPost(file, principal, Long.parseLong(postId));
        return ResponseEntity.ok(new MessageResponse("Image uploaded successfully"));
    }

    @GetMapping("/profileImage")
    public ResponseEntity<ImageModel> getImageForUser(Principal principal) {
        ImageModel imageModel = imageService.getImageForUser(principal);
        return new ResponseEntity<>(imageModel, HttpStatus.OK);
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<ImageModel> getImageForPost(@PathVariable("postId") String postId) {
        ImageModel imageModel = imageService.getImageForPost(Long.parseLong(postId));
        return new ResponseEntity<>(imageModel, HttpStatus.OK);
    }

    @GetMapping("/{userId}/user/image")
    public ResponseEntity<ImageModel> getImageForOtherUser(@PathVariable("userId") String userId) {
        ImageModel imageModel = imageService.getImageForOtherUser(Long.parseLong(userId));
        return new ResponseEntity<>(imageModel, HttpStatus.OK);
    }
}
