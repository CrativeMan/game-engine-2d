package io.crative.engine.window;

import io.crative.engine.inputs.KeyListener;
import io.crative.engine.inputs.MouseListener;
import io.crative.engine.scene.Scene;
import io.crative.engine.scene.scenes.LevelEditorScene;
import io.crative.engine.scene.scenes.LevelScene;
import io.crative.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static io.crative.util.Constants.COLOR.*;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    private final int width;
    private final int height;
    private final String title;
    private long glfwWindow;

    public float red, green, blue, alpha;

    private static Window window = null;
    private static Scene currentScene = null;

    private Window()
    {
        this.width = 1920;
        this.height = 1080;
        this.title = "Engine - Dev";
        this.red = BG_RED;
        this.green = BG_GREEN;
        this.blue = BG_BLUE;
        this.alpha = 1.0f;
    }

    public static void changeScene(int newScene)
    {
        switch (newScene)
        {
            case 0 -> {
                currentScene = new LevelEditorScene();
                currentScene.init();
            }
            case 1 -> {
                currentScene = new LevelScene();
                currentScene.init();
            }
            default -> {
                assert false: "Unknown scene : " + newScene;
            }
        }
    }

    // So that only one window exists
    public static Window get()
    {
        // Creating a window if none exists
        if(Window.window == null)
            Window.window = new Window();

        return Window.window;
    }

    public void run()
    {
        System.out.println("Version of LWJGL : " + Version.getVersion());

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init()
    {
        // Setup error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if(!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");


        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL)
            throw new IllegalStateException("Unable to create the GLFW window");

        // MouseListener setup
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        // KeyListener setup
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable V-Sync
        // Makes it, so we run the loop at around 60 fps
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        /*
          This line is critical for LWJGL's interoperation with GLFW's
          OpenGL context, or any context that is managed externally.
          LWJGL detects the context that is current in the current thread,
          creates the GLCapabilities instance and makes the OpenGL
          bindings available for use.
         */
        GL.createCapabilities();

        Window.changeScene(0);
    }

    public void loop()
    {
        // Save time at start of frame
        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float deltaTime = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow))
        {
            // Poll events
            glfwPollEvents();

            // Set background color
            glClearColor(red, green, blue, alpha);
            glClear(GL_COLOR_BUFFER_BIT);

            if(deltaTime >= 0)
                currentScene.update(deltaTime);

            glfwSwapBuffers(glfwWindow);

            // Save the time after an entire loop run
            endTime = Time.getTime();
            // calculate deltaTime
            deltaTime = endTime - beginTime;
            // Set the new beginTime to now
            beginTime = endTime;
        }
    }
}
