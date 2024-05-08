package io.crative.engine.ecs.components;

import io.crative.engine.ecs.Component;

public class SpriteRendererComponent extends Component
{

    private boolean first = false;

    @Override
    public void start()
    {
        System.out.println("SpriteRenderer starting");
    }

    @Override
    public void update(float deltaTime)
    {
        if(!first)
        {
            System.out.println("I am updating SpriteRenderer");
            first = true;
        }
    }
}
