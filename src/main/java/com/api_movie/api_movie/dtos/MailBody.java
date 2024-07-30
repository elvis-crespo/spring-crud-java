package com.api_movie.api_movie.dtos;

import lombok.Builder;

@Builder
public record MailBody(String to, String subject, String text) {

}
