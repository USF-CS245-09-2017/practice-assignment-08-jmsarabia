import java.util.*;
public class GraphAdjMatrix implements Graph{

	//UNWEIGHTED, DIRECTED
	private Edge[][] edges;
	private int numVertices;

	public GraphAdjMatrix(int vertices)
	{
		numVertices = vertices;
		edges = new Edge[vertices][vertices];
		//initializing edges graph with cost = 0
		for(int i = 0; i < vertices; i++)
		{
			for (int j = 0; j < vertices; j++)
			{
				Edge e = new Edge();
				e.cost = 0;			//could be Integer.MAX_VALUE, but unweighted/directed graph
				edges[i][j] = e;
			}
		}
	}

	//addEdge: changes cost at src,tar position in edges array to 1
	public void addEdge(int src, int tar)
	{
		edges[src][tar].cost = 1;
	}

	//neighbors: return int array of the the neighbors of a given index
	public int[] neighbors(int vertex)
	{
		int[] connected = new int[0];
		for(int i = 0; i < numVertices; i++)
		{
			if(edges[vertex][i].cost != 0)
			{
				connected = copy(connected, i);
			}
		}
		return connected;
	}

	//copy: grow neighbors array, return a copy of the array with the next int appended
	private int[] copy(int[] input, int nextTarget)
	{
		int[] output = new int[input.length+1];
		for(int i = 0; i < input.length; i++)
		{
			output[i] = input[i];
		}
		output[input.length] = nextTarget;
		return output;
	}

	//topologicalSort: print a topological sorting of the graph (bfs and queue implementation)
	public void topologicalSort()
	{
		//build NumIncident array w/each position being the vertex:
		//	initialize with 0, then fill incoming 
		int[] numIncident = new int[numVertices];
		for(int v = 0; v < numVertices; v++)
		{
			numIncident[v] = 0;
		}
		for(int vert = 0; vert < numVertices; vert++)
		{
			for(int j = 0; j < numVertices; j++)
			{
				if(edges[vert][j].cost == 1)
				{
					numIncident[j]++;
				}
			}
		}

		/*Implement queue using LinkedList from java.util (FIFO), for adding in topological order
		 *	where vertices are added to the final array (topologicalOrder) if the vertex has 0 
		 *  incident edges
		 */
		Queue<Integer> queue = new LinkedList<Integer>();
		for(int i = 0; i < numVertices; i++)
		{
			if(numIncident[i] == 0)
			{
				queue.add(new Integer(i));
			}
		}
		int currentIndex = 0;
		int[] topologicalOrder = new int[numVertices];
		while(!queue.isEmpty())
		{
			//nextOnTopological should be the one with the lowest number of incident edges (0)
			int nextOnTopological = ((Integer)queue.remove()).intValue();
			topologicalOrder[currentIndex++] = nextOnTopological;
			for(int vertex = 0; vertex < numVertices; vertex++)
			{
				//if matrix shows an edge, decrement number of incident edges in numIncident array,
				//	then check if the vertex has 0 incidents and if it does, then add to queue
				if(edges[nextOnTopological][vertex].cost != 0)
				{
					if(--numIncident[vertex] == 0)
					{
						queue.add(new Integer(vertex));
					}
				}
			}//end for
		}//end while

		/*If the currentIndex is less than total vertices, the queue did not add all the vertices,
		 *	meaning there was some cycle because some incident edges did not decrement to 0 then get 
		 *	added to queue, so currentIndex did not update and ends with less than total num of vertices
		 */
		if(currentIndex < numVertices)
		{
			System.out.println("There may be a cycle in this graph");
		}
		System.out.println("----------------------------------");
		System.out.println("testing Topological Order:");
		for(int i = 0; i < topologicalOrder.length; i++)
		{
			System.out.print(topologicalOrder[i] + "| ");
		}
		System.out.println();
	}

	//Prints matrix, for testing purposes
	public void print()
	{
		System.out.println("---------------------------------");
		for(int i = 0; i < edges.length; i++)
		{
			for(int j = 0; j < edges.length; j++)
			{
				System.out.print("| ");
				System.out.print(edges[i][j].cost + " ");
			}
			System.out.print("|\n");
		}//end outer for
		System.out.println("---------------------------------");
	}

	//Inner class Edge
	class Edge
	{
		int neighbor;
		int cost;
		Edge next;
	}

	//Personal test for Graph Adj Matrix
	public static void main(String[] args)
	{
		GraphAdjMatrix adj = new GraphAdjMatrix(8);
		adj.addEdge(0, 2);
		adj.addEdge(0, 3);
		//test neighbors function
		int[] test = adj.neighbors(0);
		System.out.print("testing neighbors on 0: " +test.length+"   vertex IDs:");
		for(int i = 0; i < test.length; i++)
		{
			System.out.print(" "+test[i] + " " );
		}
		System.out.println();

		//testing topological sort function (graph looks like reader page 174
		adj.addEdge(1, 3);
		adj.addEdge(2, 6);
		adj.addEdge(3, 4);
		adj.addEdge(4, 6);
		adj.addEdge(4, 7);
		adj.addEdge(5, 4);
		adj.addEdge(5, 7);
		adj.print();
		adj.topologicalSort();

		System.out.println("\n------------------------------------------------------------------------\n");
		adj = new GraphAdjMatrix(8);
		adj.addEdge(0, 1);
		adj.addEdge(1, 2);
		//test neighbors function
		int[] test2 = adj.neighbors(0);
		System.out.print("testing neighbors on 0: " +test2.length+"   vertex IDs:");
		for(int i = 0; i < test2.length; i++)
		{
			System.out.print(" "+test2[i] + " " );
		}
		System.out.println();

		//testing topological sort function
		adj.addEdge(2, 3);
		adj.addEdge(3, 4);
		adj.addEdge(4, 0);
		adj.addEdge(5, 2);
		adj.addEdge(5, 6);
		adj.addEdge(6, 7);
		adj.print();
		adj.topologicalSort();
		/*Graph looks like this, with cycle 0-4:
		 * 				
		 * 				0  -- 4
		 * 				|  	 /
		 * 			5	1   3
		 * 			|\	|  /
		 * 			|	2	
		 * 			6
		 * 			|
		 * 			7
		 * 
		 */
		

	}
}