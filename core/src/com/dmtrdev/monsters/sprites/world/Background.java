package com.dmtrdev.monsters.sprites.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;

public class Background extends Sprite {

    private final World mWorld;
    private final Sprite mClouds;

    public Background(final World pWorld) {
        mWorld = pWorld;
        mClouds = new Sprite();
        defineBackground();
        setRegion(DefenderOfNature.getWorldAtlas().findRegion("background"));
        setBounds(0, ConstGame.SPAWN_HEIGHT - 2.5f, ConstGame.X, ConstGame.Y);
        mClouds.setRegion(DefenderOfNature.getWorldAtlas().findRegion("clouds"));
        mClouds.setBounds(0, 14.5f, ConstGame.X - 11, 5);
    }

    @Override
    public void draw(final Batch batch) {
        super.draw(batch);
        mClouds.draw(batch);
    }

    public void update() {
        mClouds.setPosition(ConstGame.CLOUDS_SPEED + mClouds.getX(), 15);
        if (mClouds.getX() >= ConstGame.X) {
            mClouds.setX(-20);
        }
    }

    private void defineBackground() {
        final ChainShape chainShape = new ChainShape();
        chainShape.createChain(new Vector2[]{new Vector2(-ConstGame.WORLD_SIDES, ConstEnemies.SPAWN_HEIGHT), new Vector2(ConstGame.X + ConstGame.WORLD_SIDES, ConstEnemies.SPAWN_HEIGHT),
                new Vector2(ConstGame.X + ConstGame.WORLD_SIDES, ConstGame.Y + 10), new Vector2(-ConstGame.WORLD_SIDES, ConstGame.Y + 10), new Vector2(-ConstGame.WORLD_SIDES, 0)});

        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.StaticBody;
        final Body body = mWorld.createBody(bodyDef);
        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = chainShape;
        fixtureDef.filter.categoryBits = ConstGame.GROUND_BIT;
        fixtureDef.filter.maskBits = ConstGame.FIRE_BIT | ConstGame.ENEMY_BIT | ConstGame.ARMOR_BIT |
                ConstGame.PLAYER_BIT | ConstGame.COIN_BIT | ConstGame.DISABLE_BIT | ConstGame.ENEMY_DOWN_BIT | ConstGame.ENEMY_SHOTS_BIT | ConstGame.ARMOR_ENEMY_BIT | ConstGame.SKY_ARMOR_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        final ChainShape chainShapeSky = new ChainShape();
        chainShapeSky.createChain(new Vector2[]{new Vector2(-ConstGame.WORLD_SIDES, ConstEnemies.FLY_SPAWN_HEIGHT), new Vector2(ConstGame.X + ConstGame.WORLD_SIDES, ConstEnemies.FLY_SPAWN_HEIGHT)});
        fixtureDef.shape = chainShapeSky;
        fixtureDef.density = ConstArmor.BOMB_EXPLOSION_DENSITY;
        fixtureDef.filter.categoryBits = ConstGame.SKY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_SHOTS_BIT | ConstGame.ARMOR_ENEMY_BIT;
        body.createFixture(fixtureDef).setUserData(this);

        final ChainShape chainShapeSkyBottom = new ChainShape();
        chainShapeSkyBottom.createChain(new Vector2[]{new Vector2(-ConstGame.WORLD_SIDES, ConstEnemies.FLY_SPAWN_HEIGHT - 8), new Vector2(ConstGame.X + ConstGame.WORLD_SIDES, ConstEnemies.FLY_SPAWN_HEIGHT - 8)});
        fixtureDef.shape = chainShapeSkyBottom;
        fixtureDef.filter.categoryBits = ConstGame.SKY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_SHOTS_BIT | ConstGame.ARMOR_ENEMY_BIT;
        body.createFixture(fixtureDef).setUserData(this);
    }
}