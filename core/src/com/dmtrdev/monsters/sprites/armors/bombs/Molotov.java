package com.dmtrdev.monsters.sprites.armors.bombs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;

public class Molotov extends Armor {

    private Animation<TextureRegion> mIdleAnimation;

    public Molotov(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setSize(ConstArmor.MOLOTOV_SIZE_WIDTH - 0.2f, ConstArmor.MOLOTOV_SIZE_HEIGHT);
        setOriginCenter();
        for (int i = 0; i < 6; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("armor_molotov"), i * 127, 0, 127, 174));
            mIdleAnimation = new Animation<TextureRegion>(ConstArmor.MOLOTOV_SPEED - 0.02f, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("fire_armor"), 0, i * 334, 1332, 334));
            destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();
        body.setLinearVelocity(0, 5);
    }

    @Override
    public void collisionEnemy() {

    }

    @Override
    public void collisionGround() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_BIG_SIZE * 2, ConstArmor.EFFECT_MEDIUM_SIZE);
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.FIRE_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getMolotovSound().play(playScreen.getOptions().getSoundVolume());
                }
                final float x = body.getPosition().x - 2;
                world.destroyBody(body);
                defineExplosion(x);
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 0.4f);
                time = 0;
                destroy = true;
            } else if (destroyAnimation.isAnimationFinished(time) && time > 5) {
                world.destroyBody(body);
                destroyed = true;
            }
            setRegion(textureRegion = destroyAnimation.getKeyFrame(time));
            time += delta;
        } else {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 4);
            setRegion(textureRegion = mIdleAnimation.getKeyFrame(time));
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
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(ConstArmor.MOLOTOV_BODY_WIDTH, ConstArmor.MOLOTOV_BODY_HEIGHT);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT;
        fixtureDef.density = ConstArmor.MOLOTOV_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }

    private void defineExplosion(final float pX) {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(pX + ConstArmor.MOLOTOV_EXPLOSION_WIDTH / 2, ConstEnemies.SPAWN_HEIGHT);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(ConstArmor.MOLOTOV_EXPLOSION_WIDTH, ConstArmor.MOLOTOV_EXPLOSION_HEIGHT);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.FIRE_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_DOWN_BIT;
        fixtureDef.density = ConstArmor.BOMB_EXPLOSION_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}

