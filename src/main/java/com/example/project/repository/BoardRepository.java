package com.example.project.repository;

import com.example.project.entity.Board;
import com.example.project.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Query("update Board b set b.count = b.count + 1 where b.id = :id")
    void updateCount(@Param("id") Long id);

    Page<Board> findAllByMember(Member member, Pageable pageable);

    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);



}
