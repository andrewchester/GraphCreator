import java.awt.Color; 
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;
/*
 * The graphical object: 
 * 		- Handles the rendering and updating of the graph
 * 		- Adds/Removes nodes/edges
 * 		- Manages all the nodes, edges, highlighted nodes, and the matrix object.
 * 
 */
public class GraphPanel extends JPanel{
	
	//Arrays for edges, nodes, etc
	private ArrayList<Node> nodes;
	private ArrayList<Node> highlighted_nodes;
	private ArrayList<Edge> edges;
	private Matrix matrix;
	
	private Graph graph;
	
	GraphPanel(Graph graph){
		super();
		this.graph = graph;
		nodes = new ArrayList<Node>();
		highlighted_nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		matrix = new Matrix(nodes, edges);
	}
	//Updates the edges/connections as well as the highlighted nodes. After the user clicks two nodes, it runs update and than connects them with an edge.
	public void update() {
		if(highlighted_nodes.size() == 2) {
			Node firstNode = highlighted_nodes.get(0);
			Node secondNode = highlighted_nodes.get(1);
			
			edges.add(new Edge(firstNode.getX(), firstNode.getY(), secondNode.getX(), secondNode.getY(), firstNode, secondNode, edges));
			matrix.addConnection(firstNode.getId(), secondNode.getId());
			matrix.addConnection(secondNode.getId(), firstNode.getId());
			
			for(Node n : highlighted_nodes)
				n.toggleHighlighted();
			highlighted_nodes.removeAll(highlighted_nodes);
			this.repaint();
		}
	}
	//Renders nodes, edges, and the lines
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 800, 600);
		
		g.setColor(Color.LIGHT_GRAY);
		for(int i = 0; i < 800; i += 15) 
			g.drawLine(i, 0, i, 600);
		for(int i = 0; i < 600; i+= 15)
			g.drawRect(0, i, 800, i);
		
		for(Node n : nodes)
			n.render(g);
		for(Edge e : edges)
			e.render(g);
		
	}
	//Adds a node to the graph. It also increments the label so that it automatically goes from A to B, B to C, C to D, etc.
	public void addNode(Node n) {
		nodes.add(n);
		matrix.addNode(n);
		char message = graph.getLabel().getText().charAt(0);
		message += 1;
		graph.getLabel().setText(Character.toString(message));
		this.repaint();
	}
	//Removes all edges, nodes, and clears the adjacency matrix
	public void clear() {
		nodes.removeAll(nodes);
		edges.removeAll(edges);
		matrix.clear();
		this.repaint();
	}
	//Highlights a node and adds/removes nodes from highlighted nodes
	public void highlightNode(int x, int y) {
		for(Node n : nodes) {
			if(n.contains(x, y)) {
				n.toggleHighlighted();
				if(n.getHighlighted())
					highlighted_nodes.add(n);
				else
					highlighted_nodes.remove(n);
				this.update();
				this.repaint();
			}
		}
	}
	//Returns the matrix object
	public Matrix getMatrix() {
		return matrix;
	}
	//Returns the nodes array
	public ArrayList<Node> getNodes(){
		return nodes;
	}
}
