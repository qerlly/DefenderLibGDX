package com.dmtrdev.monsters.sprites.armors.bombs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;

public class PrehistorikBomb extends Armor {

    private Animation<TextureRegion> mIdleAnimation;

    public PrehistorikBomb(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setSize(ConstArmor.SMALL_BOMB_SIZE + 0.15f, ConstArmor.SMALL_BOMB_SIZE + 2);
        setOriginCenter();
        for (int i = 0; i < 4; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("bombik_high"), i * 135, 0, 135, 313));
            mIdleAnimation = new Animation<TextureRegion>(ConstArmor.BOMB_SPEED - 0.02f, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 5; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("smoke_explode"), j * 255, i * 255, 255, 255));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED - 0.005f, frames);
            }
        }
        frames.clear();
    }

    @Override
    public void collisionEnemy() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_MEDIUM_SIZE, ConstArmor.EFFECT_MEDIUM_SIZE + 0.4f);
    }

    @Override
    public void collisionGround() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_MEDIUM_SIZE, ConstArmor.EFFECT_MEDIUM_SIZE + 0.4f);
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.PREHISTORIK_BOMB_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getBombikSound().play(playScreen.getOptions().getSoundVolume());
                }
                world.destroyBody(body);
                defineExplosion();
                time = 0;
                destroy = true;
            } else if (destroyAnimation.isAnimationFinished(time)) {
                world.destroyBody(body);
                destroyed = true;
            }
            setPosition(body.getPosition().x - getWidth() / 2, ConstGame.FRAME_WORLD_MARGIN);
            setRegion(textureRegion = destroyAnimation.getKeyFrame(time));
            time += delta;
        } else if (setToDestroy) {
            setSize(0, 0);
            if (!destroy) {
                world.destroyBody(body);
                destroy = true;
                time = 0;
                defineExplosion();
            } else if (time >= 1) {
                world.destroyBody(body);
                destroyed = true;
            }
            time += delta;
        } else {
            textureRegion = mIdleAnimation.getKeyFrame(time);
            if (direction && !textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
            setRegion(textureRegion);
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
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
        shape.setRadius(ConstArmor.BOMB_BODY_SIZE / 2);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT;
        fixtureDef.density = ConstArmor.BOMB_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
        body.setFixedRotation(true);
    }

    private void defineExplosion() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(body.getPosition().x, ConstGame.SPAWN_HEIGHT - 0.7f);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final CircleShape shape = new CircleShape();
        shape.setRadius(ConstArmor.BOMB_EXPLOSION_SIZE - 2);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.density = ConstArmor.BOMB_EXPLOSION_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}
