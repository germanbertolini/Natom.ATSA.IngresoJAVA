package ar.com.atsa.persistence.enumconverters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ar.com.atsa.commons.enums.NivelEstudio;
import ar.com.atsa.commons.enums.TipoPersona;

/**
 * Created by tomas.colombo on 06/03/14.
 */
@Converter(autoApply = true)
public class NivelEstudioConverter implements AttributeConverter<NivelEstudio, String> {

    @Override
    public String convertToDatabaseColumn(NivelEstudio tipoPersona) {
        switch (tipoPersona){
            case PRIMARIO: return "P";
            case SECUNDARIO: return "S";
            case TERCIARIO: return "T";
            case UNIVERSITARIO: return "U";
            default: throw new IllegalArgumentException("Unknown nivel: " + tipoPersona);
        }
    }

    @Override
    public NivelEstudio convertToEntityAttribute(String dbData) {
        switch (dbData){
            case "P": return NivelEstudio.PRIMARIO;
            case "S": return NivelEstudio.SECUNDARIO;
            case "T": return NivelEstudio.TERCIARIO;
            case "U": return NivelEstudio.UNIVERSITARIO;
            default: throw new IllegalArgumentException("Unknown nivel: " + dbData);
        }
    }
}
