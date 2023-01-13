package com.logicsoftware.utils.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Map;

@Data
@Builder(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Dto to represent default date response of server")
public class DataResponse<T> {
    @Schema(description = "Message from server for request called", example = "success")
    private String message;
    @Schema(description = "Data response of request called")
    private T data;
    @Schema(description = "List of errors from server of request called", example = "[\"error_1\", \"error_2\"]")
    private Map<Object, Object> errors;
}
