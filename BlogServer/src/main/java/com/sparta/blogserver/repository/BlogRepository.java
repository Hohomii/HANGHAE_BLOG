package com.sparta.blogserver.repository;

import com.sparta.blogserver.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
// import java.util.Optional; // findOne 써보려 했는데 Optional 이게 뭔지 잘 모르겠어서 못 씀. 이거 무엇?


public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByOrderByCreatedAtDesc(); // 작성일 기준 내림차순 정렬 리스트

}