package com.dmtrdev.monsters.sprites.enemies.yetis;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.bombs.Molotov;
import com.dmtrdev.monsters.sprites.armors.medieval.Arrow;
import com.dmtrdev.monsters.sprites.enemies.Enemy;

public class YetiHigh extends Enemy {

    private Animation<TextureRegion> jumpAnimation;
    private boolean jump;

    public YetiHigh(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        super(pScreen, pX, pY, pDirection);
        if(pScreen.getType().contains(ConstGame.SURVIVE_MODE)){
            if(pScreen.getGameGui().getGenerates().getWaveIndex() % 5 == 0) {
                hp = (ConstEnemies.YETI_HIGH_HP / 2) + ((pScreen.getGameGui().getGenerates().getWaveIndex() / 5) * 20);
            } else if(pScreen.getGameGui().getGenerates().getWaveIndex() > 4 ){
                hp = (ConstEnemies.YETI_HIGH_HP / 2) + ((int)Math.floor(Math.abs(pScreen.getGameGui().getGenerates().getWaveIndex()/5)) * 25);
            } else {
                hp = ConstEnemies.YETI_HIGH_HP / 2;
            }
        } else {
            hp = ConstEnemies.YETI_HIGH_HP;
        }

        for(int j = 0; j < 4; j++) {
            for (int i = 0; i < 2; i++) {
                frames.add(new TextureRegion(DefenderOfNature.getYetisAtlas().findRegion("yeti_high_run"), i * 330, i * 330, 330, 330));
                runAnimation = new Animation<>(ConstEnemies.YETI_ANIM, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for(int j = 0; j < 2; j++) {
            for (int i = 0; i < 5; i++) {
                frames.add(new TextureRegion(DefenderOfNature.getYetisAtlas().findRegion("yeti_high_attack"), i * 330, j * 330, 330, 330));
                attackAnimation = new Animation<>(ConstEnemies.YETI_ANIM, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getYetisAtlas().findRegion("yeti_high_jump"), i * 330, 0, 330, 330));
            jumpAnimation = new Animation<>(ConstEnemies.YETI_ANIM, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("skeleton_explode"), j * 233, i * 213, 233, 213));
                deathAnimation = new Animation<>(ConstArmor.EFFECT_SPEED, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();
        jump = false;
        setSize(ConstEnemies.YETI_SIZE - 1, ConstEnemies.YETI_SIZE);
    }

    @Override
    public void damaged(final int pDamage, final String pData) {
        hp -= pDamage;
        if (hp <= 0) {
            setToDestroy = true;
        }
    }

    @Override
    public void update(final float delta) {
        super.update(delta);
        if(burn) {
            setToDestroy = true;
        }
        if (direction && !setToDestroy) {
            setPosition(body.getPosition().x - getWidth() / 2 - 0.3f, body.getPosition().y - 0.6f);
        } else if (!setToDestroy) {
            setPosition(body.getPosition().x - getWidth() / 2 + 0.3f, body.getPosition().y - 0.6f);
        }
        switch (getState()) {
            case DIE:
                if (effects) {
                    if (!destroy) {
                        if (screen.getOptions().getSoundCheck()) {
                            DefenderOfNature.getDeathBigSound().play(screen.getOptions().getSoundVolume());
                        }
                        if (direction) {
                            setPosition(body.getPosition().x - getWidth() / 2 + 0.5f, body.getPosition().y);
                        } else {
                            setPosition(body.getPosition().x - getWidth() / 2 + 0.5f, body.getPosition().y);
                        }
                        world.destroyBody(body);
                        setSize(ConstArmor.EFFECT_MEDIUM_SIZE, ConstArmor.EFFECT_MEDIUM_SIZE + 0.6f);
                        destroy = true;
                        time = 0;
                        coinSpawn = true;
                    } else if (deathAnimation.isAnimationFinished(time)) {
                        destroyed = true;
                    }
                    setRegion(getFrame());
                    time += delta;
                } else {
                    world.destroyBody(body);
                    destroyed = true;
                    coinSpawn = true;
                }
                break;
            case DISABLE:
                body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                if (disableTime <= 0) {
                    disable = false;
                }
                disableTime -= delta;
                break;
            case ATTACK:
                if (attackAnimation.isAnimationFinished(attackTime) && plantAttack) {
                    collisionPlant(takenDamage);
                    attackTime = 0;
                } else if (attackAnimation.isAnimationFinished(attackTime) && attack) {
                    tree.setHp(ConstEnemies.YETI_HIGH_DAMAGE);
                    attackTime = 0;
                }
                body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                setRegion(getFrame());
                attackTime += delta;
                break;
            default:
                if ((body.getPosition().x > 3.5f && body.getPosition().x < 7.5f && body.getPosition().y < ConstEnemies.SPAWN_HEIGHT + 0.3f) ||
                        (body.getPosition().x < 25 && body.getPosition().x > 21 && body.getPosition().y < ConstEnemies.SPAWN_HEIGHT + 0.3f)) {
                    jump = true;
                }
                if (!jump) {
                    if (direction) {
                        body.setLinearVelocity(ConstEnemies.YETI_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    } else {
                        body.setLinearVelocity(-ConstEnemies.YETI_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                    }
                } else {
                    if (body.getPosition().y > 17) {
                        jump = false;
                    }
                    if (direction) {
                        body.setLinearVelocity(4, 16);
                    } else {
                        body.setLinearVelocity(-4, 16);
                    }
                }
                if (body.getPosition().y > ConstEnemies.SPAWN_HEIGHT + 0.3f) {
                    textureRegion = jumpAnimation.getKeyFrame(time);
                    if (!direction && !textureRegion.isFlipX()) {
                        textureRegion.flip(true, false);
                    } else if (direction && textureRegion.isFlipX()) {
                        textureRegion.flip(true, false);
                    }
                    setRegion(textureRegion);
                } else {
                    setRegion(getFrame());
                    time += delta;
                }
                break;
        }
    }

    @Override
    public int getEnemyDamage() {
        return ConstEnemies.YETI_HIGH_DAMAGE;
    }

    @Override
    public int getScore() {
        return ConstEnemies.YETI_HIGH_SCORE;
    }

    @Override
    protected void defineEnemy() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final CircleShape legs = new CircleShape();
        legs.setRadius(ConstEnemies.LEGS_SIZE);
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_DOWN_BIT;
        fixtureDef.filter.maskBits = ConstGame.DISABLE_BIT | ConstGame.ARMOR_ENEMY_BIT | ConstGame.GROUND_BIT | ConstGame.FIRE_BIT;
        fixtureDef.shape = legs;
        fixtureDef.density = ConstEnemies.YETI_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape corpus = new CircleShape();
        corpus.setRadius(getSize(ConstEnemies.YETI_BODY_SIZE, ConstEnemies.YETI_BODY_DIFF_SIZE));
        corpus.setPosition(new Vector2(0, corpus.getRadius() + ConstEnemies.LEGS_SIZE));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.PLAYER_BIT | ConstGame.TREE_LOW_BIT | ConstGame.GROUND_BIT | ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.shape = corpus;
        fixtureDef.density = ConstEnemies.YETI_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape head = new CircleShape();
        head.setRadius(getSize(ConstEnemies.YETI_HEAD_SIZE, ConstEnemies.YETI_HEAD_DIFF_SIZE));
        head.setPosition(new Vector2(0, ConstEnemies.LEGS_SIZE + head.getRadius() + corpus.getRadius() * 2));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.PLAYER_BIT;
        fixtureDef.shape = head;
        fixtureDef.density = ConstEnemies.YETI_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
        body.setFixedRotation(true);
    }
}
