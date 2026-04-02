package com.api.ckappalpha.service;

import com.api.framework.service.BaseService;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("ckAppAlphaCommentService")
public class CommentService extends BaseService {

    private static final String COMMENTS = "/comments";
    private static final String COMMENT_BY_ID = "/comments/{id}";

    public CommentService(@Qualifier("ckAppAlpha") RequestSpecification requestSpec) {
        super(requestSpec);
    }

    public Response getAllComments() {
        return get(COMMENTS);
    }

    public Response getCommentById(int id) {
        return get(COMMENT_BY_ID, id);
    }

    public Response getCommentsByPostId(int postId) {
        return getWithQueryParam(COMMENTS, "postId", postId);
    }
}
