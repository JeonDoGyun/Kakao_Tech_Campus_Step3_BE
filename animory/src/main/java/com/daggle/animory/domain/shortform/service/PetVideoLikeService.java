package com.daggle.animory.domain.shortform.service;

import com.daggle.animory.domain.pet.entity.PetVideo;
import com.daggle.animory.domain.shortform.entity.PetVideoLike;
import com.daggle.animory.domain.shortform.exception.AlreadyLikedPetVideo;
import com.daggle.animory.domain.shortform.exception.NotLikedPetVideo;
import com.daggle.animory.domain.shortform.exception.ShortFormNotFound;
import com.daggle.animory.domain.shortform.repository.PetVideoJpaRepository;
import com.daggle.animory.domain.shortform.repository.PetVideoLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class PetVideoLikeService {
    final PetVideoJpaRepository petVideoJpaRepository;
    final PetVideoLikeRepository petVideoLikeRepository;

    public void updatePetVideoLikeCount(final String IPAddress, final int petVideoId) {
        validatePetVideoLikeDuplication(IPAddress, petVideoId);

        final PetVideo petVideo = petVideoJpaRepository.findById(petVideoId)
            .orElseThrow(ShortFormNotFound::new);

        petVideo.updateLikeCount();

        petVideoLikeRepository.save(PetVideoLike.builder()
                                        .ipAddress(IPAddress)
                                        .petVideo(petVideo)
                                        .build());
    }

    public void deletePetVideoLikeCount(final String ipAddress, final int petVideoId) {
        validatePetVideoLikeDelete(ipAddress, petVideoId);

        final PetVideo petVideo = petVideoJpaRepository.findById(petVideoId)
            .orElseThrow(ShortFormNotFound::new);

        petVideo.deleteLikeCount();

        petVideoLikeRepository.deleteByIpAddressAndPetVideoId(ipAddress, petVideoId);
    }

    private void validatePetVideoLikeDuplication(final String IPAddress, final int petVideoId) {
        if (petVideoLikeRepository.existsByIpAddressAndPetVideoId(IPAddress, petVideoId)) {
            throw new AlreadyLikedPetVideo();
        }
    }

    private void validatePetVideoLikeDelete(final String IPAddress, final int petVideoId) {
        if (!petVideoLikeRepository.existsByIpAddressAndPetVideoId(IPAddress, petVideoId)) {
            throw new NotLikedPetVideo();
        }
    }
}