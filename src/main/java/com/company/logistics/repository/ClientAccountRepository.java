package com.company.logistics.repository;

import com.company.logistics.model.entities.ClientAccount;
import com.company.logistics.model.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientAccountRepository extends JpaRepository<ClientAccount, Long> {
    Optional<ClientAccount> findByName(String name);
    List<ClientAccount> findAllByCompanyId(Company company);
}
