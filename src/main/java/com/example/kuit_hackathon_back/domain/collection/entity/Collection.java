package com.example.kuit_hackathon_back.domain.collection.entity;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.example.kuit_hackathon_back.domain.mission.entity.Mission;
import com.example.kuit_hackathon_back.domain.trip.entity.Trip;
import com.example.kuit_hackathon_back.domain.user.entity.User;
import com.example.kuit_hackathon_back.global.entity.BaseTimeEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "collection")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Collection extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collection_id")
    private Long collectionId;

    @Column(nullable = false, length = 500)
    private String memo;

    @Column(name = "local_image_id", nullable = false, length = 255)
    private String localImageId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CollectionStatus status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false, unique = true)
    private Mission mission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "crop_type", nullable = false, length = 50)
    private CropType cropType;

    @OneToMany(mappedBy = "collection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Emotion> emotions = new ArrayList<>();

    @Builder
    private Collection(
            String memo,
            String localImageId,
            CollectionStatus status,
            Mission mission,
            Trip trip,
            User user,
            CropType cropType) {
        this.memo = memo;
        this.localImageId = localImageId;
        this.status = status;
        this.mission = mission;
        this.trip = trip;
        this.user = user;
        this.cropType = cropType;
    }

    public void addEmotionTag(String tag) {
        this.emotions.add(Emotion.builder().collection(this).tag(tag).build());
    }

    public boolean isOwnedBy(Long userId) {
        return this.user.getUserId().equals(userId);
    }
}
