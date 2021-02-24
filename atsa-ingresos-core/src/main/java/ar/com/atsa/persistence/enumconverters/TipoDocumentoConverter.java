package ar.com.atsa.persistence.enumconverters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ar.com.atsa.commons.enums.TipoDocumento;

/**
 * Created by tomas.colombo on 06/03/14.
 */
@Converter(autoApply = true)
public class TipoDocumentoConverter implements AttributeConverter<TipoDocumento, String> {

    @Override
    public String convertToDatabaseColumn(TipoDocumento tipoDocumento) {
        switch (tipoDocumento){
            case DNI: return "D";
            case CEDULA: return "C";
            case PASAPORTE: return "P";
            default: throw new IllegalArgumentException("Unknown tipo documento: " + tipoDocumento);
        }
    }

    @Override
    public TipoDocumento convertToEntityAttribute(String dbData) {
        switch (dbData){
            case "D": return TipoDocumento.DNI;
            case "C": return TipoDocumento.CEDULA;
            case "P": return TipoDocumento.PASAPORTE;
            default: throw new IllegalArgumentException("Unknown tipo documento: " + dbData);
        }
    }
}
