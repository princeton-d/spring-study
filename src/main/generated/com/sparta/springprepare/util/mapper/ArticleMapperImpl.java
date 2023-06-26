package com.sparta.springprepare.util.mapper;

import com.sparta.springprepare.domain.dto.ArticleDto;
import com.sparta.springprepare.domain.entity.Article;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-06-26T15:32:53+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.7 (Oracle Corporation)"
)
public class ArticleMapperImpl implements ArticleMapper {

    @Override
    public Article articleDtoToEntity(ArticleDto.RequestArticleDto dto) {
        if ( dto == null ) {
            return null;
        }

        Article.ArticleBuilder article = Article.builder();

        article.id( dto.getId() );
        article.title( dto.getTitle() );
        article.content( dto.getContent() );
        article.author( dto.getAuthor() );
        article.password( dto.getPassword() );

        return article.build();
    }

    @Override
    public ArticleDto.ResponseArticleDto entityToArticleDto(Article article) {
        if ( article == null ) {
            return null;
        }

        ArticleDto.ResponseArticleDto.ResponseArticleDtoBuilder responseArticleDto = ArticleDto.ResponseArticleDto.builder();

        responseArticleDto.id( article.getId() );
        responseArticleDto.title( article.getTitle() );
        responseArticleDto.author( article.getAuthor() );
        responseArticleDto.content( article.getContent() );
        responseArticleDto.createdAt( article.getCreatedAt() );

        return responseArticleDto.build();
    }
}
