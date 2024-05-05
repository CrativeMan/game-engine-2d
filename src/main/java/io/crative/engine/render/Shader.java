package io.crative.engine.render;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader
{
    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;
    private String filePath;

    public Shader(String filepath)
    {
        this.filePath = filepath;
        try
        {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // Find first pattern after #type 'pattern'
            int index = source.indexOf("#type") + 6; // beginning of next word after "#inxed"
            int endOfLine = source.indexOf("\n", index); // \n only on linux and mac on windows its \r\n
            String firstPattern = source.substring(index, endOfLine).trim();

            // Find second pattern after #type 'pattern'
            index = source.indexOf("#type", endOfLine) + 6;
            endOfLine = source.indexOf("\n", index);
            String secondPattern = source.substring(index, endOfLine).trim();

            // Stores source code from shader in source strings
            if(firstPattern.equals("vertex"))
                vertexSource = splitString[1];
            else if(firstPattern.equals("fragment"))
                fragmentSource = splitString[1];
            else
                throw new IOException("Unexpected token '" + firstPattern + "'");

            if(secondPattern.equals("vertex"))
                vertexSource = splitString[2];
            else if(secondPattern.equals("fragment"))
                fragmentSource = splitString[2];
            else
                throw new IOException("Unexpected token '" + secondPattern + "'");

        } catch (IOException e)
        {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }

    }

    public void compile()
    {
        //============================
        // Compile and link shaders
        //============================
        int vertexID, fragmentID;

        // Load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Check for errors in compilations process
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH); // Gets the length of the error message
            System.out.println("ERROR: '" + filePath + "'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        // Load and compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // Check for errors in compilations process
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH); // Gets the length of the error message
            System.out.println("ERROR: '" + filePath + "'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // Link shaders
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if(success == GL_FALSE)
        {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filePath + "'\n\tLinking shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void use()
    {
        // Bind shader program
        glUseProgram(shaderProgramID);
    }

    public void detach()
    {
        glUseProgram(0);
    }

    public void uploadMatrix4f(String varName, Matrix4f matrix4)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, varName); // gets the location of the uniform variable
        // creates a buffer with the size of the matrix (4x4 | 16)
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        matrix4.get(matrixBuffer);
        glUniformMatrix4fv(varLocation, false, matrixBuffer); // Will be explained in the next video
    }

}
