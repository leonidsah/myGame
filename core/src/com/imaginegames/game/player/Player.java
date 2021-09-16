package com.imaginegames.game.player;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player {

    private float unitsToMeters, metersToUnits;

    World world;
    float x, y;

    public Player(World world, float x, float y) {
        this.world = world;
        this.x = x;
        this.y = y;


        unitsToMeters = 0.5f;
        metersToUnits = 2f;

        PolygonShape playerShape = new PolygonShape();
        playerShape.setAsBox(0.5f, 1.8f);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(x, y);
        Body body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0.3f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.5f;
        Fixture fixture = body.createFixture(fixtureDef);
        //circle.dispose();
    }
}
