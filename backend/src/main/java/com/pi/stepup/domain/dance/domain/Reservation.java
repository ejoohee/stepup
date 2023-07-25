package com.pi.stepup.domain.dance.domain;

import com.pi.stepup.domain.user.domain.User;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User host;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RANDOM_DANCE_ID")
    private RandomDance randomDance;

    @Builder
    public Reservation(Long reservationId, User host, RandomDance randomDance) {
        this.reservationId = reservationId;
        this.host = host;
        this.randomDance = randomDance;
    }
}
