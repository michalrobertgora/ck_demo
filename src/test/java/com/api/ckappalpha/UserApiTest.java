package com.api.ckappalpha;

import com.api.base.BaseApiTest;
import com.api.base.suite.Regression;
import com.api.base.suite.Smoke;
import com.api.ckappalpha.model.User;
import com.api.ckappalpha.model.UserDetails;
import com.api.ckappalpha.service.UserService;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CkAppAlpha — Users API")
class UserApiTest extends BaseApiTest {

    @Autowired
    private UserService userService;

    @Test
    @Smoke
    @DisplayName("GET /users — should return all 10 users")
    void getAllUsers() {
        Response response = userService.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(200);

        List<User> users = response.as(new TypeRef<>() {});
        assertThat(users).hasSize(10);
    }

    @Test
    @Regression
    @DisplayName("GET /users/{id} — should return user details")
    void getUserById() {
        Response response = userService.getUserById(1);

        assertThat(response.getStatusCode()).isEqualTo(200);

        User user = response.as(User.class);
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isNotBlank();
        assertThat(user.getEmail()).contains("@");
    }

    @Test
    @Regression
    @DisplayName("GET /users?username=Bret — should filter by username")
    void getUserByUsername() {
        Response response = userService.getUserByUsername("Bret");

        assertThat(response.getStatusCode()).isEqualTo(200);

        List<User> users = response.as(new TypeRef<>() {});
        assertThat(users)
                .hasSize(1)
                .first()
                .satisfies(u -> assertThat(u.getUsername()).isEqualTo("Bret"));
    }

    // Forced example of mapping the response to our business model representation and using some methods for assertions

    @Nested
    @DisplayName("UserDetails — business logic")
    class UserDetailsBusinessLogic {

        @Test
        @Regression
        @DisplayName("should build a formatted address from nested address fields")
        void formattedAddress() {
            Response response = userService.getUserById(1);

            assertThat(response.getStatusCode()).isEqualTo(200);

            UserDetails details = response.as(UserDetails.class);

            assertThat(details.getAddress()).isNotNull();
            assertThat(details.getFormattedAddress())
                    .contains(details.getAddress().getStreet())
                    .contains(details.getAddress().getCity())
                    .contains(details.getAddress().getZipcode());
        }

        @Test
        @Regression
        @DisplayName("should determine hemisphere from geo-coordinates")
        void hemisphereFromGeoCoordinates() {
            Response response = userService.getUserById(1);

            assertThat(response.getStatusCode()).isEqualTo(200);

            UserDetails details = response.as(UserDetails.class);
            double latitude = Double.parseDouble(details.getAddress().getGeo().getLat());

            if (latitude > 0) {
                assertThat(details.isLocatedInNorthernHemisphere()).isTrue();
            } else {
                assertThat(details.isLocatedInNorthernHemisphere()).isFalse();
            }
        }
    }
}
