package io.crative.engine.scene;

import io.crative.engine.ecs.GameObject;
import io.crative.engine.render.camera.Camera;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene(){}

    public void start()
    {
        for(GameObject gameObject: gameObjects)
        {
            gameObject.start();
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject gameObject)
    {
        if(isRunning)
        {
            gameObjects.add(gameObject);
            gameObject.start();
        }
        else
        {
            gameObjects.add(gameObject);
        }
    }

    public void init() {};
    public abstract void update(float deltaTime);

}
