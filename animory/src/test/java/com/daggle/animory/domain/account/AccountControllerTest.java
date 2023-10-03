package com.daggle.animory.domain.account;

import com.daggle.animory.domain.account.dto.request.ShelterAddressSignUpDto;
import com.daggle.animory.domain.account.dto.request.ShelterSignUpDto;
import com.daggle.animory.domain.shelter.entity.Province;
import com.daggle.animory.testutil.webmvctest.BaseWebMvcTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(AccountController.class)
public class AccountControllerTest extends BaseWebMvcTest {

    @Autowired
    ObjectMapper om;

    @MockBean
    AccountService accountService;

    @Nested
    class 회원가입 {
        @Test
        void 성공_보호소_회원가입() throws Exception {
            // given
            ShelterSignUpDto shelterSignUpDto = ShelterSignUpDto.builder()
                    .name("animory")
                    .email("aaa@jnu.ac.kr")
                    .password("secreT123!")
                    .contact("01012345678")
                    .zonecode("3143")
                    .address(ShelterAddressSignUpDto.builder()
                            .province(Province.광주)
                            .city("북구")
                            .roadName("용봉동")
                            .detail("전남대")
                            .build())
                    .build();

            // when
            mvc.perform(post("/account/shelter")
                            .content(om.writeValueAsString(shelterSignUpDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andDo(print());
        }

        @Test
        void 실패_이메일_형식_오류() throws Exception {
            // given
            ShelterSignUpDto shelterSignUpDto = ShelterSignUpDto.builder()
                    .name("animory")
                    .email("aaajnu.ac.kr")
                    .password("secreR123!")
                    .contact("01012345678")
                    .zonecode("3143")
                    .address(ShelterAddressSignUpDto.builder()
                            .province(Province.광주)
                            .city("북구")
                            .roadName("용봉동")
                            .detail("전남대")
                            .build())
                    .build();

            // when
            mvc.perform(post("/account/shelter")
                            .content(om.writeValueAsString(shelterSignUpDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andDo(print());
        }

        @Test
        void 실패_비밀번호_형식_오류() throws Exception {
            // given
            ShelterSignUpDto shelterSignUpDto = ShelterSignUpDto.builder()
                    .name("animory")
                    .email("aaa@jnu.ac.kr")
                    .password("secre123!")
                    .contact("01012345678")
                    .zonecode("3143")
                    .address(ShelterAddressSignUpDto.builder()
                            .province(Province.광주)
                            .city("북구")
                            .roadName("용봉동")
                            .detail("전남대")
                            .build())
                    .build();

            // when
            mvc.perform(post("/account/shelter")
                            .content(om.writeValueAsString(shelterSignUpDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andDo(print());
        }
    }


}
