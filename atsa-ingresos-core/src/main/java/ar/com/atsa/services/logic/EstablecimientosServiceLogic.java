package ar.com.atsa.services.logic;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import ar.com.atsa.persistence.entities.Establecimiento;
import ar.com.atsa.persistence.repositories.EstablecimientoRepository;

@Component
public class EstablecimientosServiceLogic {
	
	private static final Integer PAGE_SIZE = 10;

	@Autowired
	EstablecimientoRepository establecimientoRepository;
	
	/**
	 * BÚSQUEDA DE AFILIADOS POR CUIL
	 * 
	 * @param cuil
	 * @return
	 */
	public Iterable<Establecimiento> findAll(Integer page, String nombre, String cuil) {
		PageRequest pageRequest = new PageRequest(page, PAGE_SIZE, Direction.ASC, "nombre");
		return this.establecimientoRepository.findByNombreContainingAndCuitPageable(nombre, cuil, pageRequest);
	}
	
	public Iterable<Establecimiento> findByNombreAndCuit(String nombre, String cuit) {
		if (cuit!=null && cuit.trim().length() < 1) cuit = null; 
		return this.establecimientoRepository.findByNombreContainingAndCuit(nombre, cuit);
	}
	
	@Transactional
	public Establecimiento saveEstablecimiento(final Establecimiento in) {
		return this.establecimientoRepository.save(in);
	}
	
	public Establecimiento findById(final Long id) {
		return this.establecimientoRepository.findOne(id);
	}
	
	public void eliminarEstablecimiento(final Long id) {
		this.establecimientoRepository.delete(id);
	}
}
