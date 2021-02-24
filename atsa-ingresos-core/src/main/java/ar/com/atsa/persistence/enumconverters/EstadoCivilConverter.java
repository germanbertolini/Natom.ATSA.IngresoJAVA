package ar.com.atsa.persistence.enumconverters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import ar.com.atsa.commons.enums.EstadoCivil;

@Converter(autoApply = true)
public class EstadoCivilConverter implements AttributeConverter<EstadoCivil, String> {

    @Override
    public String convertToDatabaseColumn(EstadoCivil estadoCivil) {
        switch (estadoCivil){
            case SOLTERO: return "S";
            case CASADO: return "C";
            case VIUDO: return "V";
            case DIVORCIADO: return "D";
            case SEPARADO: return "P";
            case CONCUBINATO: return "U";
            case SIN_DATOS: return "N";
            default: throw new IllegalArgumentException("Unknown estado civil: " + estadoCivil);
        }
    }

    @Override
    public EstadoCivil convertToEntityAttribute(String dbData) {
    	if (dbData != null) {
	        switch (dbData){
	            case "S": return EstadoCivil.SOLTERO;
	            case "C": return EstadoCivil.CASADO;
	            case "V": return EstadoCivil.VIUDO;
	            case "D": return EstadoCivil.DIVORCIADO;
	            case "P": return EstadoCivil.SEPARADO;
	            case "U": return EstadoCivil.CONCUBINATO;
	            case "N": return EstadoCivil.SIN_DATOS;
	            default: throw new IllegalArgumentException("Unknown estado civil: " + dbData);
	        }
    	} else {
    		return null;
    	}
    }
}
