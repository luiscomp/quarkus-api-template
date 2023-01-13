package com.logicsoftware.dtos.user;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dto the represents a User Filter")
public class UserFilterDto {
    @Schema(description = "User Name", example = "Luis Eduardo M. Ferreira", required = true)
    private String name;
    @Schema(description = "User E-mail", example = "luizeduardo354@gmail.com", required = true)
    private String email;
}
