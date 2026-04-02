package com.api.ckappbeta.service;

import com.api.framework.service.BaseService;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("ckAppBetaUserService")
public class UserService extends BaseService {

    private static final String USERS = "/users";
    private static final String USER_BY_ID = "/users/{id}";

    public UserService(@Qualifier("ckAppBeta") RequestSpecification requestSpec) {
        super(requestSpec);
    }

    public Response getAllUsers() {
        return get(USERS);
    }

    public Response getUserById(int id) {
        return get(USER_BY_ID, id);
    }
}
