package com.imaginegames.game.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class TextureButton extends ImageButton {

    public TextureButton(Texture imageUp, Texture imageDown) {
        super(new SpriteDrawable(new Sprite(imageUp)), new SpriteDrawable(new Sprite(imageDown)));
    }
}
