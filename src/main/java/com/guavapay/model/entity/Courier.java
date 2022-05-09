package com.guavapay.model.entity;

import com.guavapay.model.type.AvailabilityState;
import com.guavapay.model.type.CourierType;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courier")
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courier_seq")
    @SequenceGenerator(name = "courier_seq", sequenceName = "seq_courier", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CourierType type;

    @Enumerated(EnumType.STRING)
    private AvailabilityState availabilityState;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", unique = true)
    private Account account;

    @OneToMany(mappedBy = "courier", fetch = FetchType.LAZY)
    private List<CourierOrder> courierOrderList;
}
