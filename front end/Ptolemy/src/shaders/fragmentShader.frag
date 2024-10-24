#version 330 core

in vec3 vertexColor;   // Color passed from the vertex shader
in vec2 fragPosition;  // Position passed from the vertex shader

out vec4 fragColor;    // Final color output

void main() {
    // Define the center of the shape in its local coordinates (from 0 to 1)
    vec2 center = vec2(0.5, 0.5);  // Center at 0.5, 0.5 (in local coords)
    
    // Set the radius for the circle
    float radius = 0.5;

    // Compute the distance from the current fragment to the center of the shape
    float dist = distance(fragPosition, center);

    // Debugging: Output color based on distance to center (helpful for visualizing)
    fragColor = vec4(1.0 - dist, dist, 0.0, 1.0);

    // Discard fragments outside the circle's radius
    if (dist > radius) {
        discard;
    }

    // If inside the circle, set the color
    fragColor = vec4(vertexColor, 1.0);  // Use the vertex color
}
