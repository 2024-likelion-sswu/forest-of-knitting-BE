package com.example.knitforest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class KnitRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private Users user;

    private String title;

    private Integer time;

    private Integer level;

    private Boolean isPosted;

    private Integer recommendation;

    public void setRecommendation() {
        this.recommendation++;
    }
}
