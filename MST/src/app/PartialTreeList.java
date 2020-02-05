package app;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import structures.Arc;
import structures.Graph;
import structures.MinHeap;
import structures.PartialTree;
import structures.Vertex;

/**
 * Stores partial trees in a circular linked list
 * 
 */
public class PartialTreeList implements Iterable<PartialTree> {
    
	/**
	 * Inner class - to build the partial tree circular linked list 
	 * 
	 */
	public static class Node {
		/**
		 * Partial tree
		 */
		public PartialTree tree;
		
		/**
		 * Next node in linked list
		 */
		public Node next;
		
		/**
		 * Initializes this node by setting the tree part to the given tree,
		 * and setting next part to null
		 * 
		 * @param tree Partial tree
		 */
		public Node(PartialTree tree) {
			this.tree = tree;
			next = null;
		}
	}

	/**
	 * Pointer to last node of the circular linked list
	 */
	private Node rear;
	
	/**
	 * Number of nodes in the CLL
	 */
	private int size;
	
	/**
	 * Initializes this list to empty
	 */
    public PartialTreeList() {
    	rear = null;
    	size = 0;
    }

    /**
     * Adds a new tree to the end of the list
     * 
     * @param tree Tree to be added to the end of the list
     */
    public void append(PartialTree tree) {
    	Node ptr = new Node(tree);
    	if (rear == null) {
    		ptr.next = ptr;
    	} else {
    		ptr.next = rear.next;
    		rear.next = ptr;
    	}
    	rear = ptr;
    	size++;
    }

    /**
	 * Initializes the algorithm by building single-vertex partial trees
	 * 
	 * @param graph Graph for which the MST is to be found
	 * @return The initial partial tree list
	 */
public static PartialTreeList initialize(Graph graph) {
	PartialTreeList L=new PartialTreeList();
	for(int i=0;i<graph.vertices.length;i++){
		PartialTree T=new PartialTree(graph.vertices[i]);
		Vertex vertex=graph.vertices[i];
		//System.out.println(graph.vertices[i].parent);
		Vertex.Neighbor neighbor=vertex.neighbors;
		while (neighbor!=null){
			Arc arc=new Arc(vertex,neighbor.vertex,neighbor.weight);
			T.getArcs().insert(arc);
			neighbor=neighbor.next;
		}
		L.append(T);
	}
	//print(L);
	return L;
}

/*private static void print(PartialTreeList a){
	Iterator<PartialTree> iter=a.iterator();
	   while (iter.hasNext()) {
	       PartialTree pt=iter.next();
	       System.out.println(pt.toString());
	   }
}
/*
	/**
	 * Executes the algorithm on a graph, starting with the initial partial tree list
	 * for that graph
	 * 
	 * @param ptlist Initial partial tree list
	 * @return Array list of all arcs that are in the MST - sequence of arcs is irrelevant
	 */
public static ArrayList<Arc> execute(PartialTreeList ptlist) {
	ArrayList<Arc> finalMST=new ArrayList<Arc>();
	int count=ptlist.size();
	while (count>1){
		PartialTree PTX=ptlist.remove();
		MinHeap<Arc> PQX=PTX.getArcs();	
		Arc priority=PQX.deleteMin();
		Vertex v1=PTX.getRoot();
		Vertex v2=priority.getv2();
		if (v1==v2 || v1==v2.parent){
			priority=PQX.deleteMin();
			v2=priority.getv2().parent;
		}
		finalMST.add(priority);
		PartialTree PTY=ptlist.removeTreeContaining(v2);
		if (PTY==null){
			continue;
		} 
		PTY.getRoot().parent=PTX.getRoot();
		PTY.merge(PTX);
		ptlist.append(PTY);
		count--;
	}
	return finalMST;
}
	
    /**
     * Removes the tree that is at the front of the list.
     * 
     * @return The tree that is removed from the front
     * @throws NoSuchElementException If the list is empty
     */
    public PartialTree remove() 
    throws NoSuchElementException {
    			
    	if (rear == null) {
    		throw new NoSuchElementException("list is empty");
    	}
    	PartialTree ret = rear.next.tree;
    	if (rear.next == rear) {
    		rear = null;
    	} else {
    		rear.next = rear.next.next;
    	}
    	size--;
    	return ret;
    		
    }

    /**
     * Removes the tree in this list that contains a given vertex.
     * 
     * @param vertex Vertex whose tree is to be removed
     * @return The tree that is removed
     * @throws NoSuchElementException If there is no matching tree
     */
    public PartialTree removeTreeContaining(Vertex vertex) 
    	    throws NoSuchElementException {
    	if (rear==null){
			throw new NoSuchElementException();
		}		
    	if (rear==rear.next){
    		if (vertex.equals(rear.tree.getRoot()))	{
    			PartialTree removedTree=rear.tree;
    			rear=null;
    			size--;
    			return removedTree;
    		}
		} 	
    	Node prev=rear;
    	Node ptr=rear.next;	
    	do {
    		if (vertex.equals(ptr.tree.getRoot()))	{
    			PartialTree currTree=ptr.tree;
    			prev.next=ptr.next;
    			size--;
    			if (ptr==rear){
    				rear=prev;
    			}	
    			return currTree;
    		}	
    		prev=ptr;
    		ptr=ptr.next;
    	}
    	while (ptr!=rear);
    	if (ptr==rear && prev==rear)
    	{
    		throw new NoSuchElementException();
    	}
    	PartialTree found=rear.tree;
		size--;
		return found;
 }
    
    /**
     * Gives the number of trees in this list
     * 
     * @return Number of trees
     */
    public int size() {
    	return size;
    }
    
    /**
     * Returns an Iterator that can be used to step through the trees in this list.
     * The iterator does NOT support remove.
     * 
     * @return Iterator for this list
     */
    public Iterator<PartialTree> iterator() {
    	return new PartialTreeListIterator(this);
    }
    
    private class PartialTreeListIterator implements Iterator<PartialTree> {
    	
    	private PartialTreeList.Node ptr;
    	private int rest;
    	
    	public PartialTreeListIterator(PartialTreeList target) {
    		rest = target.size;
    		ptr = rest > 0 ? target.rear.next : null;
    	}
    	
    	public PartialTree next() 
    	throws NoSuchElementException {
    		if (rest <= 0) {
    			throw new NoSuchElementException();
    		}
    		PartialTree ret = ptr.tree;
    		ptr = ptr.next;
    		rest--;
    		return ret;
    	}
    	
    	public boolean hasNext() {
    		return rest != 0;
    	}
    	
    	public void remove() 
    	throws UnsupportedOperationException {
    		throw new UnsupportedOperationException();
    	}
    	
    }
}


