package com.dmtrdev.monsters.sprites.armors.vegetables;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.spawns.SpawnDef;
import com.dmtrdev.monsters.spawns.SpawningSystem;
import com.dmtrdev.monsters.sprites.armors.Armor;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class SweetCorn extends Armor implements SpawningSystem {

    private final PlayScreen mPlayScreen;
    private final Array<Corn> mCorns;
    private final BlockingDeque<SpawnDef> mEnemiesToSpawn;

    public SweetCorn(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        mCorns = new Array<Corn>();
        mPlayScreen = pPlayScreen;
        mEnemiesToSpawn = new LinkedBlockingDeque<SpawnDef>();
        setRegion(DefenderOfNature.getArmorsAtlas().findRegion("sweetcorn"));
        setSize(ConstArmor.SWEETCORN_WIDTH, ConstArmor.SWEETCORN_HEIGHT);
        setOriginCenter();

        for (int j = 0; j < 22; j++) {
            frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("down_explode"), 0, j * 70, 354, 70));
            destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
        }
        frames.clear();
        body.setLinearVelocity(0, 6);
    }

    @Override
    public void collisionEnemy() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_MEDIUM_SIZE + 1.7f, ConstArmor.EFFECT_MEDIUM_SIZE - 1.3f);
    }

    @Override
    public void collisionGround() {
        for (int i = 0; i < 5; i++) {
            spawn(new SpawnDef(new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2), Corn.class, direction));
        }
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_MEDIUM_SIZE + 1.7f, ConstArmor.EFFECT_MEDIUM_SIZE - 1.3f);
    }

    public void draw(final Batch pBatch) {
        if (!destroyed) {
            super.draw(pBatch);
        }
        for (final Corn corn : mCorns) {
            corn.draw(pBatch);
        }
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.SWEETCORN_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        handleSpawning();
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getTomatoSound().play(playScreen.getOptions().getSoundVolume());
                }
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight());
                world.destroyBody(body);
                destroy = true;
            } else if (destroyAnimation.isAnimationFinished(time)) {
                if (mCorns.size == 0) {
                    destroyed = true;
                }
            } else {
                setRegion(textureRegion = destroyAnimation.getKeyFrame(time));
                time += delta;
            }
        } else if (setToDestroy) {
            if (!destroy) {
                world.destroyBody(body);
                destroy = true;
            } else if (mCorns.size == 0) {
                destroyed = true;
            }
        } else {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        }
        for (int i = 0; i < mCorns.size; i++) {
            mCorns.get(i).update(delta);
            if (mCorns.get(i).getDestroyed()) {
                mCorns.removeIndex(i);
                i--;
            }
        }
    }

    @Override
    public void handleSpawning() {
        if (!mEnemiesToSpawn.isEmpty()) {
            final SpawnDef cornDef = mEnemiesToSpawn.poll();
            if (cornDef.type == Corn.class) {
                mCorns.add(new Corn(mPlayScreen, cornDef.position.x, cornDef.position.y, cornDef.direction));
            }
        }
    }

    @Override
    public void spawn(final SpawnDef pSpawnDef) {
        mEnemiesToSpawn.add(pSpawnDef);
    }

    @Override
    protected void defineArmor() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(ConstArmor.SWEETCORN_BODY_WIDTH, ConstArmor.SWEETCORN_BODY_HEIGHT);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT | ConstGame.ENEMY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.density = ConstArmor.SWEETCORN_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}