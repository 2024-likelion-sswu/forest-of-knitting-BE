package com.example.knitforest.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Builder
public class AccTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="user_id", referencedColumnName = "id")
    private Users user;

    private Integer accTime;

    private Integer step;

    public void setAccTime(Integer accTime) {
        this.accTime = accTime;

        if (accTime != null) {
            this.step = calculateStep(accTime);
        }
    }

    private Integer calculateStep(Integer accTime) {

        if (accTime < 10) {
            return 1;
        } else if (accTime <= 20) {
            return 2;
        } else if (accTime <= 30) {
            return 3;
        } else if (accTime <= 40) {
            return 4;
        } else {
            return 5;
        }
    }
}
