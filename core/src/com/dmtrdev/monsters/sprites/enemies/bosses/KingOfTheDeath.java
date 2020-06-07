package com.dmtrdev.monsters.sprites.enemies.bosses;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstEnemies;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.medieval.Arrow;
import com.dmtrdev.monsters.sprites.enemies.Enemy;

public class KingOfTheDeath extends Enemy {

    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> riseAnimation;
    private boolean flag, rise, jump;
    private byte count;

    public KingOfTheDeath(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        super(pScreen, pX, pY, pDirection);
        if(pScreen.getType().contains(ConstGame.SURVIVE_MODE)){
            if(pScreen.getGameGui().getGenerates().getWaveIndex() % 5 == 0) {
                hp = (ConstEnemies.KING_HP / 2) + ((pScreen.getGameGui().getGenerates().getWaveIndex() / 5) * 80);
            } else if(pScreen.getGameGui().getGenerates().getWaveIndex() > 4 ){
                hp = (ConstEnemies.KING_HP / 2) + ((int)Math.floor(Math.abs(pScreen.getGameGui().getGenerates().getWaveIndex()/5)) * 80);
            } else {
                hp = ConstEnemies.KING_HP / 2;
            }
        } else {
            hp = ConstEnemies.KING_HP;
        }

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 6; i++) {
                frames.add(new TextureRegion(getKingAtlas().findRegion("king_run"), i * 250, j * 200, 250, 200));
                runAnimation = new Animation<>(ConstEnemies.KING_ANIM - 0.01f, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 6; i++) {
                frames.add(new TextureRegion(getKingAtlas().findRegion("king_rise"), i * 250, j * 200, 250, 200));
                riseAnimation = new Animation<>(ConstEnemies.KING_ANIM, frames);
            }
        }
        frames.clear();

        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 6; i++) {
                frames.add(new TextureRegion(getKingAtlas().findRegion("king_attack"), i * 251, j * 200, 251, 200));
                attackAnimation = new Animation<>(ConstEnemies.KING_ANIM, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 6; i++) {
                frames.add(new TextureRegion(getKingAtlas().findRegion("king_jump"), i * 250, j * 200, 250, 200));
                jumpAnimation = new Animation<>(ConstEnemies.KING_ANIM, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 6; i++) {
                frames.add(new TextureRegion(getKingAtlas().findRegion("king_die"), i * 250, j * 200, 250, 200));
                deathAnimation = new Animation<>(ConstEnemies.KING_ANIM, frames);
            }
        }
        frames.clear();
        setSize(ConstEnemies.KING_SIZE_WIDTH - 0.8f, ConstEnemies.KING_SIZE_HEIGHT + 0.5f);
        flag = rise = jump = false;
        count = 0;
    }

    private static TextureAtlas getKingAtlas() {
        return DefenderOfNature.manager.get("atlases/enemies/king_of_death_atlas.txt", TextureAtlas.class);
    }

    @Override
    public void damaged(final int pDamage, final String pData) {
        hp -= pDamage;
        if (hp <= 0) {
            setToDestroy = true;
        }
    }

    @Override
    public void setDirection() {
        if (!jump) {
            direction = !direction;
            jump = true;
        }
    }

    @Override
    public void update(final float delta) {
        super.update(delta);
        if (direction && !setToDestroy) {
            setPosition(body.getPosition().x - getWidth() / 2 + 1, body.getPosition().y - 0.5f);
        } else if (!setToDestroy) {
            setPosition(body.getPosition().x - getWidth() / 2 - 1, body.getPosition().y - 0.5f);
        }
        switch (getState()) {
            case DIE:
                if (!destroy) {
                    if (screen.getOptions().getSoundCheck()) {
                        DefenderOfNature.getDeathBigSound().play(screen.getOptions().getSoundVolume());
                    }
                    world.destroyBody(body);
                    destroy = true;
                    if (!rise) {
                        time = 0;
                    }
                    attackTime = 0;
                    coinSpawn = true;
                } else if (deathAnimation.isAnimationFinished(time) && time >= 4.5f) {
                    if (flag) {
                        destroyed = true;
                    } else {
                        rise = true;
                    }
                }
                if (riseAnimation.isAnimationFinished(attackTime)) {
                    setToDestroy = false;
                    destroy = false;
                    if(screen.getType().contains(ConstGame.SURVIVE_MODE)){
                        if(screen.getGameGui().getGenerates().getWaveIndex() % 5 == 0) {
                            hp = (ConstEnemies.KING_HP / 2) + ((screen.getGameGui().getGenerates().getWaveIndex() / 5) * 80);
                        } else if(screen.getGameGui().getGenerates().getWaveIndex() > 4 ){
                            hp = (ConstEnemies.KING_HP / 2) + ((int)Math.floor(Math.abs(screen.getGameGui().getGenerates().getWaveIndex()/5)) * 80);
                        } else {
                            hp = ConstEnemies.KING_HP / 2;
                        }
                    } else {
                        hp = ConstEnemies.KING_HP;
                    }
                    defineEnemy();
                    flag = true;
                }
                if (!rise) {
                    setRegion(getFrame());
                    time += delta;
                } else {
                    textureRegion = riseAnimation.getKeyFrame(attackTime);
                    if (!direction && !textureRegion.isFlipX()) {
                        textureRegion.flip(true, false);
                    } else if (direction && textureRegion.isFlipX()) {
                        textureRegion.flip(true, false);
                    }
                    setRegion(textureRegion);
                    attackTime += delta;
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
                    tree.setHp(ConstEnemies.KING_DAMAGE);
                    attackTime = 0;
                    if (count >= MathUtils.random(3) + 1) {
                        attack = false;
                        direction = !direction;
                        count = 0;
                    }
                    count++;
                }
                if (direction) {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                } else {
                    body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                }
                setRegion(getFrame());
                attackTime += delta;
                break;
            default:
                if (!jump) {
                    if (direction) {
                        body.setLinearVelocity(ConstEnemies.KING_VELOCITY_X, ConstEnemies.VELOCITY_Y * 2);
                    } else {
                        body.setLinearVelocity(-ConstEnemies.KING_VELOCITY_X, ConstEnemies.VELOCITY_Y * 2);
                    }
                } else {
                    if (body.getPosition().x > 2 && body.getPosition().x < 27) {
                        jump = false;
                    }
                    if (direction) {
                        body.setLinearVelocity(12, 20);
                    } else {
                        body.setLinearVelocity(-12, 20);
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
        return ConstEnemies.KING_DAMAGE;
    }

    @Override
    public int getScore() {
        return ConstEnemies.KING_SCORE;
    }

    @Override
    protected void defineEnemy() {
        final BodyDef bodyDef = new BodyDef();
        if (direction) {
            bodyDef.position.set(getX() + 2.5f, getY() + 1);
        } else {
            bodyDef.position.set(getX() + 5, getY() + 1);
        }
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final CircleShape legs = new CircleShape();
        legs.setRadius(ConstEnemies.LEGS_SIZE);
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_DOWN_BIT;
        fixtureDef.filter.maskBits = ConstGame.DISABLE_BIT | ConstGame.ARMOR_ENEMY_BIT | ConstGame.GROUND_BIT | ConstGame.FIRE_BIT;
        fixtureDef.shape = legs;
        fixtureDef.density = ConstEnemies.KING_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape corpus = new CircleShape();
        corpus.setRadius(ConstEnemies.KING_BODY_SIZE);
        corpus.setPosition(new Vector2(0, corpus.getRadius() + ConstEnemies.LEGS_SIZE));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.PLAYER_BIT | ConstGame.TREE_LOW_BIT | ConstGame.GROUND_BIT| ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.shape = corpus;
        fixtureDef.density = ConstEnemies.KING_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape head = new CircleShape();
        head.setRadius(ConstEnemies.KING_HEAD_SIZE);
        head.setPosition(new Vector2(0, ConstEnemies.LEGS_SIZE + head.getRadius() + corpus.getRadius() * 2));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_SHOTS_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.PLAYER_BIT;
        fixtureDef.shape = head;
        fixtureDef.density = ConstEnemies.KING_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
        body.setFixedRotation(true);
    }
}