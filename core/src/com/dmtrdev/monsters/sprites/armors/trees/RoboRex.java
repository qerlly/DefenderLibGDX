package com.dmtrdev.monsters.sprites.armors.trees;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;

public class RoboRex extends Armor {

    private final Sound sound;
    private Animation<TextureRegion> mIdleAnimation;
    private Animation<TextureRegion> mAttackAnimation;
    private boolean mFlag;

    public RoboRex(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setSize(5.2f, 6);
        setOriginCenter();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("roborex_run"), j * 289, i * 275, 289, 275));
                mAttackAnimation = new Animation<TextureRegion>(0.06f, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("roborex_idle"), j * 289, i * 275, 289, 275));
                mIdleAnimation = new Animation<TextureRegion>(0.1f, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("machine_explodetxt"), j * 337, i * 337, 337, 337));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
            }
        }
        frames.clear();
        mFlag = false;
        sound = DefenderOfNature.getRexSound();
    }

    @Override
    public void collisionEnemy() {
        collisions++;
        if (collisions >= 5) {
            setToDestroy = true;
        }
    }

    @Override
    public void collisionGround() {
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.ROBOREX_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                sound.stop();
                setSize(ConstArmor.EFFECT_BIG_SIZE - 1, ConstArmor.EFFECT_BIG_SIZE);
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 1.25f);
                world.destroyBody(body);
                time = 0;
                destroy = true;
            } else if (destroyAnimation.isAnimationFinished(time)) {
                destroyed = true;
            }
            setRegion(textureRegion = destroyAnimation.getKeyFrame(time));
            time += delta;
        } else if (setToDestroy) {
            world.destroyBody(body);
            destroyed = true;
        } else {
            textureRegion = mAttackAnimation.getKeyFrame(time);
            if (!textureRegion.isFlipX() && direction) {
                textureRegion.flip(true, false);
            }
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            if (mFlag) {
                if (direction) {
                    body.setLinearVelocity(9, -10);
                } else {
                    body.setLinearVelocity(-9, -10);
                }
                setRegion(textureRegion);
                time += delta;
                if (time >= 2.5f) {
                    setToDestroy = true;
                }
            } else {
                textureRegion = mIdleAnimation.getKeyFrame(time);
                if (!textureRegion.isFlipX() && direction) {
                    textureRegion.flip(true, false);
                }
                setRegion(textureRegion);
                time += delta;
                if (mIdleAnimation.isAnimationFinished(time)) {
                    mFlag = true;
                    world.destroyBody(body);
                    defineArmor();
                    if(playScreen.getOptions().getSoundCheck()) {
                        sound.play(playScreen.getOptions().getSoundVolume());
                    }
                    time = 0;
                }
            }
        }
    }

    @Override
    public void draw(Batch pBatch) {
        if (!destroyed) {
            super.draw(pBatch);
        }
        if(ConstGame.GAME_STATE == ConstGame.State.PAUSE && mFlag){
            sound.pause();
        } else if(playScreen.getOptions().getSoundCheck() && mFlag) {
            sound.resume();
        }
    }

    @Override
    protected void defineArmor() {
        final BodyDef bodyDef = new BodyDef();
        if (direction) {
            bodyDef.position.set(ConstGame.X / 2 + 0.8f, ConstEnemies.SPAWN_HEIGHT + 1.4f);
        } else {
            bodyDef.position.set(ConstGame.X / 2 - 0.8f, ConstEnemies.SPAWN_HEIGHT + 1.4f);
        }
        if (!mFlag) {
            bodyDef.type = BodyDef.BodyType.StaticBody;
        } else {
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape chain = new PolygonShape();
        chain.setAsBox(1.7f, 1.4f);
        fixtureDef.shape = chain;
        fixtureDef.density = ConstArmor.BOMB_EXPLOSION_DENSITY;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_ENEMY_BIT;
        if (!mFlag) {
            fixtureDef.filter.maskBits = ConstGame.GROUND_BIT;
        } else {
            fixtureDef.filter.maskBits = ConstGame.GROUND_BIT | ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        }
        body.createFixture(fixtureDef).setUserData(this);
    }
}
