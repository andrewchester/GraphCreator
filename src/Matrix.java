import java.awt.Button;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
/*
 * This class handles updating the adjacency matrix, printing the adjacency matrix to console, adding and removing connections, clearing the adjacency matrix, and all the recursive pathfinding functions
 * 
 */
public class Matrix {
	
	private ArrayList<ArrayList<Boolean>> adjacencyMatrix;
	private ArrayList<Node> nodes;
	private ArrayList<Edge> edges;
	
	public Matrix(ArrayList<Node> nodes, ArrayList<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges;
		adjacencyMatrix = new ArrayList<ArrayList<Boolean>>();
	}
	//Updates the adjacency matrix
	public void update() {
		for(int i = 0; i < adjacencyMatrix.size(); i++) {
			if(adjacencyMatrix.get(i).size() < adjacencyMatrix.size()) {
				while(adjacencyMatrix.get(i).size() < adjacencyMatrix.size())
					adjacencyMatrix.get(i).add(false);
			}
		}
	}
	//Prints the adjacency matrix to console
	public void printMatrix() {
		update();
		System.out.println("Printing Matrix: ");
		for(int a = 0; a < adjacencyMatrix.size(); a++) {
			for(int b = 0; b < adjacencyMatrix.size(); b++) {
				System.out.println(adjacencyMatrix.get(a).get(b) + "\t");
			}
			System.out.println();
		}
	}
	//Adds a connection to the adjacency matrix
	public void addConnection(int first, int second) {
		adjacencyMatrix.get(first).set(second, true);
	}
	//Adds a node to the adjacency matrix
	public void addNode(Node n) {
		adjacencyMatrix.add(new ArrayList<Boolean>());
		for(int i = 0; i < adjacencyMatrix.size(); i++) {
			adjacencyMatrix.get(adjacencyMatrix.size() - 1).add(false);
			n.setId(adjacencyMatrix.size() - 1);
		}
		update();
	}
	//Clears the adjacency matrix
	public void clear() {
		adjacencyMatrix.removeAll(adjacencyMatrix);
	}
	//A function that uses the recursive function checkPath to search from a starting node to find a ending node
	public void checkConnection(Node first, Node last) {
		ArrayList<Node> path = new ArrayList<Node>(); //Creates an empty path
		path.add(first);//Adds the starting node
		for(Node n : getConnected(first)) //Looks through all the neighbors
			if(checkPath(path, n, last)) { //Recursively checks that branch to try and find the end node
				message("Path Found!", path);
				for(Node node : path) //Highlights the path
					node.toggleHighlighted();
				return;
			}
		message("They're not connected");
	}
	//Recursively checks path taking the current path, current node, and end node as parameters
	public boolean checkPath(ArrayList<Node> path, Node current, Node last) {
		ArrayList<Node> neighbors = getConnected(current); //Gets the neighbors of the current node it's on
		neighbors.removeAll(path); //Removes the current path so it doesn't go backwards
		if(current != last & neighbors.size() != 0) { //Continues to search if it's not the end node and it's not a dead end
			path.add(current); 
			for(Node n : neighbors) //Recursively loops through neighbors looking for the end node
				if(checkPath(path, n, last))
					return true;
			
			//If it's not the right branch, none of it's neighbors leads to the end node, then it removes itself from the path and returns false.
			path.remove(current);
			return false;
		}else if(current == last) { //If it's the end node return true
			path.add(last);
			return true;
		}else{ //Anything else, false
			return false;
		}
	}
	//Finds the shortest path to visiting each node once, takes the current path and the current node as parameters
	public boolean salesman(ArrayList<Node> que, Node current) {
		que.add(current);
		ArrayList<Node> neighbors = getConnected(current); //Gets its current neighbors
		neighbors.removeAll(que);
		if(que.size() == nodes.size()) { //If we're at the end of the path, return true
			return true;
		}else if(neighbors.size() == 0 && que.size() != nodes.size()) { //If we're at a dead end without having reached all the possible nodes, return false
			return false;
		}else { //If we're still looking then recursively search through the lowest cost path
			Node least = neighbors.get(0);
			//Finds the next neighbor by finding the lowest cost path
			for(int i = 0; i < neighbors.size(); i++) {
				if(getCost(current, neighbors.get(i)) < getCost(current, least))
						least = neighbors.get(i);
			}
			return salesman(que, least);//Calls the salesman function on that neighbor
		}
	}
	//Gets all the neighbors of a node
	public ArrayList<Node> getConnected(Node n){
		ArrayList<Node> connected = new ArrayList<Node>();
		
		update();
		for(int i = 0; i < adjacencyMatrix.get(n.getId()).size(); i++) {
			if(adjacencyMatrix.get(n.getId()).get(i))
				for(Node node : nodes)
					if(node.getId() == i)
						connected.add(node);
		}
		
		return connected;
	}
	//Gets the edge between two nodes, returns null if there isn't one
	public Edge getEdge(Node first, Node second) {
		for (Edge e : edges) 
			if(e.connectedTo(first) && e.connectedTo(second))
				return e;
		return null;
	}
	//Error message that takes a string
	public void message(String message) {
		JFrame messageFrame = new JFrame(message);
		messageFrame.setResizable(false);
		messageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		messageFrame.setResizable(false);
		messageFrame.setLocationRelativeTo(null);
		messageFrame.setSize(250,125);
		Button close = new Button("Close");
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				messageFrame.dispose();
			}
		});
		messageFrame.add(close);
		messageFrame.setVisible(true);
	}
	//Error message that takes a string and the path
	public void message(String message, ArrayList<Node> path) {
		JFrame messageFrame = new JFrame(message + " Length: " + path.size());
		messageFrame.setResizable(false);
		messageFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		messageFrame.setResizable(false);
		messageFrame.setLocationRelativeTo(null);
		messageFrame.setSize(250,125);
		Button close = new Button("Close");
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				for(Node n : path)
					n.toggleHighlighted();
				path.removeAll(path);
				messageFrame.dispose();
			}
		});
		messageFrame.add(close);
		messageFrame.setVisible(true);
	}
	//A version of the getCost method that gets the total cost of an entire path
	public int getCost(ArrayList<Node> path) {
		int cost = 0;
		
		for(int i = 0; i < path.size() - 1; i++) 
			cost += getCost(path.get(i), path.get(i + 1));
		
		return cost;
	}
	//A version of the getCost method that gets the cost between two nodes
	public int getCost(Node first, Node second) {
		int cost = 0;
		
		cost += getEdge(first, second).getLabel();
		
		return cost;
	}
}
