import static org.lwjgl.glfw.GLFW.*;
import display.DisplayManager;
import static org.lwjgl.opengl.GL11.*;
import gameobjects.Node;
import gameobjects.Shape;

public class Main {
    private static DisplayManager displayManager;
    private static Node testNode;
    private static Shape testShape;
    public static void main(String[] args) {
        displayManager = new DisplayManager();
        displayManager.createDisplay(640, 480, "Ptolemy");
        long window = displayManager.getWindow();
        // Ensures that after resizing, any shapes on screen keep their on hover properties
        glfwSetFramebufferSizeCallback(window, (windowHandle, width, height) -> {
            glViewport(0, 0, width, height);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity(); // Reload identity
            glOrtho(0, width, 0, height, -1, 1); // Adjust orthographic projection based on new size
            glMatrixMode(GL_MODELVIEW);
        });
        // TODO fix coupling
/*        int width1 = displayManager.getDim(window)[0];
        int height1 = displayManager.getDim(window)[1];
        IntFunction<Float> ndcX = pos -> (float)2*((float)pos/(float)width1)-(float)1;
        IntFunction<Float> ndcY = pos -> (float)2*((float)pos/(float)height1)-(float)1;
        */float[] vertices = new float[] {
        	    100, 150,  // top left
        	    200, 150,  // top right
        	    200, 100,  // bottom right
        	       
        	    100, 150,  // top left
        	    100, 100, // bottom left
        	    200, 100,  // bottom right
        	};/*
        for (int i=0; i < vertices.length-1; i+=2) {
        	vertices[i] = ndcX.apply((int)vertices[i]);
        	System.out.println(vertices[i]);
        }
        for (int i=1; i < vertices.length; i+=2) {
        	vertices[i] = ndcY.apply((int)vertices[i]);
        	System.out.println(vertices[i]);
        }*/
        testShape = new Shape(vertices); // Testing VBO instead of display lists because of performance gains
        //testNode = new Node(640/2, 200, 100, 50, "Click Me");
        // This is sort of like "When mouse button clicked"
        glfwSetMouseButtonCallback(window, (long win, int button, int action, int mods) -> {
            if (button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
            	// Sets arrays of size 1 because GLFW requires cursor position to be passed by reference, so not just double literals
            	// For future reference this looks like how &variable is in c++
                double[] xpos = new double[1];
                double[] ypos = new double[1];
                glfwGetCursorPos(win, xpos, ypos);
                int[] width = new int[1];
                int[] height = new int[1];
                glfwGetWindowSize(window, width, height);
                
                
                // Adjust for OpenGL coordinates (inverted Y-axis)
                double mouseX = xpos[0];
                double mouseY = height[0] - ypos[0];
                System.out.println(mouseX + ", " + mouseY);
                if (Main.testShape.isHovered(mouseX, mouseY)) {
                    Main.testShape.onClick();
                }
                
            }
        });


        // Main game loop
        while (!displayManager.shouldClose()) {
        	displayManager.clearScreen();
            //testNode.render();
            testShape.render();
            // Update the display
            displayManager.updateDisplay();

        }

        displayManager.closeDisplay(); // Clean up
    }

}
   