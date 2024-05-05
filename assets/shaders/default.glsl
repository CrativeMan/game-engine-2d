#type vertex
#version 330 core
layout (location = 0) in vec3 attributePos;
layout (location = 1) in vec4 attributeColor;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fragmentColor;

void main()
{
    fragmentColor = attributeColor;
    gl_Position = uProjection * uView * vec4(attributePos, 1.0);
}

#type fragment
#version 330 core

in vec4 fragmentColor;

out vec4 color;

void main()
{
    color = fragmentColor;
}
