package com.healthscore.app.repository;

import com.healthscore.app.domain.ControleMedicamentos;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ControleMedicamentos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ControleMedicamentosRepository
    extends JpaRepository<ControleMedicamentos, Long>, JpaSpecificationExecutor<ControleMedicamentos> {
    @Query(
        "select controleMedicamentos from ControleMedicamentos controleMedicamentos where controleMedicamentos.internalUser.login = ?#{authentication.name}"
    )
    List<ControleMedicamentos> findByInternalUserIsCurrentUser();
}
