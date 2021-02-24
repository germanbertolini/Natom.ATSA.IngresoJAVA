package ar.com.atsa.persistence.repositories;

import java.util.List;

import ar.com.atsa.commons.enums.TipoPersona;
import ar.com.atsa.persistence.entities.Persona;

public interface PersonaRepository extends BaseRepository<Persona, Long> {
    
	public List<Persona> findByCuilAndTipoPersona(String cuil, TipoPersona tipoPersona);
    
	public List<Persona> findByCuil(String cuil);
	
	public List<Persona> findByDocumentoAndTipoPersona(String documento, TipoPersona tipoPersona);
	
	public List<Persona> findByNumeroAfiliadoAndTipoPersonaIn(String numeroAfiliado, TipoPersona[] tipoPersona);
	
	public List<Persona> findByDocumento(String documento);
	
	public Persona findOneByDocumento(String documento);
}
