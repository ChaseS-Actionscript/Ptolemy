import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import display.DisplayManager;
import gameobjects.Shape;
import shaders.ShaderManager;
import java.io.IOException;
import org.joml.Matrix4f;
public class Main {
	private static boolean isShapeVisible(Shape shape, float cameraX, float cameraY, float left, float right, float bottom, float top) {
	    float shapeX = shape.getPositionX();
	    float shapeY = shape.getPositionY();

	    // Check if any vertex is within the camera view
	    float[] vertices = shape.getVertices();
	    for (int i = 0; i < vertices.length; i += 5) { // Step by 5 for each vertex (2 for position, 3 for color)
	        float x = vertices[i] + shapeX;  // Adjust for shape's position
	        float y = vertices[i + 1] + shapeY;  // Adjust for shape's position

	        if (x + cameraX >= left && x + cameraX <= right && y + cameraY >= bottom && y + cameraY <= top) {
	            return true; // At least one vertex is visible
	        }
	    }
	    return false; // No vertices are visible
	}
    private static DisplayManager displayManager;
    private static Shape testShape;
    private static ShaderManager shaderManager;
    private static Shape testShape2;
    public static void main(String[] args) {
        displayManager = new DisplayManager();
        displayManager.createDisplay(640, 480, "Ptolemy");
        long window = displayManager.getWindow();
        // Load and compile shaders
        try {
            shaderManager = new ShaderManager(System.getProperty("user.dir")+"/front end/Ptolemy/src/shaders/vertexShader.vert", System.getProperty("user.dir")+"/front end/Ptolemy/src/shaders/fragmentShader.frag");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // Set up callback for resizing window
        glfwSetFramebufferSizeCallback(window, (windowHandle, width, height) -> {
            glViewport(0, 0, width, height);
        });
        
        // Define vertices for the shape (with positions and colors)
        float[] vertices = new float[] {
        	    100, 100, 1f, 0f, 0f, // top left
        	    200, 100, 0f, 1f, 0f, // top right
        	    200, 200, 0f, 0f, 1f, // bottom right

        	    100, 100, 1f, 0f, 0f, // top left
        	    100, 200, 1f, 0f, 1f, // bottom left
        	    200, 200, 0f, 0f, 1f  // bottom right
        	};
        float[] vertices2 = new float[] {
        	    700, 100, 1f, 0f, 0f, // top left
        	    200, 100, 0f, 1f, 0f, // top right
        	    200, 200, 0f, 0f, 1f, // bottom right

        	    700, 100, 1f, 0f, 0f, // top left
        	    700, 200, 1f, 0f, 1f, // bottom left
        	    200, 200, 0f, 0f, 1f  // bottom right
        	};
        testShape = new Shape(vertices, 100, 100);
        testShape2 = new Shape(vertices2, 700, 100);
     // Zoom factor
        float[] zoomFactor = {1.0f};

        // Scroll callback to zoom in/out
        glfwSetScrollCallback(window, (long win, double xOffset, double yOffset) -> {
            if (yOffset < 0) {
                zoomFactor[0] *= 1.1f;  // Zoom in
            } else if (yOffset > 0) {
                zoomFactor[0] /= 1.1f;  // Zoom out
            }
        });

        // Apply zoom to projection matrix
        int windowWidth = displayManager.getDim(window)[0];
        int windowHeight = displayManager.getDim(window)[1];
        float aspectRatio = (float) windowWidth / windowHeight;
        float left= -100.0f * aspectRatio;
        float right = 100.0f * aspectRatio;
        float bottom = -100.0f;
        float top = 100.0f;
        Matrix4f projectionMatrix = new Matrix4f().ortho(left * zoomFactor[0], right * zoomFactor[0], bottom * zoomFactor[0], top * zoomFactor[0], 0, 0);

        // Set mouse button callback
        boolean[] dragging = {false};
        double[] prevX = new double[1];
        double[] prevY = new double[1];
        double someScaleFactor = 1.0;
        float[] cameraX = {0.0f};
        float[] cameraY = {0.0f};
        double[] deltaX = new double[1];
        double[] deltaY = new double[1];
        glfwSetMouseButtonCallback(window, (long win, int button, int action, int mods) -> {
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
                dragging[0] = true;
                glfwGetCursorPos(win, prevX, prevY);
                double mouseX = prevX[0];
                double mouseY = prevY[0];
                
                int[] width = new int[1];
                int[] height = new int[1];
                glfwGetWindowSize(window, width, height);
                
                System.out.println(mouseX + ", " + mouseY);
                if (testShape.isHovered(mouseX, mouseY)) {
                    testShape.onClick();
                }
            }
            else if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE) {
                dragging[0] = false;
            }
        });
     // Mouse position callback to handle dragging
        glfwSetCursorPosCallback(window, (win, xpos, ypos) -> {
            if (dragging[0]) {
                deltaX[0] = xpos - prevX[0];
                deltaY[0] = ypos - prevY[0];

                // Update camera position
                cameraX[0] += deltaX[0] * someScaleFactor;
                cameraY[0] += -deltaY[0] * someScaleFactor;

                // Store the new cursor position
                prevX[0] = xpos;
                prevY[0] = ypos;
            }
        });

        // Apply translation to view matrix (for example, in 2D)
        Matrix4f viewMatrix = new Matrix4f().translate(cameraX[0], cameraY[0], 0);
        
        int projectionMatrixLocation = glGetUniformLocation(shaderManager.getProgramID(), "projection");

        // Set uniform for the view matrix if applicable
        // viewMatrix = ...; // Update based on camera position/rotation
        int viewMatrixLocation = glGetUniformLocation(shaderManager.getProgramID(), "view");

        // Main game loop
        while (!displayManager.shouldClose()) {
        	displayManager.clearScreen();
            
            // Update the zoom factor dynamically
            windowWidth = displayManager.getDim(window)[0];
            windowHeight = displayManager.getDim(window)[1];
            aspectRatio = (float) windowWidth / windowHeight;
            left = 0.0f;
            right = windowWidth+300;   // 640
            bottom = -240.0f; // 480
            top = 240.0f;
            
            // Update projection matrix on every frame to reflect zoom factor changes
            projectionMatrix.identity().ortho(left * zoomFactor[0], right * zoomFactor[0], bottom * zoomFactor[0], top * zoomFactor[0], -1.0f, 1.0f);
            
            // Update view matrix based on cameraX and cameraY (which are updated on dragging)
            viewMatrix.identity().translate(cameraX[0], cameraY[0], 0);
            
            // Use the shader program
            shaderManager.use();
            
            // Send updated projection and view matrices to the shader
            glUniformMatrix4fv(projectionMatrixLocation, false, projectionMatrix.get(new float[16]));
            glUniformMatrix4fv(viewMatrixLocation, false, viewMatrix.get(new float[16]));
            
            // Render the shape
            testShape.render();
            testShape2.render();
            // Update the display
            displayManager.updateDisplay();
            
        }

        displayManager.closeDisplay();  // Clean up when window is closed
        shaderManager.cleanup();        // Clean up shaders
    }
    
}
