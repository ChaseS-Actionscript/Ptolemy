package gameobjects;
import static org.lwjgl.opengl.GL33.*;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
public class Shape {
//	private int draw_count;
	private int vbo;
	private int vao;
	private float[] vertices;
	public Shape (float[] vertices) {
		
		// TODO: account for the fact that after 2 values, the next 3 values are color
		this.vertices = vertices;
//		draw_count = vertices.length / 2;
		
		vao = glGenVertexArrays();
		vbo = glGenBuffers();
		
		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
		buffer.put(vertices);
		buffer.flip();
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		
	    // Setup vertex attributes
	    glVertexAttribPointer(0, 2, GL_FLOAT, false, 5 * Float.BYTES, 0); // Position attribute
	    glEnableVertexAttribArray(0);
	    
	    glVertexAttribPointer(1, 3, GL_FLOAT, false, 5 * Float.BYTES, 2 * Float.BYTES); // Color attribute
	    glEnableVertexAttribArray(1);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
	}

	public void render() {
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClear(GL_COLOR_BUFFER_BIT);
	    
	    glBindVertexArray(vao);
	    // Draw the triangles (adjust count based on vertices array)
	    glDrawArrays(GL_TRIANGLES, 0, 6); // Total number of vertices
	    glBindVertexArray(0);
	    
	}

	
    public boolean isHovered(double mouseX, double mouseY) {
        // Check if the mouse is within the button's bounds
        return mouseX >= vertices[0] && mouseX <= vertices[5] && mouseY >= vertices[11] && mouseY <= vertices[1];
        // Checks if mouse position is in the range of x and y values the rectangle covers
    }
    
    public void onClick() {
        // Perform the button's action when clicked
        System.out.println("Button '" + "' clicked!");
    }
}
