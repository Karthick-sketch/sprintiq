package com.karthi.sprintiq.fields.repository;

import com.karthi.sprintiq.fields.entity.Field;
import com.karthi.sprintiq.fields.enums.FieldKind;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends JpaRepository<Field, Long> {

    Optional<Field> findBySystemKey(String systemKey);

    List<Field> findByFieldKindAndActiveTrue(FieldKind fieldKind);

    List<Field> findByActiveTrue();

    boolean existsBySystemKey(String systemKey);
}
