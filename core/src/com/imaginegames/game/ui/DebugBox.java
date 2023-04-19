package com.imaginegames.game.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class DebugBox extends Container<ScrollPane> {
    ScrollPane scrollPane;
    Label label;

    public DebugBox(Skin skin) {
        super();
        label = new Label("text\ntext\ntext\ntext\ntext\ntext\ntext\ntext\ntext\ntext\ntext\ntext\ntext\ntext\ntext", skin);
        scrollPane = new ScrollPane(label, skin);
        super.setActor(scrollPane);
        super.height(150);
    }

    //public void update(Pair<String, Object> descriptionAndObject, int periodInMilliseconds) {}

}
