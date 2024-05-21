package com.healthscore.app.repository;

import com.healthscore.app.domain.ConsultaEspecialista;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConsultaEspecialista entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsultaEspecialistaRepository
    extends JpaRepository<ConsultaEspecialista, Long>, JpaSpecificationExecutor<ConsultaEspecialista> {
    @Query(
        "select consultaEspecialista from ConsultaEspecialista consultaEspecialista where consultaEspecialista.internalUser.login = ?#{authentication.name}"
    )
    List<ConsultaEspecialista> findByInternalUserIsCurrentUser();
}
