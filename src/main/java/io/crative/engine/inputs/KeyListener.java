package io.crative.engine.inputs;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];

    private KeyListener() {}

    public static KeyListener get() {
        if (KeyListener.instance == null)
            KeyListener.instance = new KeyListener();
        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        // If key is pressed and in keyPressed array then set key to true
        if(action == GLFW_PRESS) {
            if(key < get().keyPressed.length)
                get().keyPressed[key] = true;
        }

        // If key is released and in keyPressed array then set key to false
        else if (action == GLFW_RELEASE){
            if(key < get().keyPressed.length)
                get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int key) {
        return get().keyPressed[key];
    }
}
