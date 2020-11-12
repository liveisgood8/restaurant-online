package com.ro.analytics.data.repository;

import com.ro.analytics.data.model.DishEmotionsStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishEmotionsStatisticRepository extends JpaRepository<DishEmotionsStatistic, Long> {
}
