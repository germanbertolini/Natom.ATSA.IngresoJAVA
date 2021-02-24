package ar.com.atsa.persistence.repositories;

import java.util.List;

import ar.com.atsa.persistence.entities.Estudio;

public interface EstudioRepository extends BaseRepository<Estudio, Long> {
    
	List<Estudio> findByPersonaId(Long personaId);
}
