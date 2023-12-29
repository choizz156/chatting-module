package me.choizz.apimodule.api.controller.profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import me.choizz.apimodule.profile.ProfileController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
class ProfileControllerTest {

    @DisplayName("prod_profile이 조회된다.")
    @Test
    void test1() throws Exception {
        //given
        String prod = "prod";

        //when
        MockEnvironment mockEnvironment = new MockEnvironment();
        mockEnvironment.addActiveProfile(prod);
        ProfileController profileController = new ProfileController(mockEnvironment);
        String profile = profileController.profile();

        //then
        assertThat(profile).isEqualTo(prod);
    }

    @DisplayName("prod 프로파일이 없으면 첫 번째가 조회된다.")
    @Test
    void test2() throws Exception {
        //given
        String expectedProfile = "local";
        MockEnvironment env = new MockEnvironment();

        env.addActiveProfile(expectedProfile);
        env.addActiveProfile("test");

        ProfileController profileController = new ProfileController(env);

        //when
        String profile = profileController.profile();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }

    @DisplayName("active profile이 없으면 default가 조회된다.")
    @Test
    void test3() throws Exception {
        //given
        String expectedProfile = "default";
        MockEnvironment env = new MockEnvironment();

        ProfileController profileController = new ProfileController(env);

        //when
        String profile = profileController.profile();

        //then
        assertThat(profile).isEqualTo(expectedProfile);
    }


    @Nested
    @DisplayName("프로파일 조회 api 테스트")
    @WebMvcTest(controllers = ProfileController.class)
    class ProfileApiTest {

        @Autowired
        private MockMvc mockMvc;

        @WithMockUser
        @DisplayName("현재 프로파일을 조회할 수 있다.")
        @Test
        void test4() throws Exception {
            //given
            mockMvc.perform(
                    get("/profile")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("test"))
                .andDo(print());
        }
    }
}