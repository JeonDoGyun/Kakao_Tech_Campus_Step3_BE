package com.daggle.animory.domain.pet.dto.response;

import com.daggle.animory.domain.pet.entity.Pet;
import com.daggle.animory.domain.pet.util.PetAgeToBirthDateConverter;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record SosPetDto(
        Integer petId,
        String petName,
        String petAge,
        String profileImageUrl,
        LocalDate protectionExpirationDate,
        Integer shelterId,
        String shelterName
) {

    public static SosPetDto fromEntity(Pet pet) {
        return SosPetDto.builder()
                .petId(pet.getId())
                .petName(pet.getName())
                .petAge(PetAgeToBirthDateConverter.birthDateToAge(pet.getBirthDate()))
                .profileImageUrl(pet.getProfileImageUrl())
                .protectionExpirationDate(pet.getProtectionExpirationDate())
                .shelterId(pet.getShelter().getId())
                .shelterName(pet.getShelter().getName())
                .build();
    }
}
