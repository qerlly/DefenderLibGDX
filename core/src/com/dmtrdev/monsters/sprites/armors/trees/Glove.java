package com.dmtrdev.monsters.sprites.armors.trees;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;

public class Glove extends Armor {

    private boolean mFlag;

    public Glove(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setRegion(DefenderOfNature.getArmorsAtlas().findRegion("punching_glove"), 0, 0, 375, 111);
        setSize(4.2f, 2);
        setOriginCenter();

        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 4; i++) {
                frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("punching_glove"), i * 375, j * 111, 375, 111));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
            }
        }
        frames.clear();
        if (direction) {
            flip(true, false);
            setPosition(body.getPosition().x - 0.9f, body.getPosition().y - getHeight() / 2);
        } else {
            setPosition(body.getPosition().x - getWidth() + 0.9f, body.getPosition().y - getHeight() / 2);
        }
        mFlag = false;
    }

    @Override
    public void collisionEnemy() {
        setToDestroy = true;
    }

    @Override
    public void collisionGround() {
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.GLOVE_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy) {
            if (!destroy) {
                world.destroyBody(body);
                defineGlove();
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getPunchSound().play(playScreen.getOptions().getSoundVolume());
                }
                if (direction) {
                    setPosition(body.getPosition().x - 1.2f, body.getPosition().y + 1);
                } else {
                    setPosition(body.getPosition().x - getWidth() + 1.2f, body.getPosition().y + 1);
                }
                time = 0;
                destroy = true;
            } else if (destroyAnimation.isAnimationFinished(time)) {
                destroyed = true;
            }
            textureRegion = destroyAnimation.getKeyFrame(time);
            if (direction && !textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
            setRegion(textureRegion);
            time += delta;
            if(time >= 0.02f && !mFlag){
                world.destroyBody(body);
                mFlag = true;
            }
        }
    }

    @Override
    protected void defineArmor() {
        final BodyDef bodyDef = new BodyDef();
        if (direction) {
            bodyDef.position.set(ConstGame.X / 2 + 1.6f, ConstEnemies.SPAWN_HEIGHT + 2);
        } else {
            bodyDef.position.set(ConstGame.X / 2 - 1.6f, ConstEnemies.SPAWN_HEIGHT + 2);
        }
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.bullet = true;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(ConstArmor.BOMB_BODY_SIZE + 0.6f, ConstArmor.BOMB_BODY_SIZE);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.density = ConstArmor.SPEAR_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }

    protected void defineGlove() {
        final BodyDef bodyDef = new BodyDef();
        if (direction) {
            bodyDef.position.set(ConstGame.X / 2 + 2, ConstGame.SPAWN_HEIGHT - 1);
        } else {
            bodyDef.position.set(ConstGame.X / 2 - 2, ConstGame.SPAWN_HEIGHT - 1);
        }
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.bullet = true;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final CircleShape shape = new CircleShape();
        shape.setRadius(ConstArmor.SPEAR_BODY_WIDTH + 2.5f);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.DISABLE_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT | ConstGame.ENEMY_DOWN_BIT;
        fixtureDef.density = ConstArmor.SPEAR_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}
