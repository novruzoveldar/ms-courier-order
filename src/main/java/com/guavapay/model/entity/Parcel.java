package com.guavapay.model.entity;

import com.guavapay.model.type.ParcelState;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "parcel")
public class Parcel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parcel_seq")
    @SequenceGenerator(name = "parcel_seq", sequenceName = "seq_parcel", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ParcelState state;

    @Column(nullable = false)
    private Date parcelPlaced;

    @Column(nullable = false)
    private Date parcelDelivered;

    @Column(nullable = false)
    private Date updateDate;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    private Account account;
}
