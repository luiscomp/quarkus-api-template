package com.logicsoftware.dtos.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Dto the represents a User")
public class UserDto {
    @Schema(description = "User Identify", example = "1")
    private Integer id;
    @Schema(description = "User Name", example = "Luis Eduardo M. Ferreira")
    private String name;
    @Schema(description = "User E-mail", example = "luizeduardo354@gmail.com")
    private String email;
}
