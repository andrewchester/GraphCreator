import java.awt.Button;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
/*
 * Andrew Chester
 * This program runs a graphing program with the ability to test to see if there is a connection between two nodes and travel to all the nodes using the shortest path.
 * 
 * 
 * Main class for graph creator
 * Handles all of the user input and GUI elements
 */
public class Graph {
	
	//A frame for the graph and the controls
	private JFrame graphFrame;
	private JFrame buttonFrame;
	
	//Buttons and text boxes
	private JButton node, edge, clear, check, salesman;
	private JTextField label, searchLabel, salesmanLabel;
	
	private GraphPanel panel;
	private STATE state = STATE.NODE;
	
	Graph(){
		graphFrame = new JFrame("Graph");
		buttonFrame = new JFrame("Options");
		
		node = new JButton("Node");
		edge = new JButton("Edge");
		clear = new JButton("Clear");
		check = new JButton("Check Connection");
		salesman = new JButton("Travelling Salesman");
		label = new JTextField("A");
		searchLabel = new JTextField("Find path to");
		salesmanLabel = new JTextField("Salesman starts at");
		
		panel = new GraphPanel(this);
		panel.setLocation(0, 0);
		panel.setSize(800, 600);
		
		graphFrame.setSize(800,600);
		buttonFrame.setSize(200, 600);
		
		graphFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		buttonFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		graphFrame.setLocationRelativeTo(null);
		buttonFrame.setLocation(350, 220);
		
		graphFrame.setResizable(false);
		buttonFrame.setResizable(false);
		
		graphFrame.setLayout(null);
		buttonFrame.setLayout(null);
		
		node.setSize(100, 65);
		edge.setSize(100, 65);
		clear.setSize(100, 45);
		check.setSize(100, 45);
		salesman.setSize(100, 45);
		label.setSize(100, 25);
		searchLabel.setSize(100, 25);
		salesmanLabel.setSize(100, 25);
		
		node.setBackground(Color.RED);
		edge.setBackground(Color.GRAY);
		clear.setBackground(Color.LIGHT_GRAY);
		check.setBackground(Color.LIGHT_GRAY);
		salesman.setBackground(Color.LIGHT_GRAY);
		
		node.setLocation(50, 20);
		edge.setLocation(50, 100);
		clear.setLocation(50, 500);
		check.setLocation(50, 245);
		salesman.setLocation(50, 320);
		label.setLocation(50, 180);
		searchLabel.setLocation(50, 210);
		salesmanLabel.setLocation(50, 370);
		
		node.setBorderPainted(false);
		edge.setBorderPainted(false);
		clear.setBorderPainted(false);
		check.setBorderPainted(false);
		salesman.setBorderPainted(false);
		
		//When you click the node button, then it'll switch the color and the state so you'll start placing nodes on the graph
		node.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				state = STATE.NODE;
				node.setBackground(Color.RED);
				edge.setBackground(Color.GRAY);
			}
		});
		//When you click the edge button, it'll switch the color and the state so you'll start placing edges between nodes on the graph
		edge.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				state = STATE.EDGE;
				edge.setBackground(Color.RED);
				node.setBackground(Color.GRAY);
			}
		});
		//Does a particular action depending on the state, adding nodes or adding edges
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				if(state == STATE.NODE)
					panel.addNode(new Node(e.getX(), e.getY(), label.getText()));
				else if(state == STATE.EDGE) {
					panel.highlightNode(e.getX(), e.getY());
				}
					
			}
		});
		//Runs the clear method when you click it
		clear.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				panel.clear();
			}
		});
		//Checks to see if there's a connection between the two nodes entered into the next field and finds the path between them
		check.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				//Gets the starting node and the ending node and passes them to checkConnection
				Node first = null;
				Node last = null;
				for(Node n : panel.getNodes()) {
					if(n.getLabel().equals(label.getText()))
						first = n;
				}
				
				if(first == null) {
					errorMessage("Couldn't find " + "'" + label.getText() + "'");
					return;
				}
				
				for(Node n : panel.getNodes()) {
					if(n.getLabel().equals(searchLabel.getText()))
						last = n;
				}
				
				if(last == null) {
					errorMessage("Couldn't find " + "'" + label.getText() + "'");
					return;
				}
				
				panel.getMatrix().checkConnection(first, last);
				panel.repaint();
			}
		});
		//Travels to each node and finds the shortest path connecting each node
		salesman.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				//Getst the starting node, makes the que array, and passes them both to salesman, which will modify the que array.
				Node first = null;
				for(Node n : panel.getNodes()) {
					if(n.getLabel().equals(salesmanLabel.getText()))
						first = n;
				}
				
				if(first == null) {
					errorMessage("Couldn't find " + "'" + label.getText() + "'");
					return;
				}
				
				ArrayList<Node> que = new ArrayList<Node>();
				if(panel.getMatrix().salesman(que, first)) {
					String path = "";
					for(Node n : que) {
						n.toggleHighlighted();
						path += n.getLabel() + " ";
					}
					System.out.println(path);
					System.out.println("Cost " + panel.getMatrix().getCost(que));
				}else
					System.out.println("Couldn't find a connection");
				
				panel.repaint();
			}
		});
		///////////////////////
		graphFrame.add(panel);
		
		buttonFrame.add(node);
		buttonFrame.add(edge);
		buttonFrame.add(clear);
		buttonFrame.add(check);
		buttonFrame.add(salesman);
		buttonFrame.add(label);
		buttonFrame.add(searchLabel);
		buttonFrame.add(salesmanLabel);
		
		graphFrame.setVisible(true);
		buttonFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e) {  e.printStackTrace();  }
		new Graph();
	}
	//Returns the label of the starting node for checkConnection
	public JTextField getLabel() {
		return label;
	}
	//returns label of the node to be searched for
	public JTextField getSearchLabel() {
		return searchLabel;
	}
	//An error message for when either salesman or check connection cannot find a node.
	public void errorMessage(String err) {
		JFrame errFrame = new JFrame(err);
		errFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		errFrame.setLocationRelativeTo(null);
		errFrame.setResizable(false);
		errFrame.setSize(300, 100);
		
		Button close = new Button("Close");
		close.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				errFrame.dispose();
			}
		});
		errFrame.add(close);
		errFrame.setVisible(true);
	}
}
