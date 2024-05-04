package io.crative.engine.window;

import io.crative.engine.inputs.KeyListener;
import io.crative.engine.inputs.MouseListener;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

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

    private float r, g, b, a;
    private boolean fadeToBlack;

    private static Window window = null;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Engine - Dev";
        this.r = 0.2f;
        this.g = 0.18f;
        this.b = 0.17f;
        this.a = 1.0f;
    }

    // So that only one window exists
    public static Window get() {
        // Creating a window if none exists
        if(Window.window == null)
            Window.window = new Window();

        return Window.window;
    }

    public void run() {
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

    public void init() {
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
    }

    public void loop(){
        while(!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            // Set background color
            glClearColor(r,g,b,a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(fadeToBlack){
                r = Math.max(r - 0.01f, 0);
                g = Math.max(g - 0.01f, 0);
                b = Math.max(b- 0.01f, 0);
                a = Math.max(a - 0.01f, 0);
            }

            if(KeyListener.isKeyPressed(GLFW_KEY_SPACE))
                fadeToBlack = true;

            glfwSwapBuffers(glfwWindow);
        }
    }
}
