package com.promotion.handwriting.entity;

import com.promotion.handwriting.dto.file.FileToken;
import com.promotion.handwriting.dto.file.LocalFileToken;
import com.promotion.handwriting.repository.file.FileRepository;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;

@Entity
@NoArgsConstructor
public class Image extends BasisEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTENTS")
    private Ad ad;
    @Column(name = "PRIORITY")
    private int priority;
    @Column(name = "IMAGE_NAME")
    private String imageName;
    @Column(name = "COMPRESS_IMAGE_NAME")
    private String compressImageName;

    @Builder
    private Image(Ad content, int priority, String imageName, String compressImageName) {
        this.ad = content;
        this.priority = priority;
        this.imageName = imageName;
        this.compressImageName = compressImageName;
    }

    public int getPriority() {
        return priority;
    }

    public String getImageName() {
        return imageName;
    }

    public String getCompressImageName() {
        return compressImageName;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setCompressImageName(String compressImageName) {
        this.compressImageName = compressImageName;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Image save(String path, MultipartFile file, FileRepository fileRepository) throws IOException {
        FileToken fileToken = LocalFileToken.save(file.getInputStream(), path, imageName);
        fileRepository.save(fileToken);
        fileRepository.compressAndSave(imageName, compressImageName, path);
        return this;
    }

    public void delete(FileRepository fileRepository, String resourcePath) {
        fileRepository.delete(LocalFileToken.delete(resourcePath, imageName));
        fileRepository.delete(LocalFileToken.delete(resourcePath, compressImageName));
    }

    public String getCompressImageUrl(String imageUrl) {
        if (compressImageName == null) return getImageUrl(imageUrl);
        return imageUrl + ad.getResourcePath() + "/" + getCompressImageName();
    }

    public String getImageUrl(String imageUrl) {
        return imageUrl + ad.getResourcePath() + "/" + getImageName();
    }

}
