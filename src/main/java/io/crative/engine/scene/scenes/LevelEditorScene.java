package io.crative.engine.scene.scenes;

import io.crative.engine.ecs.GameObject;
import io.crative.engine.ecs.components.FontRenderComponent;
import io.crative.engine.ecs.components.SpriteRendererComponent;
import io.crative.engine.render.Texture;
import io.crative.engine.render.camera.Camera;
import io.crative.engine.render.Shader;
import io.crative.engine.scene.Scene;
import io.crative.util.Time;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene
{
    private final float[] vertexArray =
    {
            // position               // color                    // UV Coordinates
            100.5f, 0.0f,   0.0f,     1.0f, 0.0f, 0.0f, 1.0f,     1, 1, // Bottom right 0
            0.0f,   100.5f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f,     0, 0, // Top left     1
            100.5f, 100.5f, 0.0f,     1.0f, 0.0f, 1.0f, 1.0f,     1, 0, // Top right    2
            0.0f,   0.0f,   0.0f,     1.0f, 1.0f, 0.0f, 1.0f,     0, 1 // Bottom left  3
    };

    //! IMPORTANT: Must be in counter-clockwise order
    private final int[] elementArray =
    {
            /*
                x     x

                x     x
             */
            2,1,0, // Top right triangle
            0,1,3, // Bottom left triangle
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader;

    private Texture testTexture;

    GameObject testObj;
    private boolean first = false;

    public LevelEditorScene()
    {
        this.camera = new Camera(new Vector2f());
    }

    @Override
    public void init()
    {
        //? Test gameObj
        System.out.println("Creating test object");
        this.testObj = new GameObject("Test Object");
        this.testObj.addComponent(new SpriteRendererComponent());
        this.testObj.addComponent(new FontRenderComponent());
        this.addGameObjectToScene(this.testObj);


        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("assets/images/testImage.png");

        //==============================================================
        // Generate VAO, VBO and EBO buffer objects, and send to GPU
        //==============================================================
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        //* take an array buffer, this buffer, we only draw it once
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes,
                positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes,
                (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);

    }

    @Override
    public void update(float deltaTime)
    {
        camera.position.x -= deltaTime * 20.0f;
        camera.position.y -= deltaTime * 20.0f;

        defaultShader.use();

        // Upload texture to shader
        glActiveTexture(GL_TEXTURE0); // Activate slot 0
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        testTexture.bind(); // Push Texture into slot 0

        defaultShader.uploadMatrix4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMatrix4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        glBindVertexArray(0);

        defaultShader.detach();

        if(!first)
        {
            System.out.println("Creating gameObject on runtime");
            GameObject gameObject2 = new GameObject("Test Object 2");
            gameObject2.addComponent(new SpriteRendererComponent());
            this.addGameObjectToScene(gameObject2);
            first = true;
        }

        // Updating game objects
        for(GameObject gameObject : this.gameObjects)
        {
            gameObject.update(deltaTime);
        }
    }
}
