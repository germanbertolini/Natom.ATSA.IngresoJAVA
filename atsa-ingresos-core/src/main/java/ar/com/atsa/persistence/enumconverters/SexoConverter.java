package ar.com.atsa.persistence.enumconverters;

import ar.com.atsa.commons.enums.Sexo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by tomas.colombo on 06/03/14.
 */
@Converter(autoApply = true)
public class SexoConverter implements AttributeConverter<Sexo, String> {

    @Override
    public String convertToDatabaseColumn(Sexo sexo) {
        switch (sexo){
            case MASCULINO: return "M";
            case FEMENINO: return "F";
            default: throw new IllegalArgumentException("Unknown sex: " + sexo);
        }
    }

    @Override
    public Sexo convertToEntityAttribute(String dbData) {
        switch (dbData){
            case "M": return Sexo.MASCULINO;
            case "F": return Sexo.FEMENINO;
            default: throw new IllegalArgumentException("Unknown sex: " + dbData);
        }
    }
}
