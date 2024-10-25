package gameobjects;

import static org.lwjgl.opengl.GL33.*;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class Shape {
    private int vbo;
    private int vao;
    private float[] vertices;
    private int shapeType;
    private String url;
    public Shape(float[] vertices, int shapeType, String url) {
        this.vertices = vertices;
        this.shapeType = shapeType;
        this.url = url;
        vao = glGenVertexArrays();
        vbo = glGenBuffers();

        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
        buffer.put(vertices);
        buffer.flip();
        glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        // Setup vertex attributes
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 5 * Float.BYTES, 0); // Position
        glEnableVertexAttribArray(0);
        
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 5 * Float.BYTES, 2 * Float.BYTES); // Color
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }
    public float[] getVertices() {
        return vertices;
    }
    
    public String getUrl() {
    	return url;
    }

    public void render(int shaderProgramID) {
        glBindVertexArray(vao);

        // Send the shapeType uniform to the shader
        int shapeTypeLocation = glGetUniformLocation(shaderProgramID, "shapeType");
        glUniform1i(shapeTypeLocation, shapeType);  // Set shapeType in the shader

        glDrawArrays(GL_TRIANGLES, 0, 6);  // Render the shape
        glBindVertexArray(0);
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= vertices[0] && mouseX <= vertices[5] && mouseY <= vertices[11] && mouseY >= vertices[1];
    }

    public void onClick() {
        System.out.println("Button clicked!");
    }
}
