package com.espresso.springboot.domain.posts;


import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p Order by p.modifiedDate DESC, p.id DESC")
    List<Posts> findAllDesc();

    @Query("SELECT p FROM Posts p Order by p.modifiedDate DESC, p.id DESC")
    List<Posts> findAllDesc(Pageable pageable);

    @Query("SELECT p FROM Posts p Where p.uid = :uid Order by p.modifiedDate DESC, p.id DESC")
    List<Posts> findByUid(@Param("uid") Long uid, Pageable pageable);

    Optional<Posts> findTopByOrderByModifiedDateDesc();

}
