package com.fast.sns.model.entity;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Data
public class UserEntity {

    @Id
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    private String password;

}
