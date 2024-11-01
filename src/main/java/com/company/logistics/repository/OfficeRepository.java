package com.company.logistics.repository;

import com.company.logistics.model.company.Company;
import com.company.logistics.model.office.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Long> {
    List<Office> findByCompany(Company company);
}
