package com.espresso.springboot.domain.posts;


import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p Order by p.modifiedDate DESC, p.id DESC")
    List<Posts> findAllDesc();

    @Query("SELECT p FROM Posts p Order by p.modifiedDate DESC, p.id DESC")
    List<Posts> findAllDesc(Pageable pageable);

}
