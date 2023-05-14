package com.idle.gaza.db.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/*
* 코드 테이블
* */
@Entity(name = "code")
@Getter
@Setter
@NoArgsConstructor
public class Code {

    @Id
    @Column(name = "code_id")
    private int codeId;

    @Column(name="parent_id")
    private int parentId;

    private String name;

    private String description;

    @Builder
    public Code(int codeId, int parentId, String name, String description) {
        this.codeId = codeId;
        this.parentId = parentId;
        this.name = name;
        this.description = description;
    }


}
