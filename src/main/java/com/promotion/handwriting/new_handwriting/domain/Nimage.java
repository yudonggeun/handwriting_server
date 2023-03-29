package com.promotion.handwriting.new_handwriting.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Nimage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "ORIGIN_NAME")
    private String originName;
    @Column(name = "COMPRESS_NAME")
    private String compressName;
    @Column(name = "PATH")
    private String path;

    public Long getId() {
        return id;
    }

    private String contentId;

    public Nimage(String originName, String compressName, String path, String contentId) {
        this.originName = originName;
        this.compressName = compressName;
        this.path = path;
        this.contentId = contentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nimage image = (Nimage) o;
        return Objects.equals(originName, image.originName) && Objects.equals(compressName, image.compressName) && Objects.equals(path, image.path) && Objects.equals(contentId, image.contentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(originName, compressName, path, contentId);
    }
}