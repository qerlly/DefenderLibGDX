package com.dmtrdev.monsters.sprites.enemies;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.ground.Trap;
import com.dmtrdev.monsters.sprites.armors.trees.Glove;
import com.dmtrdev.monsters.sprites.world.Tree;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.utils.Options;

public abstract class Enemy extends Sprite {

    protected enum State {RUN, DIE, ATTACK, DISABLE}

    protected PlayScreen screen;
    protected World world;
    protected Body body;
    protected int hp, takenDamage;
    protected float fireTime, disableTime, attackTime, time;
    protected final Tree tree;
    protected Animation<TextureRegion> attackAnimation, deathAnimation, runAnimation;
    protected TextureRegion textureRegion;
    protected Array<TextureRegion> frames;
    protected boolean effects, disable, direction, destroy,
            coinSpawn, setToDestroy, plantAttack, burn, attack, destroyed;

    public Enemy(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        setPosition(pX, pY);
        screen = pScreen;
        world = pScreen.getWorld();
        direction = pDirection;
        tree = pScreen.getTree();
        setToDestroy = destroy = destroyed = attack = plantAttack = burn = disable = coinSpawn = false;
        hp = takenDamage = 0;
        time = disableTime = attackTime = 0;
        frames = new Array<TextureRegion>();
        effects = new Options().getEffectCheck();
        defineEnemy();
    }

    protected State getState() {
        if (setToDestroy) {
            return State.DIE;
        } else if (disable) {
            return State.DISABLE;
        } else if (attack || plantAttack) {
            return State.ATTACK;
        } else {
            return State.RUN;
        }
    }

    protected TextureRegion getFrame() {
        switch (getState()) {
            case DIE:
                textureRegion = deathAnimation.getKeyFrame(time);
                break;
            case ATTACK:
                textureRegion = attackAnimation.getKeyFrame(attackTime);
                break;
            case RUN:
                textureRegion = runAnimation.getKeyFrame(time);
                break;
        }
        if (!direction && !textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
        } else if (direction && textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
        }
        return textureRegion;
    }

    public void setAttack(final boolean pAttack) {
        attack = pAttack;
    }

    public void setBurn(final boolean pBurn) {
        burn = pBurn;
    }

    public void setPlantAttack(final boolean pPlantAttack) {
        plantAttack = pPlantAttack;
    }

    public void draw(final Batch pBatch) {
        if (!destroyed) {
            super.draw(pBatch);
        }
    }

    public float getSize(final float pSize, final float pDifference) {
        return MathUtils.random(pDifference, pSize);
    }

    public void disabled(final int pDamage, final String pData, final boolean pDisabled) {
        hp -= pDamage;
        disable = pDisabled;
        if (hp <= 0) {
            setToDestroy = true;
        } else if (pData.equals(Trap.class.getSimpleName())) {
            disableTime = ConstArmor.TRAP_TIME;
        } else if (pData.equals(Glove.class.getSimpleName())) {
            disableTime = ConstArmor.GLOVE_TIME;
            body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y * 2);
        }
    }

    public void update(final float delta) {
        if (burn) {
            fireTime += delta;
            if (hp <= 0) {
                setToDestroy = true;
            } else if (fireTime >= 0.5f) {
                hp -= ConstArmor.FIRE_DAMAGE;
                fireTime = 0;
            }
        }
    }

    public void collisionPlant(final int pDamage) {
        plantAttack = true;
        takenDamage = pDamage;
        hp -= takenDamage;
        if (hp <= 0) {
            setToDestroy = true;
        }
    }

    public boolean getDirection() {
        return direction;
    }

    public void setDirection() {
        direction = !direction;
    }

    public Body getBody() {
        return body;
    }

    public abstract void damaged(final int pDamage, final String pData);

    public abstract int getEnemyDamage();

    public boolean getDestroyed() {
        return destroyed;
    }

    public void setCoinSpawn(final boolean pCoinSpawn) {
        coinSpawn = pCoinSpawn;
    }

    public boolean getCoinSpawn() {
        return coinSpawn;
    }

    public abstract int getScore();

    protected abstract void defineEnemy();
}