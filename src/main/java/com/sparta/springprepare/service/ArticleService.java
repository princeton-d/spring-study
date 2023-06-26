package com.sparta.springprepare.service;

import com.sparta.springprepare.domain.entity.Article;
import com.sparta.springprepare.exception.InvalidPasswordException;
import com.sparta.springprepare.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.sparta.springprepare.domain.dto.ArticleDto.RequestArticleDto;
import static com.sparta.springprepare.domain.dto.ArticleDto.ResponseArticleDto;
import static com.sparta.springprepare.util.mapper.ArticleMapper.INSTANCE;

@Service
@Transactional
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final PasswordEncoder passwordEncoder;

    public Long addArticle(RequestArticleDto articleDto) {
        Article article = INSTANCE.articleDtoToEntity(articleDto);
        String encodedPassword = passwordEncoder.encode(articleDto.getPassword());
        article.encodedPassword(encodedPassword);

        Article savedArticle = articleRepository.save(article);
        return savedArticle.getId();
    }

    public List<ResponseArticleDto> getArticles() {
        List<Article> findArticles = articleRepository.findAllByOrderByCreatedAtDesc();

        return findArticles.stream()
                .map(INSTANCE::entityToArticleDto)
                .toList();
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 게시물은 없는 게시물입니다."));
    }

    public List<ResponseArticleDto> getArticleResponseDtoById(Long id) {
        List<ResponseArticleDto> responseArticleDtos = new ArrayList<>();
        Article findArticle = this.getArticleById(id);
        ResponseArticleDto responseArticleDto = INSTANCE.entityToArticleDto(findArticle);
        responseArticleDtos.add(responseArticleDto);

        return responseArticleDtos;
    }

    public void updateArticle(RequestArticleDto articleDto) throws InvalidPasswordException {
        Article findArticle = this.getArticleById(articleDto.getId());
        String articlePassword = findArticle.getPassword();
        CharSequence dtoPassword = articleDto.getPassword();

        if (isValidatePassword(dtoPassword, articlePassword)) {
            findArticle.update(articleDto);
            return;
        }

        throw new InvalidPasswordException("잘못된 비밀번호입니다.");
    }

    public void deleteArticle(RequestArticleDto articleDto) {
        Article findArticle = this.getArticleById(articleDto.getId());

        if (isValidatePassword(articleDto.getPassword(), findArticle.getPassword())) {
            articleRepository.delete(findArticle);
            return;
        }

        throw new InvalidPasswordException("잘못된 비밀번호입니다.");
    }

    private boolean isValidatePassword(CharSequence password, String encodePassword) {
        return passwordEncoder.matches(password, encodePassword);
    }
}
