package com.api.ckappalpha;

import com.api.base.BaseApiTest;
import com.api.base.suite.Regression;
import com.api.base.suite.Smoke;
import com.api.ckappalpha.model.Post;
import com.api.ckappalpha.service.PostService;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CkAppAlpha — Posts API")
class PostApiTest extends BaseApiTest {

    @Autowired
    private PostService postService;

    @Nested
    @DisplayName("GET /posts")
    class GetPosts {

        @Test
        @Regression
        @DisplayName("should return all 100 posts")
        void getAllPosts() {
            Response response = postService.getAllPosts();

            assertThat(response.getStatusCode()).isEqualTo(200);

            List<Post> posts = response.as(new TypeRef<>() {});
            assertThat(posts).hasSize(100);
        }

        @Test
        @Smoke
        @DisplayName("should return a single post by id")
        void getPostById() {
            Response response = postService.getPostById(1);

            assertThat(response.getStatusCode()).isEqualTo(200);

            Post post = response.as(Post.class);
            assertThat(post.getId()).isEqualTo(1);
            assertThat(post.getUserId()).isNotNull();
            assertThat(post.getTitle()).isNotBlank();
        }

        @Test
        @Regression
        @DisplayName("should return 404 for non-existent post")
        void getNonExistentPost() {
            Response response = postService.getPostById(99999);

            assertThat(response.getStatusCode()).isEqualTo(404);
        }

        @Test
        @Regression
        @DisplayName("should filter posts by userId")
        void getPostsByUserId() {
            Response response = postService.getPostsByUserId(1);

            assertThat(response.getStatusCode()).isEqualTo(200);

            List<Post> posts = response.as(new TypeRef<>() {});
            assertThat(posts)
                    .isNotEmpty()
                    .allSatisfy(p -> assertThat(p.getUserId()).isEqualTo(1));
        }
    }

    @Nested
    @DisplayName("POST /posts")
    class CreatePosts {

        @Test
        @Smoke
        @DisplayName("should create a new post and return 201")
        void createPost() {
            Post newPost = Post.builder()
                    .userId(1)
                    .title("Framework Test Post")
                    .body("Created by the API test framework")
                    .build();

            Response response = postService.createPost(newPost);

            assertThat(response.getStatusCode()).isEqualTo(201);

            Post created = response.as(Post.class);
            assertThat(created.getId()).isNotNull();
            assertThat(created.getTitle()).isEqualTo(newPost.getTitle());
        }
    }

    @Nested
    @DisplayName("PUT /posts/{id}")
    class UpdatePosts {

        @Test
        @Regression
        @DisplayName("should fully replace a post")
        void updatePost() {
            Post updated = Post.builder()
                    .userId(1)
                    .title("Updated Title")
                    .body("Updated Body")
                    .build();

            Response response = postService.updatePost(1, updated);

            assertThat(response.getStatusCode()).isEqualTo(200);

            Post result = response.as(Post.class);
            assertThat(result.getTitle()).isEqualTo("Updated Title");
            assertThat(result.getBody()).isEqualTo("Updated Body");
        }
    }


    @Nested
    @DisplayName("PATCH /posts/{id}")
    class PatchPosts {

        @Test
        @Regression
        @DisplayName("should partially update a post")
        void patchPostTitle() {
            Post partial = Post.builder()
                    .title("Patched Title")
                    .build();

            Response response = postService.patchPost(1, partial);

            assertThat(response.getStatusCode()).isEqualTo(200);

            Post result = response.as(Post.class);
            assertThat(result.getTitle()).isEqualTo("Patched Title");
            assertThat(result.getBody()).isNotBlank();
        }
    }


    @Nested
    @DisplayName("DELETE /posts/{id}")
    class DeletePosts {

        @Test
        @Smoke
        @DisplayName("should delete a post and return 200")
        void deletePost() {
            Response response = postService.deletePost(1);

            assertThat(response.getStatusCode()).isEqualTo(200);
        }
    }

    // Forced useage of request builder

    @Nested
    @DisplayName("Request Builder Showcase Test")
    class RequestBuilderExamples {

        @Test
        @Regression
        @DisplayName("should validate status inline via expectStatus")
        void getWithInlineStatusValidation() {
            Response response = postService.getPostByIdExpecting200(1);

            Post post = response.as(Post.class);
            assertThat(post.getId()).isEqualTo(1);
        }

        @Test
        @Regression
        @DisplayName("should send custom header and validate 201 on create")
        void createWithCorrelationHeader() {
            Post newPost = Post.builder()
                    .userId(1)
                    .title("Correlated Post")
                    .body("Sent with X-Correlation-Id")
                    .build();

            Response response = postService.createPostWithCorrelation(newPost, "test-corr-001");

            Post created = response.as(Post.class);
            assertThat(created.getTitle()).isEqualTo("Correlated Post");
        }
    }
}
