package com.examcraft.repository;

import com.examcraft.entity.TestPaper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestPaperRepository extends JpaRepository<TestPaper, Long> {
}