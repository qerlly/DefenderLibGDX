package com.dmtrdev.monsters.sprites.armors.trees;

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

public class Spikes extends Armor {

    private Animation<TextureRegion> mAttackAnimation;

    public Spikes(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setSize(3.8f, 8.4f);
        setOriginCenter();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("spikes_armor"), j * 246, i * 428, 246, 428));
                mAttackAnimation = new Animation<TextureRegion>(ConstArmor.KNIFE_SPEED, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();
        for (int j = 0; j < 22; j++) {
            frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("down_explode"), 0, j * 70, 354, 70));
            destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
        }
        frames.clear();
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 1);
    }

    @Override
    public void collisionEnemy() {
    }

    @Override
    public void collisionEnemy(final int pDamage, final boolean pCollision) {
    }

    @Override
    public void collisionGround() {
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.SPIKES_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getBarrelSound().play(playScreen.getOptions().getSoundVolume());
                }
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 1.2f);
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
            if(time >= 6.5f) {
                setToDestroy = true;
                setSize(ConstArmor.EFFECT_MEDIUM_SIZE + 2.3f, ConstArmor.EFFECT_MEDIUM_SIZE - 1.1f);
                time = 0;
            }
            setRegion(textureRegion = mAttackAnimation.getKeyFrame(time));
            time += delta;
        }
    }

    @Override
    protected void defineArmor() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(ConstGame.X / 2, ConstGame.SPAWN_HEIGHT);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape chain = new PolygonShape();
        chain.setAsBox(1.2f, 5f);
        fixtureDef.shape = chain;
        fixtureDef.density = ConstArmor.BOMB_EXPLOSION_DENSITY;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        body.createFixture(fixtureDef).setUserData(this);
    }
}
