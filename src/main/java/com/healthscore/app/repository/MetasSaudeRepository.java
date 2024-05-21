package com.healthscore.app.repository;

import com.healthscore.app.domain.MetasSaude;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MetasSaude entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MetasSaudeRepository extends JpaRepository<MetasSaude, Long>, JpaSpecificationExecutor<MetasSaude> {
    @Query("select metasSaude from MetasSaude metasSaude where metasSaude.internalUser.login = ?#{authentication.name}")
    List<MetasSaude> findByInternalUserIsCurrentUser();
}
