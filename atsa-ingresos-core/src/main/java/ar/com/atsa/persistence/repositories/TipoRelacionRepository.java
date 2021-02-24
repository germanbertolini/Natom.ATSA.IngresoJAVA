package ar.com.atsa.persistence.repositories;

import ar.com.atsa.persistence.entities.TipoRelacion;

public interface TipoRelacionRepository extends BaseRepository<TipoRelacion, Long> {
    
	public TipoRelacion findOneByNombre(String nombre); 
	
}
