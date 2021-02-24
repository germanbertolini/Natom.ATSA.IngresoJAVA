package ar.com.atsa.persistence.repositories;

import ar.com.atsa.persistence.entities.TipoTramite;

public interface TipoTramiteRepository extends BaseRepository<TipoTramite, Long> {
    
	public TipoTramite findOneByCodigo(String codigo);
}
