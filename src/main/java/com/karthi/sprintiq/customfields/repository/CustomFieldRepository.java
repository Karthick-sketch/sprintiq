package com.karthi.sprintiq.customfields.repository;

import com.karthi.sprintiq.customfields.entity.CustomField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomFieldRepository
  extends JpaRepository<CustomField, Long> {}
