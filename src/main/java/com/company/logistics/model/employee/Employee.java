package com.company.logistics.model.employee;

import com.company.logistics.model.company.Company;
import com.company.logistics.utils.EmployeeRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employee")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String secondName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private EmployeeRole role;

    @Column(nullable = false, unique = true)
    private String egn;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
}
