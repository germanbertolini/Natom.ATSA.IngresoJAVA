package ar.com.atsa.persistence.repositories;

import java.util.List;

import ar.com.atsa.persistence.entities.Localidad;

public interface LocalidadRepository extends BaseRepository<Localidad, Long> {
    
	public List<Localidad> findAll();
	
	public Localidad findOneByNombre(String nombre);
}
