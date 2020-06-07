package com.dmtrdev.monsters.sprites.armors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.utils.Options;

public abstract class Armor extends Sprite {

    protected World world;
    protected Body body;
    protected PlayScreen playScreen;
    protected boolean destroy, direction, setToDestroy, destroyed, effects;
    protected TextureRegion textureRegion;
    protected Array<TextureRegion> frames;
    protected Animation<TextureRegion> destroyAnimation;
    protected float time;
    public byte collisions;

    public Armor(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        world = pPlayScreen.getWorld();
        playScreen = pPlayScreen;
        effects = new Options().getEffectCheck();
        direction = pDirection;
        setPosition(pX, pY);
        defineArmor();
        destroyed = destroy = setToDestroy = false;
        frames = new Array<TextureRegion>();
        time = 0;
        collisions = 0;
    }

    public void draw(final Batch pBatch) {
        if (!destroyed) {
            super.draw(pBatch);
        }
    }

    public void collisionEnemy(final int pDamage, final boolean pCollision) {
        if (pCollision) {
            collisions++;
        } else {
            collisions--;
        }
    }

    public boolean getDirection() {
        return direction;
    }

    public boolean getDestroyed() {
        return destroyed;
    }

    public abstract void collisionEnemy();

    public abstract void collisionGround();

    public abstract int getArmorDamage();

    public abstract void update(final float delta);

    protected abstract void defineArmor();
}