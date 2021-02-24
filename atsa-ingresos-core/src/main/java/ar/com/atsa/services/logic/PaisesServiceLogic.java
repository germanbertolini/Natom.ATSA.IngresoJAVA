package ar.com.atsa.services.logic;

import java.util.List;






import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ar.com.atsa.commons.dto.PaisTO;
import ar.com.atsa.persistence.entities.Pais;
import ar.com.atsa.persistence.repositories.PaisRepository;
import ar.com.atsa.services.ServiceException;

@Component
public class PaisesServiceLogic {

	@Autowired
	PaisRepository paisRepository;
	
	/**
	 * BÚSQUEDA DE PAISES
	 * 
	 * @return
	 */
	public List<Pais> findAll() {
		return this.paisRepository.findAll();
	}
	
	/**
	 * OBTENCIÓN DE DETALLES DE PAIS
	 * 
	 * @param paisId
	 * @return
	 */
	public PaisTO getPais(final Long paisId) {
		PaisTO to =  new PaisTO();
		Pais pais = this.paisRepository.findOne(paisId);
		
		to.setPais(pais);
				
		return to;
	}
	
	/**
	 * GUARDAR PAIS
	 * 
	 * @param in
	 * @return
	 * @throws ServiceException 
	 */
	@Transactional
	public PaisTO savePais(final PaisTO in) throws ServiceException {
		Pais pais = in.getPais();

		try {
			this.paisRepository.save(pais);
		} catch (Exception e) {
			throw new ServiceException(e.getMessage(), e);
		}

		return this.getPais(pais.getId());
	}
}
