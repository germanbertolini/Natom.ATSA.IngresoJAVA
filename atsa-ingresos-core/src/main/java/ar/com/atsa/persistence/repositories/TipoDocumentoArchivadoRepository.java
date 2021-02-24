package ar.com.atsa.persistence.repositories;

import ar.com.atsa.persistence.entities.TipoDocumentoArchivado;

public interface TipoDocumentoArchivadoRepository extends BaseRepository<TipoDocumentoArchivado, Long> {
    
	public TipoDocumentoArchivado findOneByCodigo(String codigo);
}
