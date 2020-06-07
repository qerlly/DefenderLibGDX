package com.dmtrdev.monsters.sprites.armors.ground;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;

public class Stone extends Armor {

    private final Vector3 vector3;
    private boolean mFlag;
    private final PlayScreen mPlayScreen;

    public Stone(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        vector3 = new Vector3();
        mFlag = false;
        mPlayScreen = pPlayScreen;
        setSize(2.8f, 2.8f);
        textureRegion = DefenderOfNature.getArmorsAtlas().findRegion("rock_hanging");
        setOriginCenter();
        for (int j = 0; j < 22; j++) {
            frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("down_explode"), 0, j * 70, 354, 70));
            destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
        }
        frames.clear();
        if (direction) {
            textureRegion.flip(true, false);
        }
        setRegion(textureRegion);
    }

    @Override
    public void collisionEnemy() {
    }

    @Override
    public void collisionGround() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_MEDIUM_SIZE + 2.3f, ConstArmor.EFFECT_MEDIUM_SIZE - 1.1f);
        destroy = false;
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.STONE_TRAP_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getBigSound().play(playScreen.getOptions().getSoundVolume());
                }
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 1.25f);
                world.destroyBody(body);
                defineStone();
                time = 0;
                destroy = true;
            } else if (destroyAnimation.isAnimationFinished(time)) {
                world.destroyBody(body);
                destroyed = true;
            }
            setRegion(textureRegion = destroyAnimation.getKeyFrame(time));
            time += delta;
        } else if (setToDestroy) {
            world.destroyBody(body);
            destroyed = true;
        } else if (mFlag) {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            if (!destroy) {
                world.destroyBody(body);
                defineArmor();
                destroy = true;
            }
        } else {
            handleInput();
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        }
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            vector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            mPlayScreen.getCamera().unproject(vector3);
            final Shape2D textureBounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
            if (textureBounds.contains(vector3.x, vector3.y)) {
                mFlag = true;
            }
        }
    }

    @Override
    protected void defineArmor() {
        final BodyDef bodyDef = new BodyDef();
        if (!mFlag) {
            if (!direction) {
                bodyDef.position.set(getX() - 2.1f, getY() + 2.5f);
            } else {
                bodyDef.position.set(getX() + 2.1f, getY() + 2.5f);
            }
            bodyDef.type = BodyDef.BodyType.StaticBody;
            body = world.createBody(bodyDef);
        } else {
            bodyDef.position.set(getX() + 1.4f, getY() + 0.5f);
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            body = world.createBody(bodyDef);
        }

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(ConstArmor.PLANT_BODY_WIDTH - 0.4f, 1.3f);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT;
        fixtureDef.density = ConstArmor.PLANT_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }

    private void defineStone() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX() + 2.5f, ConstEnemies.SPAWN_HEIGHT + 0.2f);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(1.8f, 0.2f);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_BIT;
        fixtureDef.density = ConstArmor.PLANT_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}
