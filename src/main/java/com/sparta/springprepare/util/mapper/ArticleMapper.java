package com.sparta.springprepare.util.mapper;

import com.sparta.springprepare.domain.entity.Article;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static com.sparta.springprepare.domain.dto.ArticleDto.*;
import static com.sparta.springprepare.domain.dto.ArticleDto.RequestArticleDto;

@Mapper
public interface ArticleMapper {

    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    Article articleDtoToEntity(RequestArticleDto dto);

    ResponseArticleDto entityToArticleDto(Article article);

}