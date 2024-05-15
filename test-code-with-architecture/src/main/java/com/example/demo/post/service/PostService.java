package com.example.demo.post.service;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.domain.User;
import com.example.demo.user.service.UserService;
import com.example.demo.user.infrastructure.UserEntity;
import java.time.Clock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    /**
     * [리팩토링] getPostById > getById
     * PostService이기 때문에 Post를 붙이지 않아도 의미 전달 가능
     *
     * @param id
     * @return
     */
    public Post getById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    /**
     * [리팩토링] createPost > create
     * PostService이기 때문에 Post를 붙이지 않아도 의미 전달 가능
     *
     * @param postCreate
     * @return
     */
    public Post create(PostCreate postCreate) {
        User writer = userService.getById(postCreate.getWriterId());
        Post post = Post.from(writer, postCreate);
        return postRepository.save(post);
    }

    /**
     * [리팩토링] updatePost > update
     * PostService이기 때문에 Post를 붙이지 않아도 의미 전달 가능
     *
     * @param id
     * @param postUpdate
     * @return
     */
    public Post update(long id, PostUpdate postUpdate) {
        Post post = getById(id);
        post = post.update(postUpdate);
        return postRepository.save(post);
    }
}