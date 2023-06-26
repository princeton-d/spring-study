package com.sparta.springprepare.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTest {

    @Test
    @DisplayName("엔티티 생성 테스트")
    public void createArticleEntity() {
        //given, when
        Article article = Article.builder().build();

        //then
        assertThat(article).isNotNull();
    }

}