package ar.com.atsa.commons.enums;

/**
 * 
 * @author diegap
 *
 */
public enum AppDefaultSettings {

	MAX_ALLOWED_FAILED_ATTEMPTS(5);
	
	private int value;
	
	private AppDefaultSettings(int value){
		this.setValue(value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
}
