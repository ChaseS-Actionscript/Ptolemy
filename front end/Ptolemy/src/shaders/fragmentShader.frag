#version 330 core

in vec3 vertexColor;   // Color passed from the vertex shader
in vec2 fragPosition;  // Position passed from the vertex shader

out vec4 fragColor;    // Final color output

uniform int shapeType;  // 0 = rectangle, 1 = circle

void main() {
    if (shapeType == 1) {  // Circle rendering
        // Define the center of the shape in local coordinates
        vec2 center = vec2(0.0, 0.0);  // Assuming center is at (0, 0) in local coords
        float radius = 0.5;  // Set the radius for the circle

        // Compute the distance from the current fragment to the center of the shape
        float dist = length(fragPosition - center);

        // Discard fragments outside the circle's radius
        if (dist > radius) {
            discard;
        }

        // If inside the circle, set the color based on vertex color
        fragColor = vec4(vertexColor, 1.0);  // Use the vertex color
    } else {
        // Rectangle rendering (Text box)
        fragColor = vec4(vertexColor, 1.0);
    }
}
