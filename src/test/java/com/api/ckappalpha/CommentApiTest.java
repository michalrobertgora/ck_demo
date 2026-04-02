package com.api.ckappalpha;

import com.api.base.BaseApiTest;
import com.api.base.suite.Regression;
import com.api.base.suite.Smoke;
import com.api.ckappalpha.model.Comment;
import com.api.ckappalpha.service.CommentService;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CkAppAlpha — Comments API")
class CommentApiTest extends BaseApiTest {

    @Autowired
    private CommentService commentService;

    @Test
    @Regression
    @DisplayName("GET /comments — should return all comments")
    void getAllComments() {
        Response response = commentService.getAllComments();

        assertThat(response.getStatusCode()).isEqualTo(200);

        List<Comment> comments = response.as(new TypeRef<>() {});
        assertThat(comments).hasSizeGreaterThan(0);
    }

    @Test
    @Smoke
    @DisplayName("GET /comments/{id} — should return a single comment")
    void getCommentById() {
        Response response = commentService.getCommentById(1);

        assertThat(response.getStatusCode()).isEqualTo(200);

        Comment comment = response.as(Comment.class);
        assertThat(comment.getId()).isEqualTo(1);
        assertThat(comment.getEmail()).isNotBlank();
    }

    @Test
    @Regression
    @DisplayName("GET /comments?postId=1 — should filter comments by post")
    void getCommentsByPost() {
        Response response = commentService.getCommentsByPostId(1);

        assertThat(response.getStatusCode()).isEqualTo(200);

        List<Comment> comments = response.as(new TypeRef<>() {});
        assertThat(comments)
                .isNotEmpty()
                .allSatisfy(c -> assertThat(c.getPostId()).isEqualTo(1));
    }
}
