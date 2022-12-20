package com.promotion.handwriting.repository;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {

    @Query("select a from Ad a where a.type = :type")
    List<Ad> selectType(@Param("type") AdType type);
}
