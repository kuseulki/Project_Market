package com.example.project.repository;

import com.example.project.entity.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {
}
