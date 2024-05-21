package com.healthscore.app.repository;

import com.healthscore.app.domain.ConsumoAgua;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConsumoAgua entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsumoAguaRepository extends JpaRepository<ConsumoAgua, Long>, JpaSpecificationExecutor<ConsumoAgua> {
    @Query("select consumoAgua from ConsumoAgua consumoAgua where consumoAgua.internalUser.login = ?#{authentication.name}")
    List<ConsumoAgua> findByInternalUserIsCurrentUser();
}
