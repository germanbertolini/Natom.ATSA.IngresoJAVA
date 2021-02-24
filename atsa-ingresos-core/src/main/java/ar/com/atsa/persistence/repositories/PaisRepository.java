package ar.com.atsa.persistence.repositories;

import java.util.List;

import ar.com.atsa.persistence.entities.Pais;

public interface PaisRepository extends BaseRepository<Pais, Long> {
    
	public List<Pais> findAll();
	
}
