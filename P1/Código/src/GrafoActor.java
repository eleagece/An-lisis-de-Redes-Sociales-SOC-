
/* GrafoActor
 * 
 * Aquí se guarda la información necesaria para construir el grafo de actores relacionados
 * por películas
 *  
 */

public class GrafoActor
	{
	// Atributos
	public int numActores;
	public Actor actores[];  // Array de actores
	public int[][] relacionActores;  // Matriz de actores relacionados por películas
	
	// Constructor
	public GrafoActor(int numActores)
		{
		this.numActores=numActores;
		actores=new Actor[numActores];
		relacionActores=new int[numActores][numActores];
		for (int i=0; i<numActores; i++)
			for (int j=0; j<numActores; j++)
				{
				relacionActores[i][j]=0;  // Ningún actor está relacionado al inicio 
				}
		}
	
	// Métodos
	public void setPosicionMatriz(boolean b, int i, int j)
		{
		if (b)
			relacionActores[i][j]=1;
		else
			relacionActores[i][j]=0;
		}
	
	public void limpiaDuplicados()
		{
		for (int i=0; i<numActores; i++)
			for (int j=0; j<numActores; j++)
				if (i>=j)
					relacionActores[i][j]=0;  // Limpia los duplicados para que el grafo sea no dirigido
		}
	}