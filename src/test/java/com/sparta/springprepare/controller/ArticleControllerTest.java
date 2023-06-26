package com.sparta.springprepare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.springprepare.domain.dto.ArticleDto.RequestArticleDto;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("게시물 생성 테스트")
    void createArticleTest() throws Exception {
        RequestArticleDto requestArticleDto = RequestArticleDto.builder()
                .title("title")
                .author("author")
                .content("content")
                .password("password")
                .build();

        mockMvc.perform(post("/api/articles")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestArticleDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("게시물 단건 조회 테스트")
    void getArticleTest() throws Exception {
        //given
        createAndPostArticle(1);

        //when, then
        mockMvc.perform(get("/api/articles/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.update-article").exists())
                .andExpect(jsonPath("_links.delete-article").exists())
                .andExpect(jsonPath("data").exists());
    }

    @Test
    @DisplayName("게시물 전체 조회 테스트")
    void getArticlesTest() throws Exception {
        //given
        createAndPostArticle(5);

        //when, then
        mockMvc.perform(get("/api/articles"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("data", hasSize(5)));
    }

    private void createAndPostArticle(int numberOfIterations) throws Exception {
        for (int i = 1; i <= numberOfIterations; i++) {
            RequestArticleDto requestArticleDto = RequestArticleDto.builder()
                    .title("title")
                    .author("author")
                    .content("content")
                    .password("password")
                    .build();

            mockMvc.perform(post("/api/articles")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestArticleDto)));
        }
    }

    @Test
    @DisplayName("게시물 수정 테스트")
    void updateArticleTest() throws Exception {
        //given
        createAndPostArticle(1);
        String updateTitle = "update title";
        String updateContent = "update content";
        RequestArticleDto updateDto = RequestArticleDto.builder()
                .id(1L)
                .title(updateTitle)
                .password("password")
                .content(updateContent)
                .build();

        //when, then
        mockMvc.perform(put("/api/articles/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, startsWith(HAL_JSON_VALUE)))
                .andExpect(jsonPath("data[0].title").value(updateTitle))
                .andExpect(jsonPath("data[0].content").value(updateContent));
    }

    @Test
    @DisplayName("게시물 삭제 테스트")
    void deleteArticleTest() throws Exception {
        //given
        createAndPostArticle(2);
        RequestArticleDto requestArticleDto = RequestArticleDto.builder()
                .id(1L)
                .password("password")
                .build();

        //when, then
        mockMvc.perform(delete("/api/articles/1")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestArticleDto)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

}