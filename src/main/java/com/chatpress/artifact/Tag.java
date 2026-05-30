package com.chatpress.artifact;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    protected Tag() {
    }

    public Tag(String name) {
        this.name = name.toLowerCase(java.util.Locale.ROOT).trim();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
