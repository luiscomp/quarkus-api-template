package com.logicsoftware.utils.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Data
@Builder(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Dto to represent default page response of server")
public class PageResponse<T> {
    @Schema(description = "Page response from server for request called")
    private List<T> page;
    @Schema(description = "Total of elements in database", example = "50")
    private Long totalElements;
    @Schema(description = "Total of pages of size passed in request", example = "5")
    @Getter(AccessLevel.NONE)
    private Integer totalPages;
    @Schema(description = "Total of elements per page", example = "10")
    private Integer pageSize;

    public Long getTotalPages() {
        return (long) Math.ceil((double) totalElements / pageSize);
    }
}
