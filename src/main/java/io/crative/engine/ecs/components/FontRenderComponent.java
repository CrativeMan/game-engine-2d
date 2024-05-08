package io.crative.engine.ecs.components;

import io.crative.engine.ecs.Component;

public class FontRenderComponent extends Component
{

    @Override
    public void start()
    {
        if(gameObject.getComponent(SpriteRendererComponent.class) != null)
        {
            System.out.println("Found Font Renderer component");
        }
    }

    @Override
    public void update(float deltaTime)
    {

    }
}
