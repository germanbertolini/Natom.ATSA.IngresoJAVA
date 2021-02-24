package ar.com.atsa.persistence.repositories;

import java.util.List;

import ar.com.atsa.persistence.entities.Configuracion;

public interface ConfiguracionRepository extends BaseRepository<Configuracion, Long> {
    
	public List<Configuracion> findAll();
	
	public Configuracion findOneByNombre(String nombre);
	
}
