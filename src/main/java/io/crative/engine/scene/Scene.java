package io.crative.engine.scene;

import io.crative.engine.render.Camera;

public abstract class Scene {

    protected Camera camera;

    public Scene(){}

    public abstract void init();
    public abstract void update(float deltaTime);

}
