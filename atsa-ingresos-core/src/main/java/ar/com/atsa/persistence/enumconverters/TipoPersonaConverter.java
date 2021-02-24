package ar.com.atsa.persistence.enumconverters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ar.com.atsa.commons.enums.TipoPersona;

/**
 * Created by tomas.colombo on 06/03/14.
 */
@Converter(autoApply = true)
public class TipoPersonaConverter implements AttributeConverter<TipoPersona, String> {

    @Override
    public String convertToDatabaseColumn(TipoPersona tipoPersona) {
        switch (tipoPersona){
            case AFILIADO: return "A";
            case FAMILIAR: return "F";
            case AFILIADO_PASIVO: return "P";
            default: throw new IllegalArgumentException("Unknown tipo persona: " + tipoPersona);
        }
    }

    @Override
    public TipoPersona convertToEntityAttribute(String dbData) {
        switch (dbData){
            case "A": return TipoPersona.AFILIADO;
            case "F": return TipoPersona.FAMILIAR;
            case "P": return TipoPersona.AFILIADO_PASIVO;
            default: throw new IllegalArgumentException("Unknown tipo persona: " + dbData);
        }
    }
}
