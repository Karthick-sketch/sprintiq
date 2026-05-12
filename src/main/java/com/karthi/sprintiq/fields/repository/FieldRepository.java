package com.karthi.sprintiq.fields.repository;

import com.karthi.sprintiq.fields.entity.FieldDefinition;
import com.karthi.sprintiq.fields.enums.FieldKind;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<FieldDefinition, Long> {

    Optional<FieldDefinition> findBySystemKey(String systemKey);

    List<FieldDefinition> findByFieldKindAndActiveTrue(FieldKind fieldKind);

    List<FieldDefinition> findByActiveTrue();

    boolean existsBySystemKey(String systemKey);
}
