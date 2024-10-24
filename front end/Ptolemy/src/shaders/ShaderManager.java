package shaders;

import static org.lwjgl.opengl.GL33.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ShaderManager {
    private int programID;

    public ShaderManager(String vertexPath, String fragmentPath) throws IOException {
        // Load vertex and fragment shader source code
        String vertexCode = new String(Files.readAllBytes(Paths.get(vertexPath)));
        String fragmentCode = new String(Files.readAllBytes(Paths.get(fragmentPath)));
        
        // Compile shaders
        int vertexShader = compileShader(GL_VERTEX_SHADER, vertexCode);
        int fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragmentCode);
        
        // Link shaders into a program
        programID = glCreateProgram();
        glAttachShader(programID, vertexShader);
        glAttachShader(programID, fragmentShader);
        glLinkProgram(programID);
        
        // Check if linking was successful
        if (glGetProgrami(programID, GL_LINK_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Shader linking failed: " + glGetProgramInfoLog(programID));
        }
        
        // Cleanup: detach and delete shaders after linking
        glDetachShader(programID, vertexShader);
        glDetachShader(programID, fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    private int compileShader(int type, String code) {
        int shaderID = glCreateShader(type);
        glShaderSource(shaderID, code);
        glCompileShader(shaderID);
        
        // Check for compilation errors
        if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
            throw new IllegalStateException("Shader compilation failed: " + glGetShaderInfoLog(shaderID));
        }
        
        return shaderID;
    }
    public int getProgramID() {
    	return programID;
    }
    public void use() {
        glUseProgram(programID);
    }

    public void cleanup() {
        glDeleteProgram(programID);
    }
}
