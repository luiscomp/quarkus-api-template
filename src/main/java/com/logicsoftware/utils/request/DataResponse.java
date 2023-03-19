package com.logicsoftware.utils.request;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.logicsoftware.utils.enums.AppStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Dto to represent default date response of server")
public class DataResponse<T> {
    @Schema(description = "Status from server for request called", example = "success")
    private AppStatus status;
    @Schema(description = "Data response of request called")
    private T data;
}
