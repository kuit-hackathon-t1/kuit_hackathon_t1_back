package com.example.kuit_hackathon_back.domain.trip.entity;

import com.example.kuit_hackathon_back.domain.user.entity.User;
import com.example.kuit_hackathon_back.global.entity.BaseTimeEntity;
import com.example.kuit_hackathon_back.global.exception.BusinessException;
import com.example.kuit_hackathon_back.global.exception.ErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "trip")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trip extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long tripId;

    @Column(name = "trip_name", nullable = false, length = 60)
    private String tripName;

    @Column(nullable = false, length = 75)
    private String region;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "companion_type", nullable = false, length = 20)
    private CompanionType companionType;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private TripMood mood;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TripStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    private Trip(String tripName, String region, LocalDate startDate, LocalDate endDate,
            CompanionType companionType, TripMood mood, User user) {
        this.tripName = tripName;
        this.region = region;
        this.startDate = startDate;
        this.endDate = endDate;
        this.companionType = companionType;
        this.mood = mood;
        this.user = user;
        this.status = TripStatus.ACTIVE;
    }

    public void end() {
        if (this.status == TripStatus.ENDED) {
            throw new BusinessException(ErrorCode.TRIP_ALREADY_ENDED);
        }
        this.status = TripStatus.ENDED;
    }

    public boolean isActive() {
        return this.status == TripStatus.ACTIVE;
    }

    public boolean isOwnedBy(Long userId) {
        return this.user.getUserId().equals(userId);
    }
}
