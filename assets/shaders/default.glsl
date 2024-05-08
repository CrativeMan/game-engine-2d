#type vertex
#version 330 core
layout (location = 0) in vec3 attributePos;
layout (location = 1) in vec4 attributeColor;
layout (location = 2) in vec2 aTexCords;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fragmentColor;
out vec2 fTexCords;

void main()
{
    fragmentColor = attributeColor;
    fTexCords = aTexCords;
    gl_Position = uProjection * uView * vec4(attributePos, 1.0);
}

#type fragment
#version 330 core

uniform float uTime;
uniform sampler2D TEX_SAMPLER;

in vec4 fragmentColor;
in vec2 fTexCords;

out vec4 color;

void main()
{
    color = texture(TEX_SAMPLER, fTexCords);
}
