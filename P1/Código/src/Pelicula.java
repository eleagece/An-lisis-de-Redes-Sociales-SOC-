
/* Película
 * 
 * Contiene la información básica de una película. Su id y su nombre
 *  
 */

public class Pelicula 
	{
	// Atributos
	public int id;  // El id original de la película sacado del XML
	public String nombre;  // El nombre de la película
	
	// Constructor
	public Pelicula(int id, String nombre)
		{
		this.id=id;
		this.nombre=nombre;
		}
	}