import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWVidMode;

public class Main {
	public static void main(String[] args) {
		if (!glfwInit()) {
			throw new IllegalStateException("Failed to initialize GLFW!");
		}
		
		long window = glfwCreateWindow(640, 480, "Ptolemy", 0, 0);
		if (window == 0) {
			throw new IllegalStateException("Failed to create window!");
		}
		
		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (videoMode.width() - 640) / 2, (videoMode.height() - 480) / 2);
		
		glfwShowWindow(window);
		while (!glfwWindowShouldClose(window)) {
           
            glfwPollEvents();
            
            // Create a sweet spot between nodes to prevent cluttering
            // Any nodes that are not connected to eachother should be at a distance away from eachother equal to (sweet spot)*1.5
            
            glfwSwapBuffers(window);
        }
        
        // Clean up and terminate GLFW
        glfwDestroyWindow(window);
        glfwTerminate();
	}
}
