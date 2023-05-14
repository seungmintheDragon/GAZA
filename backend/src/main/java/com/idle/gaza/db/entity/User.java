package com.idle.gaza.db.entity;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    Integer userId;

    String name;

    String phone_number;

    String id;

    @Column(name = "password")
    String password;

    String gender;

    Date birthday;

    String picture;

    String email;

    String email_domain;

    @Column(name = "state_code")
    String state;

    @Builder(builderMethodName = "userBuilder", toBuilder = true)
    public User(int userId, String name, String phone_number, String id, String password, String gender, Date birthday, String picture, String email, String email_domain, String state) {
        this.userId = userId;
        this.name = name;
        this.phone_number = phone_number;
        this.id = id;
        this.password = password;
        this.gender = gender;
        this.birthday = birthday;
        this.picture = picture;
        this.email = email;
        this.email_domain = email_domain;
        this.state = state;
    }
}