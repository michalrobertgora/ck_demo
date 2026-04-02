package com.api.ckappalpha.service;

import com.api.ckappalpha.model.Post;
import com.api.framework.service.BaseService;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("ckAppAlphaPostService")
public class PostService extends BaseService {

    private static final String POSTS = "/posts";
    private static final String POST_BY_ID = "/posts/{id}";
    private static final String POST_COMMENTS = "/posts/{id}/comments";

    public PostService(@Qualifier("ckAppAlpha") RequestSpecification requestSpec) {
        super(requestSpec);
    }

    public Response getAllPosts() {
        return get(POSTS);
    }

    public Response getPostById(int id) {
        return get(POST_BY_ID, id);
    }

    public Response getPostsByUserId(int userId) {
        return getWithQueryParam(POSTS, "userId", userId);
    }

    public Response getCommentsByPostId(int postId) {
        return get(POST_COMMENTS, postId);
    }

    public Response createPost(Post post) {
        return post(POSTS, post);
    }

    public Response updatePost(int id, Post post) {
        return put(POST_BY_ID, post, id);
    }

    public Response patchPost(int id, Post partialPost) {
        return patch(POST_BY_ID, partialPost, id);
    }

    public Response deletePost(int id) {
        return delete(POST_BY_ID, id);
    }

    // Forced examples created to use the builder:

    public Response getPostByIdExpecting200(int id) {
        return request()
                .expectStatus(200)
                .get(POST_BY_ID, id);
    }

    public Response createPostWithCorrelation(Post post, String correlationId) {
        return request()
                .header("X-Correlation-Id", correlationId)
                .expectStatus(201)
                .withLogging()
                .post(POSTS, post);
    }
}
