package com.api_movie.api_movie.dtos;

import java.util.List;

public record MoviePageResponse(List<MovieDTO> movieDTOs, Integer pageNumber,
                                Integer pageSize, Long totalElements,
                                int totalPages, boolean isLast) {
}
