import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
/*
 * Node object, handles rendering itself, toggling whether or not it's highlighted, and click detection
 * 
 */
public class Node{
	
	private int x, y, w, h, id;
	private String label;
	private boolean highlighted = false;
	
	
	Node(int x, int y, String label){
		this.x = x - 6;
		this.y = y - 6;
		this.w = 12;
		this.h = 12;
		this.label = label;
	}
	//Renders the node, gray or red oval depending on if it's highlighted
	public void render(Graphics g) {
		if(!highlighted)
			g.setColor(Color.DARK_GRAY);
		else
			g.setColor(Color.RED);
		g.drawOval(x, y, w, h);
		g.drawString(label, x + 10, y);
	}
	//Toggles the highlight
	public void toggleHighlighted() {
		if(highlighted)
			highlighted = false;
		else 
			highlighted = true;
	}
	//Tests if the mouse is inside the bounds of the node
	public boolean contains(int mousex, int mousey) {
		int centerx = x + 6;
		int centery = y + 6;
		
		if(Math.abs(mousex - centerx) < 8 && Math.abs(mousey - centery) < 8)
			return true;
		return false;
	}
	public boolean getHighlighted() {
		return this.highlighted;
	}
	public int getX() {
		return x + 6;
	}
	public int getY() {
		return y + 6;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public String getLabel() {
		return label;
	}
}
