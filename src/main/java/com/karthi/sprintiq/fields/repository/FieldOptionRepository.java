package com.karthi.sprintiq.fields.repository;

import com.karthi.sprintiq.fields.dto.FilterFieldOptionDTO;
import com.karthi.sprintiq.fields.entity.FieldOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldOptionRepository extends JpaRepository<FieldOption, Long> {

  List<FieldOption> findByFieldIdOrderBySortOrderAsc(Long fieldId);

  List<FieldOption> findByFieldIdAndActiveTrue(Long fieldId);

  @Query(
      value = "select f.id, f.label FROM field_options f WHERE f.field_id = :fieldId",
      nativeQuery = true
  )
  List<FilterFieldOptionDTO> findFieldOptionByFieldId(@Param("fieldId") Long fieldId);
}
