package com.promotion.handwriting.repository.database;

import com.promotion.handwriting.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    @Modifying(clearAutomatically = true)
    void deleteAllByAdId(@Param("ad_id") long adId);

    List<Image> findByAdId(long ad);
}
