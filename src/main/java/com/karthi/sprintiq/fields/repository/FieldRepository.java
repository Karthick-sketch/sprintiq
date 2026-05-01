package com.karthi.sprintiq.fields.repository;

import com.karthi.sprintiq.fields.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {}
