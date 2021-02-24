package ar.com.atsa.persistence.repositories;

import java.util.List;

import ar.com.atsa.persistence.entities.DocumentoArchivado;
import ar.com.atsa.persistence.entities.Persona;
import ar.com.atsa.persistence.entities.Tramite;

public interface DocumentoArchivadoRepository extends BaseRepository<DocumentoArchivado, Long> {
    
	public List<DocumentoArchivado> findByPersonaAndActualAndActivo(Persona p, Boolean actual, Boolean activo);
	
	public DocumentoArchivado findOneByTramite(Tramite tramite);
}
