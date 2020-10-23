package com.ro.orders.repository;

import com.ro.core.repository.BaseEnumEntityRepository;
import com.ro.orders.model.PaymentMethod;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends BaseEnumEntityRepository<PaymentMethod, Short> {

}
