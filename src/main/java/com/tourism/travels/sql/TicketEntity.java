package com.tourism.travels.sql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "ticket")
public class TicketEntity {

    @Id
    @Column(name = "ticket_id")
    private int ticketId;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "package_id")
    private String packageId;

    @Column(name = "travel_date")
    private LocalDate travelDate;

    @Column(name = "total_members")
    private int totalMembers;

    @ManyToOne
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private CustomerEntity customerEntity;

    @ManyToOne
    @JoinColumn(name = "package_id", insertable = false, updatable = false)
    private PackageEntity packageEntity;

}
