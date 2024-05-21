package com.healthscore.app.repository;

import com.healthscore.app.domain.Anuncio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Anuncio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnuncioRepository extends JpaRepository<Anuncio, Long>, JpaSpecificationExecutor<Anuncio> {}
