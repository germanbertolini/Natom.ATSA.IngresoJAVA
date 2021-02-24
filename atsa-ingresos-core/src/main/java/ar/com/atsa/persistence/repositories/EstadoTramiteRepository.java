package ar.com.atsa.persistence.repositories;

import ar.com.atsa.persistence.entities.EstadoTramite;

public interface EstadoTramiteRepository extends BaseRepository<EstadoTramite, Long> {
    
	public EstadoTramite findOneByCodigo(String codigo);
}
