package DataStructures;

import java.util.ArrayList;

import javastrava.api.v3.model.StravaMapPoint;

public class RAWSegment
	{
	// Atributos
	public int id;  // El id original del segmento
	public String name;  // El nombre original del segmento
	public StravaMapPoint startPoint;  // Punto final del segmento
	public ArrayList<Integer> athletesId;  // Los ids originales de los atletas del segmento
	
	// Constructor
	public RAWSegment(int id, String name)
		{
		this.id=id;
		this.name=name;
		athletesId=new ArrayList<Integer>();
		}
	
	public RAWSegment(int id, String name, StravaMapPoint startPoint)
		{
		this.id=id;
		this.name=name;
		this.startPoint=startPoint;
		athletesId=new ArrayList<Integer>();
		}
	}

