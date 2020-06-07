package com.dmtrdev.monsters.sprites.armors.flyings;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.spawns.SpawnDef;
import com.dmtrdev.monsters.spawns.SpawningSystem;
import com.dmtrdev.monsters.sprites.armors.Armor;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Bird  extends Armor implements SpawningSystem {

    private Sound sound;
    private Animation<TextureRegion> mAttackAnimation;
    private final Array<BirdArmor> mSpawns;
    private final BlockingDeque<SpawnDef> mEnemiesToSpawn;
    private int mSplats;
    private final PlayScreen mPlayScreen;

    public Bird(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setSize(ConstArmor.STORK_WIDTH - 0.8f, ConstArmor.STORK_HEIGHT + 1.1f);
        mSpawns = new Array<BirdArmor>();
        mPlayScreen = pPlayScreen;
        mEnemiesToSpawn = new LinkedBlockingDeque<SpawnDef>();
        mSplats = 0;
        setOriginCenter();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("bird_armor_fly"), j * 250, i * 240, 250, 240));
                mAttackAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("bird_armor_die"), j * 250, i * 240, 250, 240));
                destroyAnimation = new Animation<TextureRegion>(ConstArmor.EFFECT_SPEED, frames);
            }
        }
        frames.clear();
        sound = DefenderOfNature.getFlySound();
        if (playScreen.getOptions().getSoundCheck()) {
            sound.loop();
            sound.play(playScreen.getOptions().getSoundVolume());
        }
    }

    public void draw(final Batch pBatch) {
        if (!destroyed) {
            super.draw(pBatch);
        }
        for (final BirdArmor birdArmor : mSpawns) {
            birdArmor.draw(pBatch);
        }
        if(ConstGame.GAME_STATE == ConstGame.State.PAUSE){
            sound.pause();
        } else if(playScreen.getOptions().getSoundCheck()) {
            sound.resume();
        }
    }

    @Override
    public void collisionEnemy() {
        setToDestroy = true;
    }

    @Override
    public void collisionGround() {
        direction = !direction;
    }

    @Override
    public int getArmorDamage() {
        return ConstArmor.BIRD_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        handleSpawning();
        if (setToDestroy && effects) {
            if (!destroy) {
                sound.stop();
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getBirdSound().play(playScreen.getOptions().getSoundVolume());
                }
                setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
                world.destroyBody(body);
                time = 0;
                destroy = true;
                setSize(ConstArmor.EFFECT_MEDIUM_SIZE, ConstArmor.EFFECT_MEDIUM_SIZE + 1);
            } else if (destroyAnimation.isAnimationFinished(time)) {
                setAlpha(0);
                if(mSpawns.size == 0) {
                    destroyed = true;
                }
            }
            textureRegion = destroyAnimation.getKeyFrame(time);
            if ((body.getLinearVelocity().x < 0) && textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            } else if ((body.getLinearVelocity().x > 0) && !textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
            setRegion(textureRegion);
            time += delta;
        } else if (setToDestroy) {
            world.destroyBody(body);
            destroyed = true;
        } else {
            textureRegion = mAttackAnimation.getKeyFrame(time);
            if ((body.getLinearVelocity().x < 0) && textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            } else if ((body.getLinearVelocity().x > 0) && !textureRegion.isFlipX()) {
                textureRegion.flip(true, false);
            }
            if (!direction) {
                body.setLinearVelocity(-ConstEnemies.FLYING_VELOCITY_X - 2, ConstEnemies.VELOCITY_Y);
            } else {
                body.setLinearVelocity(ConstEnemies.FLYING_VELOCITY_X + 2, ConstEnemies.VELOCITY_Y);
            }
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion(textureRegion);
            time += delta;
            if(time >= 0.4f){
                spawn(new SpawnDef(new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2), BirdArmor.class, direction));
                mSplats++;
                if (mSplats >= 20) {
                    setToDestroy = true;
                }
                time = 0;
            }
        }
        for (int i = 0; i < mSpawns.size; i++) {
            mSpawns.get(i).update(delta);
            if (mSpawns.get(i).getDestroyed()) {
                mSpawns.removeIndex(i);
                i--;
            }
        }
    }

    @Override
    public void handleSpawning() {
        if (!mEnemiesToSpawn.isEmpty()) {
            final SpawnDef spawnDef = mEnemiesToSpawn.poll();
            if (spawnDef.type == BirdArmor.class) {
                mSpawns.add(new BirdArmor(mPlayScreen, spawnDef.position.x, spawnDef.position.y, spawnDef.direction));
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
        bodyDef.position.set(getX(), getY() + 1);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape legs = new PolygonShape();
        legs.setAsBox(ConstEnemies.FLYING_BODY_SIZE + 1, ConstEnemies.FLYING_BODY_SIZE);
        fixtureDef.filter.categoryBits = ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT | ConstGame.SKY_BIT | ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.shape = legs;
        fixtureDef.density = ConstEnemies.FLYING_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}
