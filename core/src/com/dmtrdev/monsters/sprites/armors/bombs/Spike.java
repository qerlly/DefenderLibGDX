package com.dmtrdev.monsters.sprites.armors.bombs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;

import java.util.Random;

public class Spike extends Armor {

    public Spike(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setRegion(DefenderOfNature.getArmorsAtlas().findRegion("spike"));

        setSize(ConstArmor.SPIKE_SIZE, ConstArmor.SPIKE_SIZE - 0.1f);
        setOriginCenter();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 13; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("big_smoke_explode"), j * 124, i * 124, 124, 124));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED - 0.01f, frames);
            }
        }
        frames.clear();
        final Random random = new Random();
        body.setLinearVelocity(random.nextInt(10) - 5, random.nextInt(9) + 7);
    }

    @Override
    public void collisionEnemy() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_LOW_SIZE, ConstArmor.EFFECT_LOW_SIZE);
    }

    @Override
    public void collisionGround() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_LOW_SIZE, ConstArmor.EFFECT_LOW_SIZE);
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.SPIKE_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getBombikSound().play(playScreen.getOptions().getSoundVolume());
                }
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
                world.destroyBody(body);
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
            body.setTransform(body.getPosition().x, body.getPosition().y, (float) Math.atan2(body.getLinearVelocity().y, body.getLinearVelocity().x));
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRotation(body.getAngle() * MathUtils.radiansToDegrees);
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
        shape.setAsBox(ConstArmor.SPIKE_BODY_HEIGHT, ConstArmor.SPIKE_BODY_WIDTH);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT | ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.density = ConstArmor.SPIKE_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}