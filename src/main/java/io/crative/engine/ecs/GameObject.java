package io.crative.engine.ecs;

import java.util.ArrayList;
import java.util.List;

public class GameObject
{
    private String name;
    private List<Component> components;

    public GameObject(String name)
    {
        this.name = name;
        this.components = new ArrayList<>();
    }

    public <T extends Component> T getComponent(Class<T> componentClass)
    {
        for(Component component : components)
        {
            if(componentClass.isAssignableFrom(component.getClass())) // checks if c is same/super from component class
            {
                try
                {
                    return componentClass.cast(component);
                }
                catch(ClassCastException e)
                {
                    e.printStackTrace();
                    assert false: "Error: Casting component";
                }
            }
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass)
    {
        for(int i = 0; i < components.size(); i++)
        {
            Component component = components.get(i);
            if(componentClass.isAssignableFrom(component.getClass()))
            {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component component) {
        this.components.add(component);
        component.gameObject = this;
    }

    public void start()
    {
        for (int i = 0; i < components.size(); i++)
        {
            components.get(i).start();
        }
    }

    public void update(float deltaTime)
    {
        for (int i = 0; i < components.size(); i++)
        {
            components.get(i).update(deltaTime);
        }
    }

}
