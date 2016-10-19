
/* GrafoPelicula
 * 
 * Aquí se guarda la información necesaria para construir el grafo de películas relacionadas
 * por actores
 *  
 */

public class GrafoPelicula 
	{
	// Atributos
	public int numPeliculas;
	public Pelicula peliculas[];  // Array de películas
	public int[][] relacionPeliculas;  // Matriz de películas relacionadas por actores
	
	// Constructor
	public GrafoPelicula(int numPeliculas)
		{
		this.numPeliculas=numPeliculas;
		peliculas=new Pelicula[numPeliculas];
		relacionPeliculas=new int[numPeliculas][numPeliculas];
		for (int i=0; i<numPeliculas; i++)
			for (int j=0; j<numPeliculas; j++)
				{
				relacionPeliculas[i][j]=0;  // Ninguna película está relacionada al inicio 
				}
		}
	
	// Métodos
	public void setPosicionMatriz(boolean b, int i, int j)
		{
		if (b)
			relacionPeliculas[i][j]=1;
		else
			relacionPeliculas[i][j]=0;
		}
	}