package com.skoles.socialNetwork.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastname;
    private String username;
    private String bio;

}
