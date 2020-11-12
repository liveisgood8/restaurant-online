package com.ro.analytics.data.repository;

import com.ro.analytics.data.model.DishOrdersStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishOrdersStatisticRepository extends JpaRepository<DishOrdersStatistic, Long> {
}
