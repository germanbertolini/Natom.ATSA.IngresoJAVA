package ar.com.atsa.persistence.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ar.com.atsa.persistence.entities.Establecimiento;

public interface EstablecimientoRepository extends BaseRepository<Establecimiento, Long> {
    
	@Query("SELECT e from Establecimiento e where activo = true and (:nombre is null or e.nombre LIKE CONCAT('%',:nombre,'%')) and (e.cuit = :cuit or :cuit is null)")
	public List<Establecimiento> findByNombreContainingAndCuit(@Param("nombre") String nombre, @Param("cuit") String cuit);
    
	@Query("SELECT e from Establecimiento e where (:nombre is null or e.nombre LIKE CONCAT('%',:nombre,'%')) and (e.cuit = :cuit or :cuit is null)")
	public List<Establecimiento> findByNombreContainingAndCuitPageable(@Param("nombre") String nombre, @Param("cuit") String cuit, Pageable pageable);
}
