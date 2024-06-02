package com.healthscore.app.repository;

import com.healthscore.app.domain.PontuacaoUsuario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PontuacaoUsuario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PontuacaoUsuarioRepository extends JpaRepository<PontuacaoUsuario, Long>, JpaSpecificationExecutor<PontuacaoUsuario> {}
