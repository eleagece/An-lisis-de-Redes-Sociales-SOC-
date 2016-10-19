
/* RAWdata
 * 
 * Aquí se guarda la información de una película según se saca del XML. Es detalle:
 * en el objeto 'pelicula' guardamos el id y el nombre de la película. En el 
 * ArrrayList de acotres 'actores' guardamos objetos Actor (que tienen id del actor
 * y su nombre). Hay tantos objetos Actor como actores hay en la película 'pelicula'
 * 
 */

import java.util.ArrayList;

public class RAWdata 
	{
	// Atributos
	public Pelicula pelicula;
	public ArrayList<Actor> actores;
	
	// Constructor
	public RAWdata()
		{

		}
	}
