package com.promotion.handwriting.entity;

import com.promotion.handwriting.dto.ContentDto;
import com.promotion.handwriting.dto.ImageUrlDto;
import com.promotion.handwriting.enums.AdType;
import com.promotion.handwriting.repository.file.FileRepository;
import com.promotion.handwriting.util.ImageUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Slf4j
@NoArgsConstructor
@Table(name = "CONTENTS")
public class Ad extends BasisEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "AD_TYPE")
    private AdType type;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "DETAIL")
    private String detail;
    @OneToMany(mappedBy = "ad", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @Builder
    private Ad(AdType type, String title, String detail) {
        super();
        this.type = type;
        this.title = title;
        this.detail = detail;
        this.images = new ArrayList<>();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ContentDto contentDto() {
        List<ImageUrlDto> dtoImage = getImages().stream()
                .map(i -> i.urlDto())
                .collect(Collectors.toList());

        var dto = new ContentDto();
        dto.setId(getId() + "");
        dto.setTitle(title);
        dto.setDescription(detail);
        dto.setImages(dtoImage);
        return dto;
    }

    public Image addImage(MultipartFile file, FileRepository fileRepository) {
        var originalFilename = file.getOriginalFilename();
        var compressFilename = ImageUtil.compressImageName(originalFilename);

        Image image = Image.builder()
                .content(this)
                .priority(Integer.MAX_VALUE)
                .imageName(originalFilename)
                .compressImageName(compressFilename)
                .build();

        fileRepository.save(image, file);
        images.add(image);
        return image;
    }
}
