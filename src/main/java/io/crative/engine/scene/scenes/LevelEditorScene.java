package io.crative.engine.scene.scenes;

import io.crative.engine.inputs.KeyListener;
import io.crative.engine.scene.Scene;
import io.crative.engine.window.Window;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {
    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene() {
        System.out.println("Inside LevelEditorScene");
    }

    @Override
    public void update(float deltaTime)
    {
        // System.out.println("FPS: " + (1.0f/deltaTime));

        if(!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE))
            changingScene = true;

        if(changingScene && timeToChangeScene > 0)
        {
            // Decrement the animation time
            timeToChangeScene -= deltaTime;
            // Fade window to black
            Window.get().red -= deltaTime * 5.0f;
            Window.get().green -= deltaTime * 5.0f;
            Window.get().blue -= deltaTime * 5.0f;
        } else if (changingScene)
        {
            // Change ot LevelScene
            Window.changeScene(1);
        }
    }
}
