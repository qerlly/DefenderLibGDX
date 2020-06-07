package com.dmtrdev.monsters.sprites.armors.ground;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.sprites.armors.flyings.BirdArmor;

public class CarnivorePlant extends Armor {

    private Animation<TextureRegion> mAttackAnimation;
    private Animation<TextureRegion> mIdleAnimation;
    private int mTakenDamage;
    private int mHp;
    private float attackTime;
    private Sound sound;

    public CarnivorePlant(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setSize(ConstArmor.PLANT_SIZE - 0.7f, ConstArmor.PLANT_SIZE);
        setOriginCenter();

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 6; i++) {
                if (j == 3 && i == 1) {
                    break;
                }
                frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("carnivorous_plant_idle"), i * 190, j * 158, 190, 158));
                mIdleAnimation = new Animation<TextureRegion>(ConstArmor.PLANT_SPEED, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 6; i++) {
                if (j == 1) {
                    break;
                }
                frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("carnivorous_plant_attack"), i * 190, j * 158, 190, 158));
                mAttackAnimation = new Animation<TextureRegion>(ConstArmor.PLANT_SPEED - 0.02f, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int j = 0; j < 22; j++) {
            frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("down_explode"), 0, j * 70, 354, 70));
            destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
        }
        frames.clear();
        mHp = ConstArmor.PLANT_HP;
        mTakenDamage = 0;
        attackTime = 0;
        direction = !(body.getPosition().x <= ConstGame.X / 2);
        sound = DefenderOfNature.getPlantSound();
        if (playScreen.getOptions().getSoundCheck()) {
            sound.loop();
            sound.play(playScreen.getOptions().getSoundVolume());
        }
    }

    public void draw(final Batch pBatch) {
        if (!destroyed) {
            super.draw(pBatch);
        }
        if (ConstGame.GAME_STATE == ConstGame.State.PAUSE) {
            sound.pause();
        } else if (playScreen.getOptions().getSoundCheck() && collisions != 0) {
            sound.resume();
        }
    }

    @Override
    public void collisionEnemy() {
    }

    @Override
    public void collisionEnemy(final int pDamage, final boolean pCollision) {
        super.collisionEnemy(pDamage, pCollision);
        if (pCollision) {
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
        return ConstArmor.CARNIVORE_PLANT_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getTomatoSound().play(playScreen.getOptions().getSoundVolume());
                }
                setSize(ConstArmor.EFFECT_MEDIUM_SIZE + 1.7f, ConstArmor.EFFECT_MEDIUM_SIZE - 1.3f);
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 0.2f);
                world.destroyBody(body);
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
        } else if (collisions != 0) {
            sound.resume();
            textureRegion = mAttackAnimation.getKeyFrame(attackTime);
            if (mHp <= 0) {
                setToDestroy = true;
                setSize(ConstArmor.EFFECT_BIG_SIZE, ConstArmor.EFFECT_BIG_SIZE);
            } else if (mAttackAnimation.isAnimationFinished(attackTime)) {
                mHp -= mTakenDamage;
                attackTime = 0;
            } else if (direction && !textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 3);
            setRegion(textureRegion);
            attackTime += delta;
        } else {
            sound.pause();
            textureRegion = mIdleAnimation.getKeyFrame(time);
            if (direction && !textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 3);
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
        shape.setAsBox(ConstArmor.PLANT_BODY_WIDTH, ConstArmor.PLANT_BODY_HEIGHT);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_DOWN_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.density = ConstArmor.PLANT_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}