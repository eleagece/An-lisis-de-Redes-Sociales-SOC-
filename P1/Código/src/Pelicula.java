
/* Pel�cula
 * 
 * Contiene la informaci�n b�sica de una pel�cula. Su id y su nombre
 *  
 */

public class Pelicula 
	{
	// Atributos
	public int id;  // El id original de la pel�cula sacado del XML
	public String nombre;  // El nombre de la pel�cula
	
	// Constructor
	public Pelicula(int id, String nombre)
		{
		this.id=id;
		this.nombre=nombre;
		}
	}