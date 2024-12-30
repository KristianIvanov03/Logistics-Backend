package com.company.logistics.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "offices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Office {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String address;

    @Column(nullable = false)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @JsonBackReference
    private Company company;

    @OneToMany(mappedBy = "senderOffice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Package> sentPackages;

    @OneToMany(mappedBy = "recieverOffice", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Package> receivedPackages;
}
