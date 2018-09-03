import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
/*
 * The edge class, handles rendering itself and testing connections
 * 
 */
public class Edge {
	private int x1, y1, x2, y2;
	private String label;
	private Node first, second;
	private ArrayList<Edge> edges;
	
	public Edge(int x1, int y1, int x2, int y2, Node first, Node second, ArrayList<Edge> edges) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.first = first;
		this.second = second;
		this.edges = edges;
		this.label = "" + (1 + edges.size());
	}
	//Renders itself, draws a line between the first node it's connected to and the second
	public void render(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.drawLine(x1, y1, x2, y2);
		int midx = (x1 + x2) / 2;
		int midy = (y1 + y2) / 2;
		g.drawString(label, midx, midy);
	}
	//Returns true if it's connected to a node
	public boolean connectedTo(Node n) {
		if (n == first || n == second)
			return true;
		return false;
	}
	//Returns its label as an int which is useful for calculating the cost of a path
	public int getLabel() {
		return Integer.parseInt(label);
	}
	public Node getFirst() {
		return first;
	}
	public Node getSecond() {
		return second;
	}
}
