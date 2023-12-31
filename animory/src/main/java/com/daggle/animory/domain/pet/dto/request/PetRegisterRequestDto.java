package com.daggle.animory.domain.pet.dto.request;

import com.daggle.animory.domain.pet.dto.PetPolygonProfileDto;
import com.daggle.animory.domain.pet.entity.*;
import com.daggle.animory.domain.pet.util.PetAgeToBirthDateConverter;
import com.daggle.animory.domain.shelter.entity.Shelter;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Builder
public record PetRegisterRequestDto(
    @NotNull(message = "이름을 입력해주세요.") String name,
    @NotNull(message = "나이를 입력해주세요.") String age,
    @NotNull(message = "종을 입력해주세요.") PetType type,
    float weight,
    String size,
    @NotNull(message = "성별을 입력해주세요.") Sex sex,
    String vaccinationStatus,
    @NotNull(message = "입양 상태를 입력해주세요.") AdoptionStatus adoptionStatus,
    NeutralizationStatus neutralizationStatus,
    LocalDate protectionExpirationDate,
    String description,
    PetPolygonProfileDto petPolygonProfileDto
) {

    public Pet toEntity(final Shelter shelter, final String imageUrl, final String videoUrl) {
        return Pet.builder()
            .name(name)
            .birthDate(PetAgeToBirthDateConverter.ageToBirthDate(age))
            .type(type)
            .weight(weight)
            .sex(sex)
            .description(description)
            .protectionExpirationDate(protectionExpirationDate)
            .vaccinationStatus(vaccinationStatus)
            .neutralizationStatus(neutralizationStatus)
            .adoptionStatus(adoptionStatus)
            .profileImageUrl(imageUrl)
            .petVideos(List.of(PetVideo.builder().videoUrl(videoUrl).build())) // 현재는 1개의 동영상만 저장한다.
            .size(size)
            .petPolygonProfile(petPolygonProfileDto.toEntity())
            .shelter(shelter)
            .build();
    }
}
