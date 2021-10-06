package com.imaginegames.game.screens;

/** <p>
 * Everything that deals with UI and may be placed inside {@link com.badlogic.gdx.Screen} class, is recommended
 * to place in an implementaion of this interface in order not to clutter up code with tons of UI preparations.
 * One exception are actors' setups like {@link com.badlogic.gdx.scenes.scene2d.EventListener},
 * they may be placed wherever they are able to work properly (usually it's a overriden <i>external()<i/> method
 * in anonymous class)
 * </p>
 *
 * @author leonidsah */
public interface UI {
    void show();
    void draw();
    void act(float delta);
    void dispose();
    void resize(int width, int height);
}
