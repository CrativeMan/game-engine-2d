package io.crative.engine.scene.scenes;

import io.crative.engine.scene.Scene;
import io.crative.engine.window.Window;

import static io.crative.util.Constants.COLOR.*;

public class LevelScene extends Scene
{

    public LevelScene() {
        System.out.println("Inside LevelScene");
        Window.get().red = BG_RED;
        Window.get().green = BG_GREEN;
        Window.get().blue = BG_BLUE;
    }

    @Override
    public void update(float deltaTime)
    {

    }
}
