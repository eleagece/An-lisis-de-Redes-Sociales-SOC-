
/* GrafoPelicula
 * 
 * Aqu� se guarda la informaci�n necesaria para construir el grafo de pel�culas relacionadas
 * por actores
 *  
 */

public class GrafoPelicula 
	{
	// Atributos
	public int numPeliculas;
	public Pelicula peliculas[];  // Array de pel�culas
	public int[][] relacionPeliculas;  // Matriz de pel�culas relacionadas por actores
	
	// Constructor
	public GrafoPelicula(int numPeliculas)
		{
		this.numPeliculas=numPeliculas;
		peliculas=new Pelicula[numPeliculas];
		relacionPeliculas=new int[numPeliculas][numPeliculas];
		for (int i=0; i<numPeliculas; i++)
			for (int j=0; j<numPeliculas; j++)
				{
				relacionPeliculas[i][j]=0;  // Ninguna pel�cula est� relacionada al inicio 
				}
		}
	
	// M�todos
	public void setPosicionMatriz(boolean b, int i, int j)
		{
		if (b)
			relacionPeliculas[i][j]=1;
		else
			relacionPeliculas[i][j]=0;
		}
	}