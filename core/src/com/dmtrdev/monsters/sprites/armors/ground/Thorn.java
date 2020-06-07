package com.dmtrdev.monsters.sprites.armors.ground;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.sprites.armors.flyings.BirdArmor;

public class Thorn extends Armor {

    private Animation<TextureRegion> mIdleAnimation;
    private int mTakenDamage;
    private int mHp;

    public Thorn(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setSize(ConstArmor.THORN_SIZE_WIDTH - 0.1f, ConstArmor.THORN_SIZE_HEIGHT);
        setOriginCenter();

        for (int j = 0; j < 7; j++) {
            for (int i = 0; i < 4; i++) {
                frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("thorn_armor"), i * 379, j * 215, 379, 215));
                mIdleAnimation = new Animation<TextureRegion>(ConstArmor.THORN_SPEED - 0.01f, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int j = 0; j < 22; j++) {
            frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("down_explode"), 0, j * 70, 354, 70));
            destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
        }
        frames.clear();
        mHp = ConstArmor.THORN_HP;
        mTakenDamage = 0;
        direction = !(body.getPosition().x <= ConstGame.X / 2);
    }

    @Override
    public void collisionEnemy() {

    }

    @Override
    public void collisionEnemy(final int pDamage, final boolean pCollision) {
        super.collisionEnemy(pDamage, pCollision);
        if(pCollision) {
            mTakenDamage += pDamage;
        } else {
            mTakenDamage -= pDamage;
        }
    }

    @Override
    public void collisionGround() {

    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.THORN_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getTomatoSound().play(playScreen.getOptions().getSoundVolume());
                }
                setPosition(body.getPosition().x - 2.3f, body.getPosition().y - 0.2f);
                world.destroyBody(body);
                setSize(ConstArmor.EFFECT_MEDIUM_SIZE + 1.7f, ConstArmor.EFFECT_MEDIUM_SIZE - 1.3f);
                time = 0;
                destroy = true;
            } else if (destroyAnimation.isAnimationFinished(time)) {
                destroyed = true;
            }
            setRegion(destroyAnimation.getKeyFrame(time));
            time += delta;
        } else if (setToDestroy) {
            world.destroyBody(body);
            destroyed = true;
        } else {
            textureRegion = mIdleAnimation.getKeyFrame(time);
            if (mHp <= 0) {
                setToDestroy = true;
                setSize(ConstArmor.EFFECT_BIG_SIZE, ConstArmor.EFFECT_BIG_SIZE);
            } else if (collisions > 0 && time >= mIdleAnimation.getAnimationDuration() / 2 && !direction) {
                mHp -= mTakenDamage;
                time = 0;
            } else if(collisions > 0 && time >= mIdleAnimation.getAnimationDuration() && direction){
                mHp -= mTakenDamage;
                time = mIdleAnimation.getAnimationDuration() / 2;
            }
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 0.3f);
            setRegion(textureRegion);
            time += delta;
            if (time >= 10f) {
                setToDestroy = true;
                time = 0;
            }
        }
    }

    @Override
    protected void defineArmor() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), ConstEnemies.SPAWN_HEIGHT + ConstArmor.PLANT_BODY_HEIGHT);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(ConstArmor.THORN_BODY_WIDTH, ConstArmor.PLANT_BODY_HEIGHT);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_DOWN_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.density = ConstArmor.PLANT_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}