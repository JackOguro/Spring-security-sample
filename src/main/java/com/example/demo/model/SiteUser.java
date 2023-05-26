package com.example.demo.model;

import com.example.demo.util.Authority;
import com.example.demo.validator.UniqueLogin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 20)
    @UniqueLogin  // 自作バリデーションを追加
    private String username;

    @Size(min = 4, max = 255)
    private String password;

    @NotBlank
    @Email
    private String email;

    private int gender;
    private boolean admin;
    private Authority authority;
}
