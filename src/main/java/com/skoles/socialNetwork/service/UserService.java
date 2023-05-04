package com.skoles.socialNetwork.service;

import com.skoles.socialNetwork.dto.UserDTO;
import com.skoles.socialNetwork.entity.User;
import com.skoles.socialNetwork.entity.enums.ERole;
import com.skoles.socialNetwork.exceptions.NotFoundException;
import com.skoles.socialNetwork.payload.request.SignUpRequest;
import com.skoles.socialNetwork.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class UserService {
    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(SignUpRequest signUp) {
        User user = new User();
        user.setEmail(signUp.getEmail());
        user.setName(signUp.getFirstname());
        user.setLastname(signUp.getLastname());
        user.setUsername(signUp.getUsername());
        user.setPassword(passwordEncoder.encode(signUp.getPassword()));
        user.getRoles().add(ERole.ROLE_USER);

        try {
            LOG.info("Saving User {}", signUp.getEmail());
            userRepository.save(user);
        } catch (Exception ex) {
            LOG.error("Error registration. {}", ex.getMessage());
            throw new NotFoundException("The user " + user.getUsername() + " already exist.");
        }
    }

    public User updateUser(UserDTO userDTO, Principal principal) {
        User user = getUserByPrincipal(principal);
        user.setName(userDTO.getName());
        user.setLastname(userDTO.getLastname());
        user.setBio(userDTO.getBio());
        return userRepository.save(user);
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    protected User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username " + username));
    }

    public User getUserById(Long id) {
        return userRepository.findUserById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
