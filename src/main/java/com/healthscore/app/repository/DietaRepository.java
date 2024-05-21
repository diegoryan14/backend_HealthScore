package com.healthscore.app.repository;

import com.healthscore.app.domain.Dieta;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Dieta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DietaRepository extends JpaRepository<Dieta, Long>, JpaSpecificationExecutor<Dieta> {
    @Query("select dieta from Dieta dieta where dieta.internalUser.login = ?#{authentication.name}")
    List<Dieta> findByInternalUserIsCurrentUser();
}
