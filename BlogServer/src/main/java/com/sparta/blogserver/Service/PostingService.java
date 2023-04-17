package com.sparta.blogserver.Service;

import com.sparta.blogserver.dto.PostingResponseDto;
import com.sparta.blogserver.entity.Blog;
import com.sparta.blogserver.repository.BlogRepository;
import com.sparta.blogserver.dto.PostingRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final BlogRepository blogRepository;

    @Transactional
    public Blog createPosting(PostingRequestDto requestDto) {
        Blog blog = new Blog(requestDto);
        blogRepository.save(blog);
        return blog;
    }

    @Transactional(readOnly = true)
    public List<Blog> getPostingList() {
        return blogRepository.findByOrderByCreatedAtDesc();
    }

    @Transactional
    public Long updatePosting(Long id, PostingRequestDto requestDto) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

        if (blog.getPassword().equals(requestDto.getPassword())) {
            blog.updatePosting(requestDto);
            return blog.getId();
        } else {
            return blog.getId();
        }
    }

    @Transactional
    public Long deletePosting(Long id, PostingRequestDto requestDto) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );

        if (blog.getPassword().equals(requestDto.getPassword())) {
            blogRepository.deleteById(id);
            return id;
        } else {
            return id;
        }
    }

    public PostingResponseDto getPosting(Long id) {
        Blog blog = blogRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        return new PostingResponseDto(blog);
    }
}
