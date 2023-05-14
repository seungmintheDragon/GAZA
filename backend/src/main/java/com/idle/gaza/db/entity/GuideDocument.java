package com.idle.gaza.db.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class GuideDocument {

    @Id
    Integer userId;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name="user_id")
    User user;

    @Column(name = "id_file")
    String idFile;

    @Column(name = "certificate_residence")
    String certificateResidence;

    String certificate;

    @Builder
    public GuideDocument(Integer userId, String idFile, String certificateResidence, String certificate) {
        this.userId = userId;
        this.idFile = idFile;
        this.certificateResidence = certificateResidence;
        this.certificate = certificate;
    }
}
