package com.dmtrdev.monsters.sprites.armors.medieval;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;

public class StoneAxe extends Armor {

    private Animation<TextureRegion> mAttackAnimation;

    public StoneAxe(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setSize(ConstArmor.AXE_SIZE - 0.4f, ConstArmor.AXE_SIZE);
        setOriginCenter();
        for (int i = 0; i < 8; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("armor_axe1"), i * 177, 0, 177, 177));
            mAttackAnimation = new Animation<TextureRegion>(ConstArmor.AXE_SPEED, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 7; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("star_explode"), j * 225, i * 213, 225, 213));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
            }
        }
        frames.clear();

        if(pDirection) {
            body.setLinearVelocity(2, 7);
        } else {
            body.setLinearVelocity(-2, 7);
        }
    }

    @Override
    public void collisionEnemy() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_MEDIUM_SIZE - 1, ConstArmor.EFFECT_MEDIUM_SIZE - 0.7f);
    }

    @Override
    public void collisionGround() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_MEDIUM_SIZE  - 1, ConstArmor.EFFECT_MEDIUM_SIZE - 0.7f);
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.STONE_AXE_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getStoneSound().play(playScreen.getOptions().getSoundVolume());
                }
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
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
            if ((body.getLinearVelocity().x < 0) && !textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            } else if ((body.getLinearVelocity().x > 0) && textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion(textureRegion);
            time += delta;
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
        shape.setRadius(ConstArmor.AXE_BODY_SIZE);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT | ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.density = ConstArmor.AXE_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}