package com.promotion.handwriting.repository.database;

import com.promotion.handwriting.entity.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface JpaImageRepository extends JpaRepository<Image, Long> {

    @Modifying(clearAutomatically = true)
    void deleteAllByAdId(long adId);

    Page<Image> findByAdId(long ad, Pageable pageable);
    List<Image> findByAdId(long ad);

    List<Image> findByIdIn(List<Long> images);

    void deleteAllByIdIn(List<Long> images);
}
