package com.guavapay.model.entity;

import com.guavapay.model.converter.AtomicDecimalConverter;
import com.guavapay.model.type.DeliveryState;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courier_order")
public class CourierOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "courier_order_seq")
    @SequenceGenerator(name = "courier_order_seq", sequenceName = "seq_courier_order", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true, columnDefinition = "CHAR(36)")
    private String deliveryOrderRrn;

    @Column(nullable = false)
    private String deliverAddress;

    @Column(name = "amount", nullable = false)
    @Convert(converter = AtomicDecimalConverter.class)
    private AtomicReference<BigDecimal> deliverAmount;

    @Column(nullable = false)
    private Date deliveryDate;

    private Date routeBeginDate;

    private Date routeStopDate;

    @Column(nullable = false)
    private BigDecimal distance;

    @Column(nullable = false)
    private String unitOfMeasurement;

    @Enumerated(EnumType.STRING)
    private DeliveryState state;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parcel_id", nullable = false, updatable = false)
    private Parcel parcel;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "courier_id")
    private Courier courier;
}
