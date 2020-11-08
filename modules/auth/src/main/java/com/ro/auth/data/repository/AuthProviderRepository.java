package com.ro.auth.data.repository;

import com.ro.auth.data.model.AuthProvider;
import com.ro.core.data.repository.BaseEnumEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthProviderRepository extends BaseEnumEntityRepository<AuthProvider, Short> {
}
