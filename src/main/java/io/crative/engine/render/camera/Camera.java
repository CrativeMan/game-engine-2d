package io.crative.engine.render.camera;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera
{
    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;

    public Camera(Vector2f position)
    {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection()
    {
        projectionMatrix.identity(); // "Standard" matrix (WRONG EXPLANATION)
        // Sets the ortho camera to be 40 tiles with the size 32 wide and 21 tiles with the size 32 high
        // and sets the near clipping plane to 0 and the far clipping plane to 100
        projectionMatrix.ortho(0.0f, 32.0f * 40.0f, 0.0f, 32.0f *  21.0f, 0.0f, 100.0f);
    }

    public Matrix4f getViewMatrix()
    {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f); // Camera facing to -1 in the z direction
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f); // For the camera the positive y-axis is up
        this.viewMatrix.identity();
        viewMatrix = viewMatrix.lookAt(
                new Vector3f(position.x, position.y, 20.0f), // Where camera is in worldspace
                cameraFront.add(position.x, position.y, 0.0f), // What direction is the camera looking at
                cameraUp // What is up for the camera
        );
        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix()
    {
        return projectionMatrix;
    }

}
