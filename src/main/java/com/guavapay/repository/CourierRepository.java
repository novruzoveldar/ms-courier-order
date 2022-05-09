package com.guavapay.repository;

import com.guavapay.model.entity.Courier;
import com.guavapay.model.type.AvailabilityState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Long> {

    @Query("FROM Courier c WHERE c.account.id = :accountId")
    Courier getCourierByAccountId(@Param(value = "accountId") Long accountId);

    @Query("FROM Courier c JOIN FETCH c.account a WHERE c.availabilityState in (:states)")
    List<Courier> getCourierByState(@Param(value = "states") List<AvailabilityState> availabilityState);
}
