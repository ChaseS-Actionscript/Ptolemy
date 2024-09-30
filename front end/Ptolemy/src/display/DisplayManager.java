package display;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import org.lwjgl.glfw.GLFWVidMode;

public class DisplayManager {
    private long window;

    public void createDisplay(int width, int height, String title) {
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW!");
        }

        window = glfwCreateWindow(width, height, title, 0, 0);
        if (window == 0) {
            throw new IllegalStateException("Failed to create window!");
        }

        // Center window
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);

        glfwShowWindow(window);
        glfwMakeContextCurrent(window);
        createCapabilities(); // Initialize OpenGL

        // Set up OpenGL projection
        glViewport(0, 0, width, height);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, 0, height, -1, 1); // Orthographic for 2D rendering (y goes bottom-up)
        
        glMatrixMode(GL_MODELVIEW); // Prepare for object transformations
        glLoadIdentity(); // Reset any previous transforms

        glClearColor(0.0f, 0.0f, 0.0f, 1.0f); // Background color
    }

    

    
    public long getWindow() {
        return window;
    }
    

    
    public void updateDisplay() {
        glfwPollEvents();
        glfwSwapBuffers(window);
        
        
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
