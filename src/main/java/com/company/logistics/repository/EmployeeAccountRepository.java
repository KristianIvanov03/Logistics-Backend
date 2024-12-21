package com.company.logistics.repository;

import com.company.logistics.model.company.Company;
import com.company.logistics.model.employeeaccaunts.EmployeeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeAccountRepository extends JpaRepository<EmployeeAccount, Long> {
    Optional<EmployeeAccount> findByName(String name);
}
