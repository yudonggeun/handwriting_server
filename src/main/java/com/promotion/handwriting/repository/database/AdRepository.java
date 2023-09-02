package com.promotion.handwriting.repository.database;

import com.promotion.handwriting.entity.Ad;
import com.promotion.handwriting.enums.AdType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {

    @EntityGraph(attributePaths = {"images"})
    Page<Ad> findByType(AdType type, Pageable pageable);

    @EntityGraph(attributePaths = {"images"})
    Ad findWithImageById(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    void deleteById(long id);

    @EntityGraph(attributePaths = {"images"})
    Ad findByType(AdType type);
}
