package display;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class DisplayManager{
    private long window;

    public void createDisplay(int width, int height, String title) {
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW!");
        }

        window = glfwCreateWindow(width, height, title, 0, 0);
        if (window == 0) {
            throw new IllegalStateException("Failed to create window!");
        }
        
        GLFW.glfwWindowHint(GL_MAJOR_VERSION, 3); GLFW.glfwWindowHint(GL_MINOR_VERSION, 3);
        GLFW.glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); // Set to OpenGL Programmable Pipeline
        // Center window
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
        
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        createCapabilities(); // Initialize OpenGL

        // Set up OpenGL projection
        glViewport(0, 0, width, height);
        glfwSwapInterval(0);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Background color
    }

    

    
    public long getWindow() {
        return window;
    }
    

    
    public void updateDisplay() {
        glfwPollEvents();
        glfwSwapBuffers(window);
        
        
    }
    
    public int[] getDim(long window) {
    	int[] width = new int[1];
    	int[] height = new int[1];
    	glfwGetWindowSize(window, width, height);
    	return new int[] {width[0],height[0]};
    }
    
    public void clearScreen() {
    	glClear(GL_COLOR_BUFFER_BIT);
    }

    public void closeDisplay() {
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }
}
