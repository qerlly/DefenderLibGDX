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

public class IronSpear extends Armor {

    private final boolean mDirection;

    public IronSpear(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setRegion(DefenderOfNature.getArmorsAtlas().findRegion("spear_type_two"));
        setSize(ConstArmor.SPEAR_WIDTH, ConstArmor.SPEAR_HEIGHT);
        setOriginCenter();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 7; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("star_explode"), j * 225, i * 213, 225, 213));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
            }
        }

        mDirection = pDirection;
        if (pDirection) {
            body.setLinearVelocity(18, -25);
        } else {
            body.setLinearVelocity(-18, -25);
        }
    }

    @Override
    public void collisionEnemy() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_MEDIUM_SIZE  - 1, ConstArmor.EFFECT_MEDIUM_SIZE - 0.7f);
    }

    @Override
    public void collisionGround() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_MEDIUM_SIZE  - 1, ConstArmor.EFFECT_MEDIUM_SIZE - 0.7f);
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.IRON_SPEAR_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getIronSound().play(playScreen.getOptions().getSoundVolume());
                }
                if(!mDirection) {
                    setPosition(getX() - getWidth(), body.getPosition().y - getHeight() + 0.7f);
                } else {
                    setPosition(getX() + getWidth() / 2, body.getPosition().y - getHeight());
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
        shape.setAsBox(ConstArmor.SPEAR_BODY_WIDTH, ConstArmor.SPEAR_BODY_HEIGHT);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT | ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.density = ConstArmor.SPEAR_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}