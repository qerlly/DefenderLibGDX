package com.dmtrdev.monsters.sprites.armors.flyings;

import com.badlogic.gdx.graphics.g2d.Animation;
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

public class FlyingBomb extends Armor {

    private Animation<TextureRegion> mAttackAnimation;

    public FlyingBomb(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setSize(ConstArmor.BOMB_SIZE + 2, ConstArmor.BOMB_SIZE + 2.2f);
        setOriginCenter();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("fl_bomb_run"), j * 339, i * 311, 339, 311));
                mAttackAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED + 0.01f, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("def_explode"), j * 179, i * 179, 179, 179));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED + 0.01f, frames);
            }
        }
        frames.clear();
    }

    @Override
    public void collisionEnemy() {
        collisions++;
        setToDestroy = true;
    }

    @Override
    public void collisionGround() {
        collisions++;
        direction = !direction;
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.FLYING_BOMB_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getGrenadeSound().play(playScreen.getOptions().getSoundVolume());
                }
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 1.5f);
                world.destroyBody(body);
                time = 0;
                destroy = true;
                setSize(ConstArmor.EFFECT_MEDIUM_SIZE - 0.2f, ConstArmor.EFFECT_MEDIUM_SIZE + 0.4f);
            } else if (destroyAnimation.isAnimationFinished(time)) {
                destroyed = true;
            }
            textureRegion = destroyAnimation.getKeyFrame(time);
            if ((body.getLinearVelocity().x < 0) && textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            } else if ((body.getLinearVelocity().x > 0) && !textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
            setRegion(textureRegion);
            time += delta;
        } else if (setToDestroy) {
            world.destroyBody(body);
            destroyed = true;
        } else {
            textureRegion = mAttackAnimation.getKeyFrame(time);
            if ((body.getLinearVelocity().x < 0) && textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            } else if ((body.getLinearVelocity().x > 0) && !textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
            if (!direction) {
                body.setLinearVelocity(-ConstEnemies.FLYING_VELOCITY_X, ConstEnemies.VELOCITY_Y);
            } else {
                body.setLinearVelocity(ConstEnemies.FLYING_VELOCITY_X, ConstEnemies.VELOCITY_Y);
            }
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion(textureRegion);
            time += delta;
            if (collisions >= 2) {
                setToDestroy = true;
            }
        }
    }

    @Override
    protected void defineArmor() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), 15);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape legs = new PolygonShape();
        legs.setAsBox(ConstEnemies.FLYING_BODY_SIZE, ConstEnemies.FLYING_BODY_SIZE);
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT | ConstGame.SKY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.shape = legs;
        fixtureDef.density = ConstEnemies.FLYING_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}