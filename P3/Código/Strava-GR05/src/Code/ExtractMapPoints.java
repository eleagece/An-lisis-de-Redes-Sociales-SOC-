package Code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import DataStructures.CityPoints;

public class ExtractMapPoints {
	public File file;
	public Scanner sc;
	public String nombreFichero; 
	ArrayList<CityPoints> arrayCityPoints;
	
	public ExtractMapPoints(String nombreFichero) throws FileNotFoundException{
		this.nombreFichero = nombreFichero;
		this.file = new File(this.nombreFichero);
		this.sc = new Scanner(file);
	}
	
	/*
	 * almacenamos en un array de CityPoints tantos objetos de esa clase como lineas con datos
	 * de las ciudades tengamos en el fichero
	 */
	public ArrayList<CityPoints> extractInfoCityPoints(){
		arrayCityPoints = new ArrayList<CityPoints>();
		String separadores = "[ ]";
		
		while(this.sc.hasNextLine()){
			String lineaCityPoints = sc.nextLine();
			String[] arrayTokensCityPoints = lineaCityPoints.split(separadores);
			String ciudad = arrayTokensCityPoints[0];
			String laSW = arrayTokensCityPoints[1];
			String loSW = arrayTokensCityPoints[2];
			String laNE = arrayTokensCityPoints[3];
			String loNE = arrayTokensCityPoints[4];
			
			CityPoints cityPoints = new CityPoints(ciudad, laSW, loSW, laNE, loNE);
			arrayCityPoints.add(cityPoints);
		}
		
		return arrayCityPoints;
		
	}
}

