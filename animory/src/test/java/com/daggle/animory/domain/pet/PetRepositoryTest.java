package com.daggle.animory.domain.pet;

import com.daggle.animory.domain.pet.entity.Pet;
import com.daggle.animory.domain.pet.repository.PetRepository;
import com.daggle.animory.testutil.datajpatest.DataJpaTestWithDummyData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class PetRepositoryTest extends DataJpaTestWithDummyData {

    @Autowired
    private PetRepository petRepository;

    @Test
    void findByShelterId() {
        final Integer shelterId = 1;
        final Page<Pet> page = petRepository.findByShelterId(shelterId, PageRequest.of(0, 10));

        final List<Pet> pets = page.getContent();
        print(pets);

        pets.forEach(
            p -> assertThat(p.getShelter().getId()).isEqualTo(shelterId)
        );
    }

    @Test
    void findProfilesWithProtectionExpirationDate() {
        final List<Pet> page =
            petRepository.findProfilesWithProtectionExpirationDate(PageRequest.of(0, 10)).getContent();

        print(page);

        page.forEach(
            p -> System.out.println(p.getId() + " " + p.getProtectionExpirationDate())
        );
    }

    @Test
    void findPage() {
        final List<Pet> page = petRepository.findPageBy(PageRequest.of(0, 10, Sort.Direction.ASC,
                                                                       "protectionExpirationDate")).getContent();

        print(page);

        page.forEach(
            p -> System.out.println(p.getId() + " " + p.getProtectionExpirationDate())
        );

    }

}