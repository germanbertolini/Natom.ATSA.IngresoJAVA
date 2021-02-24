package ar.com.atsa.persistence.repositories;

import ar.com.atsa.persistence.entities.Usuario;

public interface UsuarioRepository extends BaseRepository<Usuario, Long> {
    
	Usuario findByEmail(String email);
	
	Usuario findById(long id);
}
