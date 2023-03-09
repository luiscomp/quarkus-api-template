package com.logicsoftware.dtos.user;

import javax.validation.constraints.NotEmpty;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dto representation of a User to create")
public class UserCreateDto {
    @NotEmpty(message = "Name is required")
    @Schema(description = "User Name", example = "Luis Eduardo M. Ferreira", required = true)
    private String name;

    @NotEmpty(message = "E-mail is required")
    @Schema(description = "User E-mail", example = "luizeduardo354@gmail.com", required = true)
    private String email;
}
