package ar.com.atsa.persistence.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ar.com.atsa.persistence.entities.Persona;
import ar.com.atsa.persistence.entities.Tramite;
import ar.com.atsa.persistence.entities.Usuario;

public interface TramiteRepository extends BaseRepository<Tramite, Long> {
	
	List<Tramite> findByEstadoCodigoAndUsuario(String codigo, Usuario usuario, Pageable request);
	List<Tramite> findByEstadoCodigoAndUsuarioAndTipoCodigoIn(String codigo, Usuario usuario, Set<String> tipoCodigos, Pageable request);
	
	@Query("SELECT t FROM Tramite t WHERE t.estado.codigo = :estadoCodigo AND t.tipo.codigo IN (:tipoCodigos)")
	List<Tramite> findByEstadoCodigoAndTipoCodigo(@Param("estadoCodigo") String estadoCodigo, @Param("tipoCodigos") Set<String> tipoCodigos, Pageable request);
	
	List<Tramite> findByEstadoCodigoAndTipoTipoIn(String estadoCodigo, Integer[] tipo, Pageable request);
    
	List<Tramite> findByPersonaId(Long personaId);
    
	List<Tramite> findByPersonaIdAndEstadoCodigoAndData(Long personaId, String codigo, String data);
    
	List<Tramite> findByTipoCodigoAndEstadoCodigoAndData(String tipoCodigo, String estadoCodigo, String data);
    
	List<Tramite> findByPersonaIdAndTipoCodigoAndEstadoCodigo(Long id, String tipoCodigo, String estadoCodigo);
	
	Tramite findOneByPersonaAndTipoCodigo(Persona persona, String codigo);
	
	List<Tramite> findOneByPersonaAndTipoCodigoAndEstadoCodigo(Persona persona, String codigo, String estadoCodigo);
	
	List<Tramite> findByPersonaIdAndTipoCodigoAndEstadoCodigoOrderByIdDesc(Long personaId, String tipoCodigo, String estadoCodigo, Pageable pageable);
	
	List<Tramite> findByPersonaIdAndTipoTipoInAndEstadoCodigoOrderByIdDesc(Long personaId, Integer[] tipoTipo, String estadoCodigo, Pageable pageable);
	
	List<Tramite> findByPersonaIdAndTipoCodigoNotInOrderByIdDesc(Long personaId, String[] tiposCodigo, Pageable pageable);
	
	List<Tramite> findByPersonaIdAndTipoCodigoInOrderByIdDesc(Long personaId, String[] tiposCodigo, Pageable pageable);
	
	List<Tramite> findByPersonaIdAndTipoTipoInOrderByIdDesc(Long personaId, Integer[] tipoTipo, Pageable pageable);
}
