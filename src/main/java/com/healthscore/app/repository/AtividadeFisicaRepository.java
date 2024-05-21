package com.healthscore.app.repository;

import com.healthscore.app.domain.AtividadeFisica;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AtividadeFisica entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AtividadeFisicaRepository extends JpaRepository<AtividadeFisica, Long>, JpaSpecificationExecutor<AtividadeFisica> {
    @Query("select atividadeFisica from AtividadeFisica atividadeFisica where atividadeFisica.internalUser.login = ?#{authentication.name}")
    List<AtividadeFisica> findByInternalUserIsCurrentUser();
}
