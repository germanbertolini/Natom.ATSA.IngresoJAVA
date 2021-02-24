package ar.com.atsa.commons.enums;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("Sexo")
public enum TipoPersona {
	AFILIADO,
	FAMILIAR,
	AFILIADO_PASIVO;
}
