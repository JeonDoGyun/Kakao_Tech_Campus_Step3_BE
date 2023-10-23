package com.daggle.animory.domain.account;

import com.daggle.animory.common.Response;
import com.daggle.animory.common.security.TokenProvider;
import com.daggle.animory.domain.account.dto.TokenWithExpirationDateTimeDto;
import com.daggle.animory.domain.account.dto.request.AccountLoginDto;
import com.daggle.animory.domain.account.dto.request.EmailValidateDto;
import com.daggle.animory.domain.account.dto.request.ShelterSignUpDto;
import com.daggle.animory.domain.account.dto.response.AccountLoginSuccessDto;
import com.daggle.animory.domain.account.entity.Account;
import com.daggle.animory.domain.account.exception.AlreadyExistEmailException;
import com.daggle.animory.domain.account.exception.CheckEmailOrPasswordException;
import com.daggle.animory.domain.shelter.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AccountService {
    private final ShelterRepository shelterRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public void registerShelterAccount(final ShelterSignUpDto shelterSignUpDto) {
        validateEmailDuplication(new EmailValidateDto(shelterSignUpDto.email()));

        final Account newAccount = accountRepository.save(
            shelterSignUpDto.getAccount(passwordEncoder));

        // TODO: 이미 존재하는 쉘터일 경우 예외처리 필요

        shelterRepository.save(shelterSignUpDto.getShelter(newAccount));
    }

    public ResponseEntity<Response<AccountLoginSuccessDto>> login(final AccountLoginDto accountLoginDto) {
        final Account account = accountRepository.findByEmail(accountLoginDto.email())
            .orElseThrow(CheckEmailOrPasswordException::new);

        validatePassword(accountLoginDto, account);

        final TokenWithExpirationDateTimeDto tokenWithExpirationDateTimeDto =
            tokenProvider.createTokenWithExpirationDateTimeDto(account.getEmail(), account.getRole());

        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, tokenWithExpirationDateTimeDto.token())
            .body(Response.success(
                AccountLoginSuccessDto.builder()
                    .id(account.getId())
                    .accountRole(account.getRole())
                    .tokenExpirationDateTime(tokenWithExpirationDateTimeDto.expirationDateTime())
                    .build()
            ));
    }

    private void validatePassword(final AccountLoginDto accountLoginDto, final Account account) {
        if(!passwordEncoder.matches(accountLoginDto.password(), account.getPassword()))
            throw new CheckEmailOrPasswordException();
    }

    public void validateEmailDuplication(final EmailValidateDto emailValidateDto) {
        if (accountRepository.existsByEmail(emailValidateDto.email()))
            throw new AlreadyExistEmailException();
    }
}
