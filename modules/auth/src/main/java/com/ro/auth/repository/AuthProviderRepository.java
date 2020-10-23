package com.ro.auth.repository;

import com.ro.auth.model.AuthProvider;
import com.ro.core.repository.BaseEnumEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthProviderRepository extends BaseEnumEntityRepository<AuthProvider, Short> {
}
