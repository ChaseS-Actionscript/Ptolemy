import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import display.DisplayManager;
import gameobjects.Shape;
import shaders.ShaderManager;
import java.io.IOException;
import org.joml.Matrix4f;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
public class Main {
    private static DisplayManager displayManager;

    private static ShaderManager shaderManager;
    public static void main(String[] args) throws IOException, InterruptedException {
        displayManager = new DisplayManager();
        displayManager.createDisplay(640, 480, "Ptolemy");
        long window = displayManager.getWindow();
        int windowWidth = displayManager.getDim(window)[0];
        int windowHeight = displayManager.getDim(window)[1];
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


        // Define vertices for the circle (using 6 vertices for a simple approximation)
        int max = 300;
        int min = 0;
        int range = max - min + 1;
        float[] circleVertices = new float[] {

            300, 300, 1f, 0f, 0f, 
            350, 300, 0f, 1f, 0f, 
            350, 350, 0f, 0f, 1f, 

            300, 300, 1f, 0f, 0f, 
            300, 350, 1f, 0f, 1f, 
            350, 350, 0f, 0f, 1f  
        };



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
        int viewMatrixLocation = glGetUniformLocation(shaderManager.getProgramID(), "view");

        // Main game loop
        Scanner scanner = new Scanner(System.in); System.out.print("Enter URL: ");
        String arg1 = scanner.nextLine(); // Reads the entire line
    	String pythonScriptPath = System.getProperty("user.dir")+"\\web crawling\\main.py";
        System.out.println(pythonScriptPath);
        ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath, arg1);
        Process p = pb.start();
        p.waitFor();
        ArrayList<Shape> nodes = new ArrayList<Shape>();
        String csvFile = System.getProperty("user.dir")+"\\tmp.csv";
        String line;
        Shape node;
        scanner.close();
    	try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                // Split the line by commas
                String[] urls = line.split(",");
                
                // Print out each URL
                for (String url : urls) {
                	float randomX = (float)(Math.random() * range) + min;
                	float randomY = (float)(Math.random() * range) + min;
                    // Randomize the position (x and y coordinates) of each vertex
                    float[] vertices = new float[circleVertices.length];
                    System.arraycopy(circleVertices, 0, vertices, 0, circleVertices.length);
                	Random random = new Random();
                    for (int i = 0; i < circleVertices.length; i += 5) {
                        // Apply a random factor to x and y (positions)
                        float randomXOffset = random.nextFloat() * 3000 - 25; 
                        float randomYOffset = random.nextFloat() * 3000 - 25; 

                        vertices[i] = circleVertices[i] + randomXOffset;   // Update X position
                        vertices[i+1] = circleVertices[i + 1] + randomYOffset; // Update Y position
                    }
                    node = new Shape(vertices, 1, url.trim()); // Use trim() to remove any extra spaces
                    nodes.add(node);
                    System.out.println(randomX + ", " + randomY);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!displayManager.shouldClose()) {

        	displayManager.clearScreen();
            
            // Update the zoom factor dynamically
            windowWidth = displayManager.getDim(window)[0];
            windowHeight = displayManager.getDim(window)[1];
            aspectRatio = (float) windowWidth / windowHeight;
            left = -300.0f;
            right = 300.0f;   // 640
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
            //testShape.render(shaderManager.getProgramID());   // Render rectangle (text box)
            for(Shape shape : nodes) {
            	//
            	
            	
            	shape.render(shaderManager.getProgramID());
            }
            //circleShape.render(shaderManager.getProgramID()); // Render circle
            // Update the display
            displayManager.updateDisplay();
            
        }
        File file = new File(csvFile);
        file.delete();

        displayManager.closeDisplay();  // Clean up when window is closed
        shaderManager.cleanup();        // Clean up shaders
    }
    
}
