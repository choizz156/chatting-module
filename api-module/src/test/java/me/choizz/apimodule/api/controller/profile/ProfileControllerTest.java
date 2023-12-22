package me.choizz.apimodule.api.controller.profile;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

class ProfileControllerTest {

    @DisplayName("prod_profile이 조회된다.")
    @Test
    void test1() throws Exception {
        String prod = "prod";

        MockEnvironment mockEnvironment = new MockEnvironment();
        mockEnvironment.addActiveProfile(prod);
        ProfileController profileController = new ProfileController(mockEnvironment);

        String profile = profileController.profile();

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

}