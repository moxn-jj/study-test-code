package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.dto.PostCreateDto;
import com.example.demo.model.dto.PostUpdateDto;
import com.example.demo.repository.PostEntity;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserEntity;
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
    public PostEntity getById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    /**
     * [리팩토링] createPost > create
     * PostService이기 때문에 Post를 붙이지 않아도 의미 전달 가능
     *
     * @param postCreateDto
     * @return
     */
    public PostEntity create(PostCreateDto postCreateDto) {
        UserEntity userEntity = userService.getById(postCreateDto.getWriterId());
        PostEntity postEntity = new PostEntity();
        postEntity.setWriter(userEntity);
        postEntity.setContent(postCreateDto.getContent());
        postEntity.setCreatedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }

    /**
     * [리팩토링] updatePost > update
     * PostService이기 때문에 Post를 붙이지 않아도 의미 전달 가능
     *
     * @param id
     * @param postUpdateDto
     * @return
     */
    public PostEntity update(long id, PostUpdateDto postUpdateDto) {
        PostEntity postEntity = getById(id);
        postEntity.setContent(postUpdateDto.getContent());
        postEntity.setModifiedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }
}