package com.promotion.handwriting.entity;


import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
public class BasisEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "create_time", nullable = false)
    @CreatedDate
    private LocalDateTime createTime;
    @Column(name = "modify_time", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifyTime;

    public BasisEntity() {
        this.createTime = LocalDateTime.now();
        this.modifyTime = this.createTime;
    }
}
