package io.crative.engine.scene;

public abstract class Scene {

    public Scene(){}

    public abstract void init();
    public abstract void update(float deltaTime);

}
