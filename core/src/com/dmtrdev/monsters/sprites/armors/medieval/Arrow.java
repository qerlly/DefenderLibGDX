package com.dmtrdev.monsters.sprites.armors.medieval;

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

public class Arrow extends Armor {

    private final boolean mDirection;

    public Arrow(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setRegion(DefenderOfNature.getArmorsAtlas().findRegion("red_arrow_projectile"));
        setSize(ConstArmor.ARROW_WIDTH, ConstArmor.ARROW_HEIGHT);
        setOriginCenter();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 7; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("star_explode"), j * 225, i * 213, 225, 213));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
            }
        }
        frames.clear();

        mDirection = pDirection;
        if (pDirection) {
            body.setLinearVelocity(18, -16);
        } else {
            body.setLinearVelocity(-18, -16);
        }
    }

    @Override
    public void collisionEnemy() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_LOW_SIZE - 0.3f, ConstArmor.EFFECT_LOW_SIZE);
    }

    @Override
    public void collisionGround() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_LOW_SIZE - 0.3f, ConstArmor.EFFECT_LOW_SIZE);
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.ARROW_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getShurikenSound().play(playScreen.getOptions().getSoundVolume());
                }
                if(!mDirection) {
                    setPosition(getX() - getWidth() / 2, getY() - getHeight() / 2);
                } else {
                    setPosition(getX(), getY() - getHeight() / 2);
                }
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
        bodyDef.bullet = true;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(ConstArmor.ARROW_BODY_WIDTH, ConstArmor.ARROW_BODY_HEIGHT);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT | ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.density = ConstArmor.ARROW_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}
