package com.promotion.handwriting.new_handwriting.repository;

import com.promotion.handwriting.new_handwriting.domain.Nimage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepo extends JpaRepository<Nimage, Long> {
}
