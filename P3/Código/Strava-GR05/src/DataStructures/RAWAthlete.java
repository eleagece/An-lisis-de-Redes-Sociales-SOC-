package DataStructures;

import java.util.ArrayList;

public class RAWAthlete 
	{
	// Atributos
	public int id;  // El id original del atleta
	public String name;  // El nombre original del atleta
	public ArrayList<Integer> segmentsId;  // Los ids originales de los atletas del segmento
	
	// Constructor
	public RAWAthlete(int id, String name)
		{
		this.id=id;
		this.name=name;
		segmentsId=new ArrayList<Integer>();
		}
	}
