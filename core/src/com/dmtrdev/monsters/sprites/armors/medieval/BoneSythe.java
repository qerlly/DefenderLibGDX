package com.dmtrdev.monsters.sprites.armors.medieval;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;

public class BoneSythe extends Armor {

    public BoneSythe(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setRegion(DefenderOfNature.getArmorsAtlas().findRegion("bone_sythe"));
        setSize(ConstArmor.SPIKE_CLUB_WIDTH - 0.1f, ConstArmor.SPIKE_CLUB_HEIGHT - 0.1f);
        setOriginCenter();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 7; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("star_explode"), j * 225, i * 213, 225, 213));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
            }
        }
        frames.clear();
        if (direction) {
            body.setAngularVelocity(-2f);
            flip(true, false);
        } else {
            body.setAngularVelocity(2f);
        }
    }

    @Override
    public void collisionEnemy() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_MEDIUM_SIZE - 0.5f, ConstArmor.EFFECT_MEDIUM_SIZE - 0.5f);
    }

    @Override
    public void collisionGround() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_MEDIUM_SIZE - 0.5f, ConstArmor.EFFECT_MEDIUM_SIZE - 0.5f);
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.BONE_SYTHE_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getStoneSound().play(playScreen.getOptions().getSoundVolume());
                }
                setPosition(body.getPosition().x, body.getPosition().y - getHeight() / 2);
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
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        }
    }

    @Override
    protected void defineArmor() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final CircleShape shape = new CircleShape();
        shape.setRadius(ConstArmor.ARROW_BODY_WIDTH);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT | ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.density = ConstArmor.ARROW_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}