package com.dmtrdev.monsters.sprites.armors.bombs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;

public class Bomb extends Armor {

    private Animation<TextureRegion> mIdleAnimation;

    public Bomb(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setSize(ConstArmor.BOMB_SIZE - 0.2f, ConstArmor.BOMB_SIZE + 0.3f);
        setOriginCenter();
        for (int i = 3; i < 6; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("armor_bomb"), i * 84, 0, 84, 92));
            mIdleAnimation = new Animation<TextureRegion>(ConstArmor.BOMB_SPEED, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("def_explode"), j * 179, i * 179, 179, 179));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED + 0.01f, frames);
            }
        }
        frames.clear();
        body.setLinearVelocity(0, 1);
    }

    @Override
    public void collisionEnemy() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_BIG_SIZE - 0.8f, ConstArmor.EFFECT_BIG_SIZE);
    }

    @Override
    public void collisionGround() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_BIG_SIZE - 0.8f, ConstArmor.EFFECT_BIG_SIZE);
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.BOMB_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getBombSound().play(playScreen.getOptions().getSoundVolume());
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
        shape.setRadius(ConstArmor.BOMB_BODY_SIZE);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT;
        fixtureDef.density = ConstArmor.BOMB_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }

    private void defineExplosion() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(body.getPosition().x, ConstGame.SPAWN_HEIGHT - ConstArmor.BOMB_EXPLOSION_SIZE / 2);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final CircleShape shape = new CircleShape();
        shape.setRadius(ConstArmor.BOMB_EXPLOSION_SIZE);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.density = ConstArmor.BOMB_EXPLOSION_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}