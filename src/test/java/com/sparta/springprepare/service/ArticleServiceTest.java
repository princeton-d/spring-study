package com.sparta.springprepare.service;

import com.sparta.springprepare.domain.entity.Article;
import com.sparta.springprepare.exception.InvalidPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.springprepare.domain.dto.ArticleDto.RequestArticleDto;
import static com.sparta.springprepare.domain.dto.ArticleDto.ResponseArticleDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private List<RequestArticleDto> dtoList = new ArrayList<>();

    @BeforeEach
    void before() {
        for (int i = 1; i <= 3; i++) {
            dtoList.add(RequestArticleDto.builder()
                    .title("title" + i)
                    .author("author" + i)
                    .content("content" + i)
                    .password("password" + i)
                    .build());
        }
    }

    @Test
    @DisplayName("게시물 저장 테스트")
    void addArticleTest() {
        //given
        RequestArticleDto requestDto = dtoList.get(0);

        //when
        Long savedId = articleService.addArticle(requestDto);
        Article article = articleService.getArticleById(savedId);

        //then
        assertThat(article.getAuthor()).isEqualTo(requestDto.getAuthor());
    }

    @Test
    @DisplayName("게시물 단건 조회 테스트")
    void getArticleById() {
        //given
        RequestArticleDto articleCreateDto = dtoList.get(0);
        Long savedId = articleService.addArticle(articleCreateDto);

        //when
        List<ResponseArticleDto> findArticleDtos = articleService.getArticleResponseDtoById(savedId);

        //then
        assertThat(findArticleDtos.size()).isEqualTo(1);
        assertThat(findArticleDtos.get(0).getId()).isEqualTo(savedId);
    }

    @Test
    @DisplayName("없는 아이디로 단건 조회 테스트")
    void getArticleWrongIdTest() {
        //when, then
        assertThatThrownBy(() -> articleService.getArticleResponseDtoById(134756L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 게시물은 없는 게시물입니다.");
    }

    @Test
    @DisplayName("모든 게시물 조회 테스트")
    void getAllArticlesTest() {
        //given
        for (RequestArticleDto dto : dtoList) {
            articleService.addArticle(dto);
        }

        //when
        List<ResponseArticleDto> findArticleDtos = articleService.getArticles();

        //then
        assertThat(findArticleDtos.size()).isEqualTo(dtoList.size());
    }

    @Test
    @DisplayName("비밀번호 인코딩 테스트")
    void encodeTest() {
        //given
        String rowPassword = "1234";

        //when
        String encodedPassword = passwordEncoder.encode(rowPassword);

        //then
        assertThat(encodedPassword).isNotEqualTo(rowPassword);
        assertThat(passwordEncoder.matches(rowPassword, encodedPassword)).isTrue();
    }

    @Test
    @DisplayName("게시물 수정 테스트")
    void updateArticle() {
        //given
        RequestArticleDto articleDto = dtoList.get(0);
        Long targetArticleId = articleService.addArticle(articleDto);
        Article targetArticle = articleService.getArticleById(targetArticleId);

        String updateTitle = "update title";
        String updateContent = "update content";

        RequestArticleDto updateDto = RequestArticleDto.builder()
                .id(targetArticleId)
                .title(updateTitle)
                .content(updateContent)
                .password("password1")
                .build();

        //when
        articleService.updateArticle(updateDto);

        //then
        assertThat(targetArticle.getTitle()).isEqualTo(updateTitle);
        assertThat(targetArticle.getContent()).isEqualTo(updateContent);
    }

    @Test
    @DisplayName("잘못된 비밀번호로 게시물 수정 테스트")
    void wrongPasswordUpdateArticleTest() {
        //given
        RequestArticleDto articleDto = dtoList.get(0);
        Long targetArticleId = articleService.addArticle(articleDto);

        String updateTitle = "update title";
        String updateContent = "update content";
        String wrongPassword = "wrong password";

        RequestArticleDto updateDto = RequestArticleDto.builder()
                .id(targetArticleId)
                .title(updateTitle)
                .content(updateContent)
                .password(wrongPassword)
                .build();

        //then
        assertThatThrownBy(() -> articleService.updateArticle(updateDto))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessage("잘못된 비밀번호입니다.");
    }

    @Test
    @DisplayName("게시물 삭제 테스트")
    void deleteArticleTest() {
        //given
        RequestArticleDto articleDto = dtoList.get(0);
        Long savedId = articleService.addArticle(articleDto);

        RequestArticleDto deleteDto = RequestArticleDto.builder()
                .id(savedId)
                .password("password1")
                .build();

        //when
        articleService.deleteArticle(deleteDto);

        //then
        assertThatThrownBy(() -> articleService.getArticleById(savedId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 게시물은 없는 게시물입니다.");
    }

    @Test
    @DisplayName("잘못된 비밀번호로 게시물 삭제 테스트")
    void wrongPasswordDeleteArticleTest() {
        //given
        RequestArticleDto articleDto = dtoList.get(0);
        Long savedId = articleService.addArticle(articleDto);

        RequestArticleDto deleteDto = RequestArticleDto.builder()
                .id(savedId)
                .password("wrong password")
                .build();

        //when, then
        assertThatThrownBy(() -> articleService.deleteArticle(deleteDto))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessage("잘못된 비밀번호입니다.");
    }

}