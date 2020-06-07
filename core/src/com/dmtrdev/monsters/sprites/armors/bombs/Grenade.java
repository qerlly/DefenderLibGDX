package com.dmtrdev.monsters.sprites.armors.bombs;

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

public class Grenade extends Armor {

    public Grenade(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        textureRegion = new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("grenade"));
        setSize(ConstArmor.GRENADE_SIZE_WIDTH, ConstArmor.GRENADE_SIZE_HEIGHT);
        setOriginCenter();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 2; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("def_explode"), j * 179, i * 179, 179, 179));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED + 0.01f, frames);
            }
        }
        frames.clear();
        setRegion(textureRegion);
        body.setLinearVelocity(0, 4);
    }

    @Override
    public void collisionEnemy() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_BIG_SIZE - 0.6f, ConstArmor.EFFECT_BIG_SIZE);
    }

    @Override
    public void collisionGround() {
        collisions++;
        if (collisions >= 2) {
            setToDestroy = true;
            setSize(ConstArmor.EFFECT_BIG_SIZE - 0.6f, ConstArmor.EFFECT_BIG_SIZE);
        }
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.GRENADE_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getGrenadeSound().play(playScreen.getOptions().getSoundVolume());
                }
                world.destroyBody(body);
                defineExplosion();
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
            time += delta;
            if (!destroy) {
                world.destroyBody(body);
                destroy = true;
                defineExplosion();
            } else if (time >= 1) {
                world.destroyBody(body);
                destroyed = true;
            }
        } else {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
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
        shape.setRadius(ConstArmor.GRENADE_BODY_SIZE);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT;
        fixtureDef.restitution = ConstArmor.GRENADE_RESTITUTION;
        fixtureDef.density = ConstArmor.GRENADE_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }

    private void defineExplosion() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(body.getPosition().x, ConstGame.SPAWN_HEIGHT - ConstArmor.GRENADE_EXPLOSION_SIZE / 2);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final CircleShape shape = new CircleShape();
        shape.setRadius(ConstArmor.GRENADE_EXPLOSION_SIZE);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.density = ConstArmor.BOMB_EXPLOSION_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}