package gameobjects;
import static org.lwjgl.opengl.GL30.*;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class Shape {
	private int draw_count;
	private int vbo;
	public Shape (float[] vertices) {
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
}
