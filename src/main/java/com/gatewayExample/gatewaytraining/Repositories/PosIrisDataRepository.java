package com.gatewayExample.gatewaytraining.Repositories;

        import com.gatewayExample.gatewaytraining.Entities.PosIrisData;
        import org.springframework.data.jpa.repository.JpaRepository;
        import org.springframework.stereotype.Repository;

@Repository
public interface PosIrisDataRepository extends JpaRepository<PosIrisData,Long> {
}
