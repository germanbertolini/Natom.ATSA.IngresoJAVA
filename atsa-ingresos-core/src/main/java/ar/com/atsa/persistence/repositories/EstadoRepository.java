package ar.com.atsa.persistence.repositories;

import ar.com.atsa.persistence.entities.Estado;

public interface EstadoRepository extends BaseRepository<Estado, Long> {
    
	public Estado findOneByCodigo(String codigo);
}
