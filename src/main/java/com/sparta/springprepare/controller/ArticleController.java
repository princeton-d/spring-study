package com.sparta.springprepare.controller;

import com.sparta.springprepare.service.ArticleService;
import com.sparta.springprepare.util.resource.ArticleResource;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.sparta.springprepare.domain.dto.ArticleDto.RequestArticleDto;
import static com.sparta.springprepare.domain.dto.ArticleDto.ResponseArticleDto;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<ArticleResource> getArticles() {
        List<ResponseArticleDto> responseArticleDtos = articleService.getArticles();

        WebMvcLinkBuilder selfLinkBuilder = linkTo(ArticleController.class);

        ArticleResource articleResource = new ArticleResource(responseArticleDtos);
        articleResource.add(selfLinkBuilder.withSelfRel());

        return ResponseEntity.ok().body(articleResource);
    }

    @GetMapping("{id}")
    public ResponseEntity<ArticleResource> getArticle(@PathVariable Long id) {
        List<ResponseArticleDto> responseArticleDtos = articleService.getArticleResponseDtoById(id);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(ArticleController.class).slash(id);

        ArticleResource articleResource = createArticleResource(responseArticleDtos, selfLinkBuilder);


        return ResponseEntity.ok().body(articleResource);
    }

    @PostMapping
    public ResponseEntity<ArticleResource> createArticle(@RequestBody RequestArticleDto requestArticleDto) {
        Long savedId = articleService.addArticle(requestArticleDto);
        List<ResponseArticleDto> responseArticleDto = articleService.getArticleResponseDtoById(savedId);
        WebMvcLinkBuilder selfLinkBuilder = linkTo(ArticleController.class).slash(savedId);
        URI createUri = selfLinkBuilder.toUri();

        ArticleResource articleResource = createArticleResource(responseArticleDto, selfLinkBuilder);

        return ResponseEntity.created(createUri).body(articleResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResource> updateArticle(@RequestBody RequestArticleDto requestArticleDto) {
        articleService.updateArticle(requestArticleDto);
        List<ResponseArticleDto> responseArticleDtos = articleService.getArticleResponseDtoById(requestArticleDto.getId());
        WebMvcLinkBuilder selfLinkBuilder = linkTo(ArticleController.class).slash(requestArticleDto.getId());

        ArticleResource articleResource = createArticleResource(responseArticleDtos, selfLinkBuilder);

        return ResponseEntity.ok(articleResource);
    }

    private static ArticleResource createArticleResource(List<ResponseArticleDto> responseArticleDto, WebMvcLinkBuilder selfLinkBuilder) {
        ArticleResource articleResource = new ArticleResource(responseArticleDto);
        articleResource.add(selfLinkBuilder.withSelfRel());
        articleResource.add(selfLinkBuilder.withRel("update-article"));
        articleResource.add(selfLinkBuilder.withRel("delete-article"));
        return articleResource;
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteArticle(@RequestBody RequestArticleDto requestArticleDto) {
        articleService.deleteArticle(requestArticleDto);
        return ResponseEntity.ok().body("deleted");
    }

}
