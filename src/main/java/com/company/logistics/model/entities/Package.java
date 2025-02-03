package com.company.logistics.model.entities;

import com.company.logistics.model.enums.DeliveryType;
import com.company.logistics.model.enums.PackageStatus;
import com.company.logistics.model.enums.ShippingMethod;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "package")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    @JsonBackReference
    private Company company;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonBackReference
    private ClientAccount senderId;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    @JsonBackReference
    private ClientAccount receiverId;

    private String deliveryAddress;

    @ManyToOne
    @JoinColumn(name = "sender_office_id", nullable = false)
    @JsonBackReference
    private Office senderOffice;

    @ManyToOne
    @JoinColumn(name = "receiver_office_id")
    @JsonBackReference
    private Office recieverOffice;

    private Double weight;

    private Boolean isDelivered;

    private Boolean isPaid;

    private LocalDate deliveryDate;

    private LocalDateTime registrationDate;

    @ManyToOne
    @JoinColumn(name = "registered_by_employee_id", nullable = false)
    private EmployeeAccount registeredByEmployee;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PackageStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryType deliveryType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShippingMethod shippingMethod;
}
