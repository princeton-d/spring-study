package com.sparta.springprepare.util.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

import static com.sparta.springprepare.domain.dto.ArticleDto.ResponseArticleDto;

@Getter
@AllArgsConstructor
public class ArticleResource extends RepresentationModel<ArticleResource> {

    @JsonProperty("data")
    private List<ResponseArticleDto> dtos;

}
