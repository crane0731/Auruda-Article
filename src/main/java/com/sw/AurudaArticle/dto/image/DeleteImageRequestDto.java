package com.sw.AurudaArticle.dto.image;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
public class DeleteImageRequestDto {
    private List<String> imageUrls;
}
