package gameobjects;

import static org.lwjgl.opengl.GL33.*;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class Shape {
    private int vbo;
    private int vao;
    private float[] vertices;
    private float positionX, positionY;

    public Shape(float[] vertices, float posX, float posY) {
        this.vertices = vertices;
        this.positionX = posX;
        this.positionY = posY;

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

    public float getPositionX() {
        return positionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public void render() {
        glBindVertexArray(vao);
        glDrawArrays(GL_TRIANGLES, 0, 6);  // Render the triangles
        glBindVertexArray(0);
    }

    public boolean isHovered(double mouseX, double mouseY) {
        return mouseX >= vertices[0] && mouseX <= vertices[5] && mouseY <= vertices[11] && mouseY >= vertices[1];
    }

    public void onClick() {
        System.out.println("Button clicked!");
    }
}
