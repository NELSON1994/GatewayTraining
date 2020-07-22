package com.gatewayExample.gatewaytraining.Repositories;

import com.gatewayExample.gatewaytraining.Entities.AllTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllTransactionRepository extends JpaRepository<AllTransactions,Long> {

}
