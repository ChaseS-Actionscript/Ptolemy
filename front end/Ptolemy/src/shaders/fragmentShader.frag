#version 330 core

in vec3 vertexColor;   // Color passed from the vertex shader
in vec2 fragPosition;  // Position passed from the vertex shader

out vec4 fragColor;    // Final color output

uniform int shapeType;  // 0 = rectangle, 1 = circle

void main() {
    // For rectangles, just set the color based on vertex color
    fragColor = vec4(vertexColor, 1.0);  // Use the vertex color
}
