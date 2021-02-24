package ar.com.atsa.commons.enums;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Created by tomas.colombo on 06/03/14.
 */
@JsonRootName("Sexo")
public enum NivelEstudio {
    PRIMARIO,
    SECUNDARIO,
    TERCIARIO,
    UNIVERSITARIO;
}
