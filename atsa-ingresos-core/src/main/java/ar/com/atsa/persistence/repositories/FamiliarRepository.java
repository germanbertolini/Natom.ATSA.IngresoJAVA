package ar.com.atsa.persistence.repositories;

import java.util.Date;
import java.util.List;

import ar.com.atsa.persistence.entities.Familiar;

public interface FamiliarRepository extends BaseRepository<Familiar, Long> {
    
	List<Familiar> findByAfiliadoId(Long personaId);
	
	public Familiar findOneByFamiliarId(Long familiarId);
	
	public List<Familiar> findByFamiliarFechaNacimientoLessThanAndRelacionNombre(Date fecha, String relacion);
}
