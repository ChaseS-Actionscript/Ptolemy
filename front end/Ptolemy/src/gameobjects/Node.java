package gameobjects;
import static org.lwjgl.opengl.GL11.*;
public class Node {

	    private float x, y, width, height;
	    private String label;
	    
	    public Node(float x, float y, float width, float height, String label) {
	        this.x = x;
	        this.y = y;
	        this.width = width;
	        this.height = height;
	        this.label = label;
	    }
	    
	    public void render() {
	        // Draw the button as a rectangle
	        glColor3f(0.2f, 0.5f, 0.8f);
	        
	        glBegin(GL_QUADS); // Draw a rectangle
	            glVertex2f(x, y); // Top left
	            glVertex2f(x + width, y); // Top right
	            glVertex2f(x + width, y + height); // Bottom right
	            glVertex2f(x, y + height); // Bottom Left
	        glEnd();
	        
	    }
	    
	    public boolean isHovered(double mouseX, double mouseY) {
	        // Check if the mouse is within the button's bounds
	        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	        // Checks if mouse position is in the range of x and y values the rectangle covers
	    }
	    
	    public void onClick() {
	        // Perform the button's action when clicked
	        System.out.println("Button '" + label + "' clicked!");
	    }
	}
