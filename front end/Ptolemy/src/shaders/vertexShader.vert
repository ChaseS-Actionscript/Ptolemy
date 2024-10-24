#version 330 core

layout(location = 0) in vec2 position;   // Vertex position
layout(location = 1) in vec3 aColor;     // Vertex color

out vec3 vertexColor;  // Color to pass to the fragment shader
out vec2 fragPosition; // Position to pass to the fragment shader

uniform mat4 projection;
uniform mat4 view;

void main() {
    // Pass the vertex color to the fragment shader
    vertexColor = aColor;

    // Normalize the position to the range [0, 1] relative to the shape's bounds
    fragPosition = (position - vec2(100.0, 100.0)) / vec2(100.0, 50.0);  // Adjust based on shape size (100x50 rectangle)

    // Compute the final position in NDC space
    gl_Position = projection * view * vec4(position, 0.0, 1.0);
}
