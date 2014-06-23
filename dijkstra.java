import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.lang.*;
import java.util.Collections;
/**
The main class is dijkstra which is used for implementing both simple schema and f-heap schema
*/
class dijkstra
{
	public static void main(String args[])
	{
		try
		{
			/*The print variable is used to determine the output displayed*/
			
			boolean print = (args[0].equals("-r"))?false:true;
			dijkstra dj = new dijkstra();
			if(args[0].equals("-r"))
			{
				int n = Integer.parseInt(args[1]);
				float d = Float.parseFloat(args[2])*10;
				int sn = Integer.parseInt(args[3]);
				long tot = n*(n-1)/2;
				long de = (long)d*tot/1000;
				int visited[] = new int[n];
				boolean cv[] = new boolean[n];

				LinkedList<Edge> l[] = new LinkedList[n];

				LinkedList<Integer> path1[] = new LinkedList[n];
				LinkedList<Integer> path2[] = new LinkedList[n];
				Nodedj node[] = new Nodedj[n];
				
				/*Initializing all the variables*/
				
				for(int i=0; i < n;i++){
					node[i] = new Nodedj();
					node[i].index = i;
					node[i].cost = Double.POSITIVE_INFINITY;
					l[i]=new LinkedList();
					path1[i]=new LinkedList();
					path1[i].add(sn);
					path2[i]=new LinkedList();
					path2[i].add(sn);

				}
				/**
				Check whether the number of edges can form a complete graph by  depth first algorithm
				*/
				
				if(de < n-1)
				{
					System.out.println("Can not form graph with these number of edges and density");
					return;
				}
				while(true){
					dj.randGraphGenerate(de,n,l);
					dj.depthFirst(l,cv,sn);
					for(int i=0;i<n;i++)
					{
						if(cv[i]==false)
							continue;
					}
					break;
				}
				long start = 0;
				long stop =0;
				long time1 = 0;
				long time2 = 0;
				
				//Applying fheap and simple schema
								
				time1 = dj.fcalc(l,path2, sn,sn, n, print);
				start = System.currentTimeMillis();
				dj.djcalc(l,sn, visited, node,sn, path1);
				stop = System.currentTimeMillis();
				time2 = stop-start;
				System.out.println("F-heap "+time1+"\nSimple "+time2);
			}
			else if(args[0].equals("-f")||args[0].equals("-s"))
			{
				String filename = args[1];
				BufferedReader br = new BufferedReader(new FileReader(filename));
				/*Getting Information stored in the file*/
				String sourceNode = br.readLine();
				String info = br.readLine();
				String[] ve = info.split(" ");
				int numVec = Integer.parseInt(ve[0]);
				int numEdg = Integer.parseInt(ve[1]);
				int sn = Integer.parseInt(sourceNode);
				int nodes[]=new int[3];
				int visited[] = new int[numVec];
				double currentCost[] = new double[numVec];
				LinkedList<Edge> l[] = new LinkedList[numVec];
				LinkedList<Integer> path[] = new LinkedList[numVec];
				Nodedj node[] = new Nodedj[numVec];
				for(int i=0; i < numVec;i++){
					l[i]=new LinkedList();
					path[i]=new LinkedList();
					path[i].add(sn);
					node[i] = new Nodedj();
					node[i].index = i;
					node[i].cost = Double.POSITIVE_INFINITY;
				}
				String check=br.readLine();
				while(check!=null)
				{
					String temp[] = check.split(" ");
					Edge e[]=new Edge[2];
					for(int i=0;i<2;i++)
						e[i]=new Edge();
					nodes[0]= Integer.parseInt(temp[0]);
					nodes[1]= Integer.parseInt(temp[1]);
					nodes[2]= Integer.parseInt(temp[2]);
					e[0].adjIndex=nodes[0];
					e[0].cost=nodes[2];
					e[1].adjIndex=nodes[1];
					e[1].cost=nodes[2];
					l[nodes[0]].add(e[1]);
					l[nodes[1]].add(e[0]);
					check = br.readLine();
				}
				currentCost[sn]=0;
				if(args[0].equals("-f"))
					dj.fcalc(l,path, sn,sn, numVec, print);
				else if (args[0].equals("-s")) {
					dj.djcalc(l,sn, visited, node,sn, path);
					for(int i=0;i<numVec;i++){
						System.out.println((int)node[i].cost);
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	void randGraphGenerate(long de, int n, LinkedList<Edge> l1[]){
		/**
				This is the reandom graph genearator where the number of nodes calculated are sufficient to generate a continuous graph.
				Only then this part of the process is applied.
				It also checks for new nodes being genreated to not override old edges
		*/
				
		Random rand= new Random();
		for(int i=0;i<de;i++)
		{
			Edge e1[] = new Edge[2];
			Edge e2[] = new Edge[2];

			int s1 = rand.nextInt(n);
			int s2 = rand.nextInt(n);
			while(s1==s2){
				s1 = rand.nextInt(n);
				s2 = rand.nextInt(n);
			}
			int tempCost = rand.nextInt(1000)+1;
			Iterator<Edge> it = l1[s1].iterator();
			while(it.hasNext()) {
				if(it.next().adjIndex==s2) {
					s1 = rand.nextInt(n);
					s2 = rand.nextInt(n);
					it = l1[s1].iterator();
				}
			}
			for(int j=0;j<2;j++){
				e1[j]=new Edge();
				e2[j]=new Edge();

			}
			e1[0].adjIndex=s1;
			e1[0].cost=tempCost;
			e1[1].adjIndex=s2;
			e1[1].cost=tempCost;
			e2[0].adjIndex=s1;
			e2[0].cost=tempCost;
			e2[1].adjIndex=s2;
			e2[1].cost=tempCost;

			l1[s1].add(e1[1]);
			l1[s2].add(e1[0]);
		
		}
	}
	void depthFirst(LinkedList<Edge> l[], boolean cv[], int cn){
		/**
		This function is used for checking the connectivity of the graph generated
		*/
		
		if(cv[cn]==true){
			return;
		}
		cv[cn]=true;
		Iterator <Edge> it = l[cn].iterator();
		while(it.hasNext()){
			Edge e = (Edge)it.next();
			depthFirst(l, cv,e.adjIndex);

		}
	}

	void djcalc(LinkedList<Edge> l[],int cn, int visited[],Nodedj n[],int sn, LinkedList<Integer> path[]){
		
		/**
		Array implementation of dijkstra's
		It has a visited boolean matrix and neighbor linked list to  determine the shortes path
		*/
	
		LinkedList<Nodedj> neighbour = new LinkedList<Nodedj>();
		Iterator<Edge> it1 = l[sn].iterator();
		n[sn].cost = 0;
		while(it1.hasNext())
		{
			Edge e= (Edge)it1.next();
			n[e.adjIndex].cost = e.cost;
			neighbour.add(n[e.adjIndex]);
			visited[sn] = 1;
			path[e.adjIndex].add(e.adjIndex);

		}
	
		/*The sort function is implemented in the Nodedj class*/
	
		Collections.sort(neighbour);
		while(!neighbour.isEmpty()){

			/*Used for adding nodes into the neghbor list depending on its distance*/
	
			Nodedj tempn = neighbour.remove();
			Iterator<Edge> it2 = l[tempn.index].iterator();
			visited[tempn.index] = 1;
			while(it2.hasNext())
			{
				Edge e= (Edge)it2.next();
				if(visited[e.adjIndex]!=1 && !neighbour.contains(n[e.adjIndex])){
					neighbour.add(n[e.adjIndex]);

				}
				double tempcost;
				tempcost= e.cost+n[tempn.index].cost;
				if(tempcost < n[e.adjIndex].cost){
					n[e.adjIndex].cost = tempcost;
					path[e.adjIndex].clear();
					for(int i=0; i < path[tempn.index].size(); i++){
						path[e.adjIndex].add(path[tempn.index].get(i));
					}
					path[e.adjIndex].add(e.adjIndex);
				}
			}
			Collections.sort(neighbour);
		}

	}


	long fcalc(LinkedList<Edge> l[], LinkedList<Integer> path[], int sn, int cn, int n, boolean print){
	/**
	This part of implements the f-heap with the helper function is used for calling fcalchelper recursively
	
	*/
		long start = System.currentTimeMillis();
		FibonacciHeap fh = new FibonacciHeap();
		Nodefh[] a=new Nodefh[n];
		a[sn]=new Nodefh();
		a[sn].index = sn;
		a[sn].dist = 0;
		a[sn].degree = 0;
		double[] currentCost = new double[n];		
		for(int i=0;i<n;i++){
			currentCost[i] = 0;
			if(i!=sn){
				a[i]=new Nodefh();
				a[i].index = i;
				a[i].dist =Double.POSITIVE_INFINITY;
				a[i].degree = 0;
				a[i].ChildCut = false;
				fh.fhInsert(a[i]);
			}
		}
		fcalcHelper(l, path, sn, cn, n, fh, a, currentCost);
		long stop = System.currentTimeMillis();
		if(print) {
			for(int i=0;i<n;i++){
				System.out.println((int)a[i].dist);
			}
		}
		return (stop - start);
	}
	void fcalcHelper(LinkedList<Edge> l[], LinkedList<Integer> path[], int sn, int cn, int n, FibonacciHeap fh, Nodefh a[],double currentCost[]){
		/*This function forms the part of calling the system recursively*/
		try{
			if(fh.isEmpty()==true){
				return;
			}
			Iterator<Edge> it = l[cn].iterator();
			while(it.hasNext())
			{
				int tempcost=0;
				Edge e= (Edge)it.next();
				if(a[cn].dist == Double.POSITIVE_INFINITY)
					tempcost = e.cost;
				else
					tempcost= (int)(e.cost+a[cn].dist);
				if(tempcost < a[e.adjIndex].dist){
					fh.decreasekey(a[e.adjIndex], tempcost);
					currentCost[e.adjIndex] = tempcost;
					path[e.adjIndex].clear();
					for(int i=0; i < path[cn].size(); i++){
						path[e.adjIndex].add(path[cn].get(i));
					}
					path[e.adjIndex].add(e.adjIndex);
				}
			}
			Nodefh temp = fh.removeMin();
			int prevcn = cn;
			cn = temp.index;
			if(cn != -1){
				fcalcHelper(l,path, sn,cn,n,fh,a,currentCost);
				return;
			}
			else{
				cn = prevcn;
				return;
			}
		}
		catch(Exception e)
		{System.out.println(e);}
	}

}

class Edge {
	int adjIndex;
	int cost;

}

class FibonacciHeap{
	/**
	The main data structure of fibonacci heap is implemented along with its duncitons
	*/
	Nodefh min;
	Nodefh current;
	Nodefh last;
	int minindex;
	FibonacciHeap(){
		min=new Nodefh();
		min = null;
	}
	void fhInsert(Nodefh in)
	{
		/**
			Ordinarily inserts a node into the f-heap altering min, current and last pointers
		*/
		in.ChildCut = false;
		if(isEmpty()){
			min=in;
			min.Left = min;
			min.Right = min;
			minindex = min.index;
			current = min;
			last = min;
		}	
		else
			if(in.dist < min.dist){
				in.Right = min;
				in.Left = min.Left;
				min.Left.Right = in;
				min.Left = in;
				min = in;
				minindex = in.index;
			}
			else{
				in.Right = min;
				in.Left = min.Left;
				min.Left.Right = in;
				min.Left = in;
			}
		last = min.Left;
	}
	boolean isEmpty(){
		if(min==null){
			return true;
		}
		return false;
	}

	void meld(FibonacciHeap fh1, FibonacciHeap fh2){
	/**
			Combiners two f-heaps
	*/
		
		Nodefh temp1 = new Nodefh();
		temp1 = fh1.min.Left;
		fh1.min.Left = fh2.min.Left;	
		fh2.min.Left = temp1;
	}
	Nodefh removeMin(){
	/**
		Removes the minimum also calls the pairwise combine
	*/
		
		if(min == null){
			Nodefh dummy = new Nodefh();
			dummy.index = -1;
			return dummy;
		}
		Nodefh temp = new Nodefh();
		temp = min;
		if((min.Child == null) && (min == min.Right)){
			min = null;
			return temp;
		}
		pairwiseCombine();
		return temp;
	}
	void decreasekey(Nodefh node, double newValue){
		/**
			This is used for decresing the key i.e. dist variable of Nodefh nodes
		*/
		
		node.dist = newValue;
		if(node.Parent == null){
			if(node == min){
				return;
			}
			else
				node.Left.Right = node.Right;
			node.Right.Left = node.Left;
			fhInsert(node);
			return;
		}
		/**
			Condition varies depending on whether the child is present or not
		*/
		if(node.Parent.Child == node){
			if(node.Right != null && node.Right != node){
				node.Parent.Child = node.Right;
				if(node.Left!=null && node.Left != node){
					node.Left.Right = node.Right;
					node.Right.Left = node.Left;

				}
				else{
					node.Right.Left = node.Right;
					node.Right.Right = node.Right;
				}
			}
			else{
				node.Parent.Child = null;
			}
		}
		else{
			if(node.Left != null && node.Left != node){
				node.Left.Right = node.Right;
				node.Right.Left = node.Left;
			}
		}
		
		/**
			The decreased key node is inserted here.
		*/
		
		fhInsert(node);
		if(node.Parent.ChildCut == true){
			cascadingCut(node.Parent);
		}
		else{
			node.Parent.ChildCut = false;
		}
		node.Parent = null;
	}
	void cascadingCut(Nodefh node){
		/**
			It checks whether the parent node's childcut field is false. If not it takes the node away and inserts again
		*/
		if(node.Parent == null){
			return;
		}
		if(node.Parent.Child == node){
			if(node.Right != null && node.Right != node){
				node.Parent.Child = node.Right;
				if(node.Left!=null && node.Left != node){
					node.Left.Right = node.Right;
					node.Right.Left = node.Left;

				}
				else{
					node.Right.Left = node.Right;
					node.Right.Right = node.Right;
				}
			}
			else{
				node.Parent.Child = null;
			}
		}
		else{
			if(node.Left != null && node.Left != node){
				node.Left.Right = node.Right;
				node.Right.Left = node.Left;
			}
		}
		fhInsert(node);
		if(node.Parent.ChildCut == true){
			cascadingCut(node.Parent);
		}
		else{
			node.Parent.ChildCut = true;
		}
		node.Parent = null;
	}
	void arbitraryRemove(Nodefh node){
	/**
		Removes the given external key automatically
	*/
	
		if(node.Parent.Child == node){
			if(node.Right != null && node.Right != node){
				node.Parent.Child = node.Right;
				if(node.Left!=null && node.Left != node){
					node.Left.Right = node.Right;
					node.Right.Left = node.Left;
					node.Parent = null;
				}
				else{
					node.Right.Left = node.Right;
					node.Parent = null;
				}
			}
			else{
				node.Parent.Child = null;
				node.Parent = null;
			}
		}
		else{
			if(node.Left != null && node.Left != node){
				node.Left.Right = node.Right;
				node.Right.Left = node.Left;
			}
			node.Parent = null;

		}
		if(node.Child!=null){
			Nodefh temp = node.Child;
			if(node.Child.Right!=null){
				temp = node.Child.Right;
			}
			fhInsert(node.Child);
			while(temp!=node.Child.Right){
				fhInsert(temp);
				temp = temp.Right;
			}

		}

	}
	void pairwiseCombine(){
	/**
			This function is used for combining two nodes in the f-heap
	*/
		
		min.Left.Right = min.Right;
		min.Right.Left = min.Left;
		/**
		Pairwise combine differentiates itself in operation of depending on the presence of a child node
		*/
		
		if(min.Child==null){
		/*map acts as the table to store similar degree nodes*/
			HashMap<Integer, Nodefh> map = new HashMap<Integer, Nodefh>();
			current = min.Right;
			min = null;
			min = current;
			last = current.Left;
			while(current!=last){
				Nodefh check = new Nodefh();
				check = current.Right;
				while(map.containsKey(current.degree)){
					Nodefh temp = map.remove(current.degree);
					
					/*Since a parent node can have only one child node a condition is checked to update its child node*/
					if(temp.dist < current.dist){
					/*If the node stored is less than the incoming node*/
						current.Right.Left = current.Left;
						current.Left.Right = current.Right;
						temp.ChildCut = false;
						if(temp.isChildEmpty()){
							temp.Child = current;
							current.Left = current.Right = current;
							temp.Child.Parent = temp;
							temp.degree = 1;
						}
						else{
							current.Right = temp.Child;
							current.Left = temp.Child.Left;
							temp.Child.Left.Right = current;
							temp.Child.Left = current;
							current.Parent = temp;
							if(temp.Child == temp.Child.Right){
								temp.Child.Right = current;
							}
							temp.degree +=1;
						}

						current = temp;
					}
					else{
					/*If the incoming node is lower*/
						temp.Right.Left = temp.Left;
						temp.Left.Right = temp.Right;
						current.ChildCut = false;	
						if(current.isChildEmpty()){

							current.Child = temp;
							temp.Left = temp.Right = temp;
							current.Child.Parent = current;
							current.degree = 1;
						}
						else{
							temp.Right = current.Child;
							temp.Left = current.Child.Left;
							current.Child.Left.Right = temp;
							current.Child.Left = temp;
							temp.Parent = current;
							current.degree +=1;
						}

					}

				}
				if(min.dist >= current.dist){
					min = current;
				}
				map.put(current.degree, current);
				current = check;

			}
			last = min.Left;
			/*Since our condition is used only till last node and exits during last node the iteration is repeated for the last node*/
			while(map.containsKey(current.degree)){
				Nodefh temp = map.remove(current.degree);
				if(temp.dist < current.dist){
					temp.ChildCut = false;
					current.Right.Left = current.Left;
					current.Left.Right = current.Right;
					if(temp.isChildEmpty()){
						temp.Child = current;
						current.Left = current.Right = current;
						temp.Child.Parent = temp;
						temp.degree = 1;
					}
					else{
						current.Right = temp.Child;
						current.Left = temp.Child.Left;
						temp.Child.Left.Right = current;
						temp.Child.Left = current;
						current.Parent = temp;
						temp.degree +=1;
					}
					current = temp;
				}
				else{
					temp.Right.Left = temp.Left;
					temp.Left.Right = temp.Right;
					current.ChildCut = false;
					if(current.isChildEmpty()){
						current.Child = temp;
						temp.Left = temp.Right = temp;
						current.Child.Parent = current;
						current.degree = 1;
					}
					else{
						temp.Right = current.Child;
						temp.Left = current.Child.Left;
						current.Child.Left.Right = temp;
						current.Child.Left = temp;
						temp.Parent = current;
						current.degree +=1;
					}

				}
			}
			if(min.dist >= current.dist){
				min = current;
			}
			current = min;
			last = min.Left;

		}
		else{
			HashMap<Integer, Nodefh> map = new HashMap<Integer, Nodefh>();
			current = min.Child;
			current.Parent = null;
			if(min!=min.Right){
				current.Right.Left = min.Left;
				min.Left.Right = current.Right;
				current.Right = min.Right;
				min.Right.Left = current;
				last = current.Left;
			
			}
			else{
				last = current.Left;

			}
			min =null;
			min = current;
			/*In the presence of a child the child has to be inserted at the top level*/
			while(current != last){
				Nodefh check = new Nodefh();
				check = current.Right;

				while(map.containsKey(current.degree)){

					Nodefh temp = map.remove(current.degree);
					if(temp.dist < current.dist){
						temp.ChildCut = false;
						current.Right.Left = current.Left;
						current.Left.Right = current.Right;
						if(temp.isChildEmpty()){

							temp.Child = current;
							current.Left = current.Right = current;
							temp.Child.Parent = temp;
							temp.degree = 1;
						}
						else{
							current.Right = temp.Child;
							current.Left = temp.Child.Left;
							temp.Child.Left.Right = current;
							temp.Child.Left = current;
							current.Parent = temp;
							temp.degree +=1;
						}
						current = temp;
					}
					else{
						temp.Right.Left = temp.Left;
						temp.Left.Right = temp.Right;
						current.ChildCut = false;
						if(current.isChildEmpty()){
							current.Child = temp;
							temp.Left = temp.Right = temp;
							current.Child.Parent = current;
							current.degree = 1;
						}
						else{
							temp.Right = current.Child;
							temp.Left = current.Child.Left;
							current.Child.Left.Right = temp;
							current.Child.Left = temp;
							temp.Parent = current;
							current.degree +=1;
						}

					}
				}
				if(min.dist >= current.dist){
					min = current;

				}
				map.put(current.degree, current);
				current = check;
			}
			last = min.Left;
			while(map.containsKey(current.degree)){
				Nodefh temp = map.remove(current.degree);
				if(temp.dist < current.dist && temp.dist!=0){
					current.Right.Left = current.Left;
					current.Left.Right = current.Right;
					temp.ChildCut = false;
					if(temp.isChildEmpty()){
						temp.Child = current;
						current.Left = current.Right = current;
						temp.Child.Parent = temp;
						temp.degree = 1;
					}
					else{
						current.Right = temp.Child;
						current.Left = temp.Child.Left;
						temp.Child.Left.Right = current;
						temp.Child.Left = current;
						current.Parent = temp;
						temp.degree +=1;
					}
					current = temp;
				}
				else{
					temp.Right.Left = temp.Left;
					temp.Left.Right = temp.Right;
					current.ChildCut = false;
					if(current.isChildEmpty()){
						current.Child = temp;
						temp.Left = temp.Right = temp;
						current.Child.Parent = current;
						current.degree = 1;
					}
					else{
						temp.Right = current.Child;
						temp.Left = current.Child.Left;
						current.Child.Left.Right = temp;
						current.Child.Left = temp;
						temp.Parent = current;
						current.degree +=1;
					}

				}
			}
			if(min.dist >= current.dist){
				min = current;

			}
			current = min;
			last = min.Left;
		}

	}

}
class Nodedj implements Comparable<Nodedj>{
/**
Nodedj is the node class used for ordinary implementation
*/
	int index;
	double cost;
	public int compareTo(Nodedj e) {
		double comparedSize = e.cost;
		if (this.cost > comparedSize) {
			return 1;
		} else if (this.cost == comparedSize) {
			return 0;
		} else {
			return -1;
		}
	}

}
class Nodefh{
/**
Nodedj is the node class used for f-heap implementation
*/

	int index;
	double dist;
	Nodefh Left;
	Nodefh Right;
	Nodefh Child;
	Nodefh Parent;
	int degree;
	boolean ChildCut;
	boolean isChildEmpty(){
		if(Child==null){
			return true;
		}
		return false;
	}
}