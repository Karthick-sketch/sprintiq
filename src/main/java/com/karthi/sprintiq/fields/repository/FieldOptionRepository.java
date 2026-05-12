package com.karthi.sprintiq.fields.repository;

import com.karthi.sprintiq.fields.entity.FieldOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldOptionRepository extends JpaRepository<FieldOption, Long> {

    List<FieldOption> findByFieldIdOrderBySortOrderAsc(Long fieldId);

    List<FieldOption> findByFieldIdAndActiveTrue(Long fieldId);
}
