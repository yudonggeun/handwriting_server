package com.promotion.handwriting.entity;


import lombok.Getter;

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
    private LocalDateTime createTime;
    @Column(name = "modify_time", nullable = false)
    private LocalDateTime modifyTime;

    public BasisEntity() {
        this.createTime = LocalDateTime.now();
        this.modifyTime = this.createTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void updateModifyTime(){
        this.modifyTime = LocalDateTime.now();
    }
}
