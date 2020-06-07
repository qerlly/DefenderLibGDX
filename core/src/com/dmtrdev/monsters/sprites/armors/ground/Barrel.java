package com.dmtrdev.monsters.sprites.armors.ground;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;

public class Barrel extends Armor {

    private int mTakenDamage, mHp;
    private boolean ready;

    public Barrel(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setRegion(DefenderOfNature.getArmorsAtlas().findRegion("barrel"), 0, 0, 179, 179);
        setSize(ConstArmor.TRAP_SIZE_WIDTH - 0.6f, ConstArmor.TRAP_SIZE_HEIGHT + 0.5f);
        setOriginCenter();

        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("barrel"), i * 179, 0, 179, 179));
            destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED + 0.01f, frames);
        }
        frames.clear();
        mHp = ConstArmor.BARREL_HP;
        mTakenDamage = 0;
        ready = false;
    }

    @Override
    public void collisionEnemy() {
    }

    @Override
    public void collisionGround() {
        setToDestroy = true;
    }

    @Override
    public void collisionEnemy(final int pDamage, final boolean pCollision) {
        super.collisionEnemy(pDamage, pCollision);
        if(pDamage == 10){
            setToDestroy = true;
        }
        if (pCollision) {
            mTakenDamage += pDamage;
        } else {
            mTakenDamage -= pDamage;
        }
    }

    @Override
    public int getArmorDamage() {
        return 0;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy) {
            if (ready) {
                if (!destroy) {
                    if (playScreen.getOptions().getSoundCheck()) {
                        DefenderOfNature.getBarrelSound().play(playScreen.getOptions().getSoundVolume());
                    }
                    world.destroyBody(body);
                    destroy = true;
                    time = 0;
                } else if (destroyAnimation.isAnimationFinished(time)) {
                    destroyed = true;
                }
                setRegion(textureRegion = destroyAnimation.getKeyFrame(time));
                time += delta;
            } else {
                final float x = body.getPosition().x;
                final float y = body.getPosition().y;
                world.destroyBody(body);
                defineBarrel(x, y);
                setToDestroy = false;
                ready = true;
            }
        } else {
            if (mHp <= 0) {
                setToDestroy = true;
                time = 0;
            } else if (collisions > 0 && time >= 1f) {
                mHp -= mTakenDamage;
                time = 0;
            }
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 0.4f);
            time += delta;
        }
    }

    @Override
    protected void defineArmor() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(ConstArmor.PLANT_BODY_WIDTH, 0.01f);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT;
        fixtureDef.density = ConstArmor.BOMB_EXPLOSION_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }

    private void defineBarrel(final float pX, final float pY) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(pX, pY);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(ConstArmor.PLANT_BODY_WIDTH, ConstArmor.PLANT_BODY_HEIGHT);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_DOWN_BIT;
        fixtureDef.density = ConstArmor.PLANT_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}

