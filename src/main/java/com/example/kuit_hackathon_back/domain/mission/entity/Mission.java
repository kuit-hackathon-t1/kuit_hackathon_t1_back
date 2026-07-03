package com.example.kuit_hackathon_back.domain.mission.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import com.example.kuit_hackathon_back.domain.collection.entity.CollectionStatus;
import com.example.kuit_hackathon_back.domain.trip.entity.Trip;
import com.example.kuit_hackathon_back.domain.user.entity.User;
import com.example.kuit_hackathon_back.global.entity.BaseTimeEntity;
import com.example.kuit_hackathon_back.global.exception.BusinessException;
import com.example.kuit_hackathon_back.global.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mission")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mission extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mission_id")
    private Long missionId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 600)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "mission_category", nullable = false, length = 30)
    private MissionCategory missionCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "mission_status", nullable = false, length = 30)
    private MissionStatus missionStatus;

    @Column(name = "is_local", nullable = false)
    private boolean local;

    /** AI가 배치로 미리 생성해둔 추천 풀에 있을 뿐 아직 사용자에게 뽑혀서(노출되어) 나가지 않은 경우 null. */
    @Column(name = "drawn_at")
    private LocalDateTime drawnAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("guideId asc")
    private List<MissionGuide> guides = new ArrayList<>();

    @Builder
    private Mission(
            String title,
            String description,
            MissionCategory missionCategory,
            boolean local,
            User user,
            Trip trip) {
        this.title = title;
        this.description = description;
        this.missionCategory = missionCategory;
        this.local = local;
        this.user = user;
        this.trip = trip;
        this.missionStatus = MissionStatus.RECOMMENDED;
    }

    public void addGuide(String comment) {
        this.guides.add(MissionGuide.builder().mission(this).comment(comment).build());
    }

    /** AI 추천 풀에서 이 미션을 실제로 뽑아 사용자에게 보여줄 때 호출한다. */
    public void draw() {
        this.drawnAt = LocalDateTime.now();
    }

    public boolean isDrawn() {
        return this.drawnAt != null;
    }

    public void start() {
        if (this.missionStatus == MissionStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.MISSION_ALREADY_STARTED);
        }
        if (this.missionStatus == MissionStatus.SUCCESS
                || this.missionStatus == MissionStatus.FAILURE) {
            throw new BusinessException(ErrorCode.MISSION_ALREADY_COMPLETED);
        }
        this.missionStatus = MissionStatus.ACTIVE;
    }

    /** 여행 종료(수동 종료)에 맞춰 SUCCESS/FAILURE가 아닌 미션을 일괄 CANCELLED 처리할 때 사용한다. */
    public void cancelIfNotTerminal() {
        if (this.missionStatus == MissionStatus.SUCCESS
                || this.missionStatus == MissionStatus.FAILURE
                || this.missionStatus == MissionStatus.CANCELLED) {
            return;
        }
        this.missionStatus = MissionStatus.CANCELLED;
    }

    public void complete(CollectionStatus collectionStatus) {
        if (this.missionStatus == MissionStatus.SUCCESS
                || this.missionStatus == MissionStatus.FAILURE) {
            throw new BusinessException(ErrorCode.MISSION_ALREADY_COMPLETED, "이미 기록된 미션입니다.");
        }
        this.missionStatus =
                collectionStatus == CollectionStatus.SUCCESS
                        ? MissionStatus.SUCCESS
                        : MissionStatus.FAILURE;
    }

    public boolean isOwnedBy(Long userId) {
        return this.user.getUserId().equals(userId);
    }
}
