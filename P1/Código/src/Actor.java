
/* Actor
 * 
 * Contiene la informaci�n b�sica de un actor. Su id y su nombre
 *  
 */

public class Actor
	{
	// Atributos
	public int id;  // El id original del actor sacado del XML
	public String nombre;  // El nombre del actor
	
	// Constructor
	public Actor(int id, String nombre)
		{
		this.id=id;
		this.nombre=nombre;
		}
	}
