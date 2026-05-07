package com.karthi.sprintiq.fields.repository;

import com.karthi.sprintiq.fields.entity.FieldOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldOptionRepository
  extends JpaRepository<FieldOption, Long> {}
