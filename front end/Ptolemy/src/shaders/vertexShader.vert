#version 330 core

layout(location = 0) in vec2 position;   // Vertex position
layout(location = 1) in vec3 aColor;     // Vertex color

out vec3 vertexColor;  // Color to pass to the fragment shader
out vec2 fragPosition; // Position to pass to the fragment shader

uniform mat4 projection;
uniform mat4 view;
uniform int shapeType;  // 0 = rectangle, 1 = circle

void main() {
    vertexColor = aColor;

    // For rectangles, just pass the position as-is
    fragPosition = position;

    // Compute the final position in NDC space using projection and view matrices
    gl_Position = projection * view * vec4(position, 0.0, 1.0);
}
