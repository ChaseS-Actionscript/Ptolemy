package gameobjects;
import static org.lwjgl.opengl.GL33.*;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class Shape {
	private int draw_count;
	private int vbo;
	private float[] vertices;
	public Shape (float[] vertices) {
		// TODO: account for the fact that after 2 values, the next 3 values are color
		this.vertices = vertices;
		draw_count = vertices.length / 2;
		
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
		buffer.put(vertices);
		buffer.flip();
		
		vbo = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
	}

	public void render() {
		glEnableClientState(GL_VERTEX_ARRAY);
		
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glVertexPointer(2, GL_FLOAT, 0, 0);
		
		glDrawArrays(GL_TRIANGLES, 0, draw_count);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDisableClientState(GL_VERTEX_ARRAY);
		
		
	}
	
    public boolean isHovered(double mouseX, double mouseY) {
        // Check if the mouse is within the button's bounds
        return mouseX >= vertices[0] && mouseX <= vertices[2] && mouseY >= vertices[5] && mouseY <= vertices[1];
        // Checks if mouse position is in the range of x and y values the rectangle covers
    }
    
    public void onClick() {
        // Perform the button's action when clicked
        System.out.println("Button '" + "' clicked!");
    }
}
