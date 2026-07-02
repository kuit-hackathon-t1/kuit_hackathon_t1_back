package com.example.kuit_hackathon_back.domain.collection.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.kuit_hackathon_back.domain.collection.dto.CollectionDetailResponse;
import com.example.kuit_hackathon_back.domain.collection.dto.CollectionListResponse;
import com.example.kuit_hackathon_back.domain.collection.dto.CreateCollectionRequest;
import com.example.kuit_hackathon_back.domain.collection.dto.CreateCollectionResponse;
import com.example.kuit_hackathon_back.domain.collection.entity.CollectionStatus;
import com.example.kuit_hackathon_back.domain.collection.service.CollectionService;
import com.example.kuit_hackathon_back.global.resolver.CurrentUserId;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @PostMapping
    public ResponseEntity<CreateCollectionResponse> createCollection(
            @CurrentUserId Long userId, @Valid @RequestBody CreateCollectionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(collectionService.createCollection(userId, request));
    }

    @GetMapping
    public ResponseEntity<CollectionListResponse> getCollections(
            @CurrentUserId Long userId,
            @RequestParam Long tripId,
            @RequestParam(required = false) CollectionStatus status) {
        return ResponseEntity.ok(collectionService.getCollections(userId, tripId, status));
    }

    @GetMapping("/{collectionId}")
    public ResponseEntity<CollectionDetailResponse> getCollectionDetail(
            @CurrentUserId Long userId, @PathVariable Long collectionId) {
        return ResponseEntity.ok(collectionService.getCollectionDetail(userId, collectionId));
    }
}
