package com.sparta.springprepare.domain.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

public class ArticleDto {

    @Getter @Builder
    @NoArgsConstructor(access = PROTECTED) @AllArgsConstructor(access = PROTECTED)
    public static class RequestArticleDto {

        @NotEmpty private Long id;
        @NotEmpty private String title;
        @NotEmpty private String author;
        @NotEmpty @Setter
        private String password;
        @NotEmpty private String content;


    }

    @Getter @Builder
    @NoArgsConstructor(access = PROTECTED) @AllArgsConstructor(access = PROTECTED)
    public static class ResponseArticleDto {

        private Long id;
        private String title;
        private String author;
        private String content;
        private LocalDateTime createdAt;

    }

}
