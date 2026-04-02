package com.api.ckappbeta;

import com.api.base.BaseApiTest;
import com.api.base.suite.Regression;
import com.api.base.suite.Smoke;
import com.api.ckappbeta.model.User;
import com.api.ckappbeta.service.UserService;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CkAppBeta — Users API")
class UserApiTest extends BaseApiTest {

    @Autowired
    private UserService userService;

    @Test
    @Smoke
    @DisplayName("GET /users — should return a non-empty list of users")
    void getAllUsers() {
        Response response = userService.getAllUsers();

        assertThat(response.getStatusCode()).isEqualTo(200);

        List<User> users = response.as(new TypeRef<>() {});
        assertThat(users).isNotEmpty();
        assertThat(users.getFirst().getId()).isNotNull();
    }

    @Test
    @Regression
    @DisplayName("GET /users/{id} — should return a single user with expected fields")
    void getUserById() {
        Response response = userService.getUserById(1);

        assertThat(response.getStatusCode()).isEqualTo(200);

        User user = response.as(User.class);
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getName()).isNotBlank();
        assertThat(user.getEmail()).contains("@");
        assertThat(user.getCountry()).isNotBlank();
    }

    @Test
    @Regression
    @DisplayName("GET /users/{id} — should match user details from list endpoint")
    void userByIdMatchesListEntry() {
        // get all users, store first in world
        Response listResponse = userService.getAllUsers();
        assertThat(listResponse.getStatusCode()).isEqualTo(200);

        List<User> users = listResponse.as(new TypeRef<>() {});
        User fromList = users.getFirst();
        world.put("expectedUser", fromList);

        // get the same user by stored id
        User expected = world.get("expectedUser", User.class);
        Response detailResponse = userService.getUserById(expected.getId());
        assertThat(detailResponse.getStatusCode()).isEqualTo(200);

        User fromDetail = detailResponse.as(User.class);

        // field-by-field comparison - fails as this mocked API uses randomized data in responses
        assertThat(fromDetail).isEqualTo(expected);
    }
}
