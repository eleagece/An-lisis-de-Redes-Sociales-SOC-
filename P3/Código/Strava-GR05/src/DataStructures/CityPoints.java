package DataStructures;

public class CityPoints {
	
	private String ciudad;
	private String laSW;
	private String loSW;
	private String laNE;
	private String loNE;
	
	/*
	 * Constructor de la clase CityPoints que almacena las coordenadas de la ciudad y su nombre 
	 */
	public CityPoints(String ciudad, String lasw, String losw, String lane, String lone){
		this.ciudad = ciudad;
		this.laSW = lasw;
		this.loSW = losw;
		this.laNE = lane;
		this.loNE = lone;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getLaSW() {
		return laSW;
	}

	public void setLaSW(String laSW) {
		this.laSW = laSW;
	}

	public String getLoSW() {
		return loSW;
	}

	public void setLoSW(String loSW) {
		this.loSW = loSW;
	}

	public String getLaNE() {
		return laNE;
	}

	public void setLaNE(String laNE) {
		this.laNE = laNE;
	}

	public String getLoNE() {
		return loNE;
	}

	public void setLoNE(String loNE) {
		this.loNE = loNE;
	}
	
	
}
