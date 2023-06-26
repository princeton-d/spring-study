package com.sparta.springprepare.domain.entity;

import com.sparta.springprepare.domain.dto.ArticleDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
public class Article {

    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String author;
    private String password;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = this.createdAt;
    }

    @PreUpdate
    private void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    public Article update(ArticleDto.RequestArticleDto updateDto) {
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
        this.modifiedAt = LocalDateTime.now();

        return this;
    }

    public void encodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
