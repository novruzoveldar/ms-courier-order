package com.guavapay.repository;

import com.guavapay.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("FROM Account a JOIN FETCH a.roles r WHERE a.mobile =:phone")
    Optional<Account> findAccountByPhone(@Param(value = "phone") String phone);

}
