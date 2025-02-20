package com.company.logistics.repository;

import com.company.logistics.model.entities.Company;
import com.company.logistics.model.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {
    List<Office> findByCompany(Company company);
}
