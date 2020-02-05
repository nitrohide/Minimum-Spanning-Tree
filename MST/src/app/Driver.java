package app;
import structures.*;
import java.io.*;
import java.util.ArrayList;



public class Driver {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Graph graph = new Graph("graph2.txt"); 
		
		PartialTreeList start = PartialTreeList.initialize(graph);
		
		ArrayList<Arc> MST = PartialTreeList.execute(start);
		
		System.out.println(MST.toString());
	
	}

}