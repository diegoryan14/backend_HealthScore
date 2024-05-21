package com.healthscore.app.repository;

import com.healthscore.app.domain.QualidadeSono;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QualidadeSono entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QualidadeSonoRepository extends JpaRepository<QualidadeSono, Long>, JpaSpecificationExecutor<QualidadeSono> {
    @Query("select qualidadeSono from QualidadeSono qualidadeSono where qualidadeSono.internalUser.login = ?#{authentication.name}")
    List<QualidadeSono> findByInternalUserIsCurrentUser();
}
