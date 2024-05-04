package io.crative.engine.inputs;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener get() {
        if (MouseListener.instance == null)
            MouseListener.instance = new MouseListener();
        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos) {
        // Setting lastPos to current Pos
        get().lastX = get().xPos;
        get().lastY = get().yPos;

        // Setting current Pos to new Pos
        get().xPos = xPos;
        get().yPos = yPos;

        // Set dragging to true when one of the buttons is pressed
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        // If button is pressed and if button is in mouseButton array
        if(action == GLFW_PRESS) {
            if (button > get().mouseButtonPressed.length)
                get().mouseButtonPressed[button] = true;
        }

        // If button is released and button is in mouseButton array
        else if(action == GLFW_RELEASE) {
                if(button > get().mouseButtonPressed.length) {
                    get().mouseButtonPressed[button] = false;
                    get().isDragging = false; // Dragging set to false because yea
                }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    // * Getters ------------------------------
    public static float getX(){
        return (float) get().xPos;
    }

    public static float getY(){
        return (float) get().yPos;
    }

    public static float getDx(){
        return (float) (get().lastX - get().xPos);
    }

    public static float getDy(){
        return (float) (get().lastY - get().yPos);
    }

    public static float getScrollX(){
        return (float) get().scrollX;
    }

    public static float getScrollY(){
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public boolean getMouseButtonDown(int button) {
        if(button > get().mouseButtonPressed.length)
            return get().mouseButtonPressed[button];
        else
            return false;
    }
}
