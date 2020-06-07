package com.dmtrdev.monsters.sprites.armors.bombs;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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

public class SpikeBomb extends Armor implements SpawningSystem {

    private final Array<com.dmtrdev.monsters.sprites.armors.bombs.Spike> mSpikes;
    private final BlockingDeque<SpawnDef> mEnemiesToSpawn;
    private final PlayScreen mPlayScreen;

    public SpikeBomb(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        mSpikes = new Array<Spike>();
        mPlayScreen = pPlayScreen;
        mEnemiesToSpawn = new LinkedBlockingDeque<SpawnDef>();
        textureRegion = new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("spikey_ball_blue"));
        setSize(ConstArmor.SPIKE_BOMB_SIZE - 0.3f, ConstArmor.SPIKE_BOMB_SIZE + 0.4f);
        setOriginCenter();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 13; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("big_smoke_explode"), j * 124, i * 124, 124, 124));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED - 0.01f, frames);
            }
        }
        frames.clear();
        setRegion(textureRegion);
    }

    @Override
    public void collisionEnemy() {
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_STAR_SIZE_BIG - 1, ConstArmor.EFFECT_STAR_SIZE_BIG);
    }

    @Override
    public void collisionGround() {
        for (int i = 0; i < 5; i++) {
            spawn(new SpawnDef(new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2), Spike.class, direction));
        }
        setToDestroy = true;
        setSize(ConstArmor.EFFECT_STAR_SIZE_BIG - 1, ConstArmor.EFFECT_STAR_SIZE_BIG);
    }

    public void draw(final Batch pBatch) {
        if (!destroyed) {
            super.draw(pBatch);
        }
        for (final Spike spike : mSpikes) {
            spike.draw(pBatch);
        }
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.SPIKE_BOMB_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        handleSpawning();
        if (setToDestroy && effects) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getBombSound().play(playScreen.getOptions().getSoundVolume());
                }
                world.destroyBody(body);
                defineExplosion();
                destroy = true;
                time = 0;
            } else if (destroyAnimation.isAnimationFinished(time) && mSpikes.size == 0) {
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
                defineExplosion();
                destroy = true;
            } else if (mSpikes.size == 0) {
                world.destroyBody(body);
                destroyed = true;
            }
        } else {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            time += delta;
        }
        for (int i = 0; i < mSpikes.size; i++) {
            mSpikes.get(i).update(delta);
            if (mSpikes.get(i).getDestroyed()) {
                mSpikes.removeIndex(i);
                i--;
            }
        }
    }

    @Override
    public void handleSpawning() {
        if (!mEnemiesToSpawn.isEmpty()) {
            final SpawnDef spikeDef = mEnemiesToSpawn.poll();
            if (spikeDef.type == Spike.class) {
                mSpikes.add(new Spike(mPlayScreen, spikeDef.position.x, spikeDef.position.y, spikeDef.direction));
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
        final CircleShape shape = new CircleShape();
        shape.setRadius(ConstArmor.SPIKE_BOMB_BODY_SIZE);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT;
        fixtureDef.density = ConstArmor.SPIKE_BOMB_DENSITY;
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