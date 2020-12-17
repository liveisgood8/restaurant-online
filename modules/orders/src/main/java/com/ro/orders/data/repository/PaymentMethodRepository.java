package com.ro.orders.data.repository;

import com.ro.core.data.repository.BaseEnumEntityRepository;
import com.ro.orders.data.model.PaymentMethod;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends BaseEnumEntityRepository<PaymentMethod, Short> {

}
