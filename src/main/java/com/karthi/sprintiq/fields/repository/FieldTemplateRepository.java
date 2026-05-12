package com.karthi.sprintiq.fields.repository;

import com.karthi.sprintiq.fields.entity.FieldTemplate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldTemplateRepository extends JpaRepository<FieldTemplate, Long> {

    Optional<FieldTemplate> findByTemplateKey(String templateKey);

    Optional<FieldTemplate> findByDefaultTemplateTrue();

    List<FieldTemplate> findByActiveTrue();
}
