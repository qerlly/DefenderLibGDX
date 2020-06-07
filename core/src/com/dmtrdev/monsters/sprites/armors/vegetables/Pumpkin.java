package com.dmtrdev.monsters.sprites.armors.vegetables;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;

public class Pumpkin extends Armor {

    public Pumpkin(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setRegion(DefenderOfNature.getArmorsAtlas().findRegion("pumpkin"));
        setSize(ConstArmor.PUMPKIN_SIZE - 0.1f, ConstArmor.PUMPKIN_SIZE);
        setOriginCenter();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("def_puff"), j * 357, i * 357, 357, 357));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
            }
        }
        frames.clear();
        body.setLinearVelocity(0, 5);
    }

    @Override
    public void collisionEnemy() {
        collisions++;
        if(collisions >= 3){
            setToDestroy = true;
            setSize(ConstArmor.EFFECT_LOW_SIZE - 0.2f, ConstArmor.EFFECT_LOW_SIZE);
        } else {
            if (playScreen.getOptions().getSoundCheck()) {
                DefenderOfNature.getPumpkinSound().play(playScreen.getOptions().getSoundVolume());
            }
        }
    }

    @Override
    public void collisionGround() {
        collisions++;
        if(collisions >= 3){
            setToDestroy = true;
            setSize(ConstArmor.EFFECT_LOW_SIZE - 0.2f, ConstArmor.EFFECT_LOW_SIZE);
        } else {
            if (playScreen.getOptions().getSoundCheck()) {
                DefenderOfNature.getPumpkinSound().play(playScreen.getOptions().getSoundVolume());
            }
        }
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.PUMPKIN_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getTomatoSound().play(playScreen.getOptions().getSoundVolume());
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
        final CircleShape shape = new CircleShape();
        shape.setRadius(ConstArmor.PUMPKIN_BODY_SIZE);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT | ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.restitution = ConstArmor.PUMPKIN_RESTITUTION;
        fixtureDef.density = ConstArmor.PUMPKIN_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}