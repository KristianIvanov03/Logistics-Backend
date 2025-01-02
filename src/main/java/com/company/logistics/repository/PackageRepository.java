package com.company.logistics.repository;

import com.company.logistics.model.entities.Employee;
import com.company.logistics.model.entities.EmployeeAccount;
import com.company.logistics.model.entities.Package;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long> {
    List<Package> findAllByRegisteredByEmployee(EmployeeAccount employee);
}
