package com.ro.analytics.data.repository;

import com.ro.analytics.data.model.DishEmotionsStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishEmotionsStatisticRepository extends JpaRepository<DishEmotionsStatistic, Long> {
  @EntityGraph(attributePaths = {"dish.category"})
  @Override
  Page<DishEmotionsStatistic> findAll(Pageable pageable);
}
