package com.promotion.handwriting.new_handwriting.repository;

import com.promotion.handwriting.dto.ContentResponse;
import com.promotion.handwriting.dto.image.ImageResponse;
import com.promotion.handwriting.new_handwriting.domain.Content;
import com.promotion.handwriting.new_handwriting.domain.Nimage;
import com.promotion.handwriting.new_handwriting.dto.SearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ContentResponseRepository {

    private final EntityManager em;

    public Page<ContentResponse> findContent(Pageable pageable, SearchCondition condition) {

        List<ContentResponse> content = em.createQuery("select c from Content c", Content.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList()
                .stream()
                .map(c -> {
                    ContentResponse r = new ContentResponse();
                    r.setDescription(c.getDescription());
                    r.setTitle(c.getTitle());
                    r.setImages(c.getImages().stream().map(i -> new ImageResponse(i.originPath(), i.compressPath())).collect(Collectors.toList()));
                    r.setId(c.getId());
                    return r;
                }).collect(Collectors.toList());

        long total = em.createQuery("select COUNT(c) from Content c", Long.class).getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }

    public Page<ImageResponse> findImage(String contentId, Pageable pageable) {
        List<ImageResponse> content = em.createQuery("select i from Nimage i where i.contentId = :contentId", Nimage.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .setParameter("contentId", contentId)
                .getResultList()
                .stream().map(i -> new ImageResponse(i.originPath(), i.compressPath()))
                .collect(Collectors.toList());

        long count = em.createQuery("select COUNT(i) from Nimage i where i.contentId = :contentId", Long.class)
                .setParameter("contentId", contentId)
                .getSingleResult();

        return new PageImpl<>(content, pageable, count);
    }
}
