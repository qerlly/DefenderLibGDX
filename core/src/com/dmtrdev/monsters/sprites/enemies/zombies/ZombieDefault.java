package com.dmtrdev.monsters.sprites.enemies.zombies;

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
import com.dmtrdev.monsters.sprites.armors.ground.CarnivorePlant;
import com.dmtrdev.monsters.sprites.armors.ground.Trap;
import com.dmtrdev.monsters.sprites.armors.ninja.ModernShuriken;
import com.dmtrdev.monsters.sprites.armors.ninja.PrimitiveShuriken;
import com.dmtrdev.monsters.sprites.armors.ninja.StandardShuriken;
import com.dmtrdev.monsters.sprites.enemies.Enemy;

public class ZombieDefault extends Enemy {

    public ZombieDefault(final PlayScreen pScreen, final float pX, final float pY, final boolean pDirection) {
        super(pScreen, pX, pY, pDirection);
        if(pScreen.getType().contains(ConstGame.SURVIVE_MODE)){
            if(pScreen.getGameGui().getGenerates().getWaveIndex() % 5 == 0) {
                hp = (ConstEnemies.ZOMBIE_DEFAULT_HP / 2) + ((pScreen.getGameGui().getGenerates().getWaveIndex() / 5) * 10);
            } else if(pScreen.getGameGui().getGenerates().getWaveIndex() > 4 ){
                hp = (ConstEnemies.ZOMBIE_DEFAULT_HP / 2) + ((int)Math.floor(Math.abs(pScreen.getGameGui().getGenerates().getWaveIndex()/5)) * 10);
            } else {
                hp = ConstEnemies.ZOMBIE_DEFAULT_HP / 2;
            }
        } else {
            hp = ConstEnemies.ZOMBIE_DEFAULT_HP;
        }

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getZombieAtlas().findRegion("zombie_default_run"), i * 312, 0, 312, 370));
            runAnimation = new Animation<>(ConstEnemies.ZOMBIE_ANIM, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();

        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getZombieAtlas().findRegion("zombie_default_attack"), i * 313, 0, 313, 331));
            attackAnimation = new Animation<>(ConstEnemies.ZOMBIE_ANIM, frames, Animation.PlayMode.LOOP);
        }
        frames.clear();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 3; j++) {
                frames.add(new TextureRegion(DefenderOfNature.getEffectsAtlas().findRegion("skeleton_explode"), j * 233, i * 213, 233, 213));
                deathAnimation = new Animation<>(ConstArmor.EFFECT_SPEED, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();
        setSize(ConstEnemies.ZOMBIE_SIZE_WIDTH, ConstEnemies.ZOMBIE_SIZE_HEIGHT);
    }

    @Override
    public void damaged(final int pDamage, final String pData) {
        if (!pData.equals(PrimitiveShuriken.class.getSimpleName()) && !pData.equals(ModernShuriken.class.getSimpleName()) &&
                !pData.equals(StandardShuriken.class.getSimpleName())) {
            hp -= pDamage;
        }
        if (hp <= 0 || pData.equals(CarnivorePlant.class.getSimpleName()) || pData.equals(Trap.class.getSimpleName())) {
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
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - ConstEnemies.ZOMBIE_HEAD_DIFF_SIZE);
        } else if (!setToDestroy) {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - ConstEnemies.ZOMBIE_HEAD_DIFF_SIZE);
        }
        switch (getState()) {
            case DIE:
                if (effects) {
                    if (!destroy) {
                        if (screen.getOptions().getSoundCheck()) {
                            DefenderOfNature.getDeathSound().play(screen.getOptions().getSoundVolume());
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
                setSize(ConstEnemies.ZOMBIE_SIZE_WIDTH - 0.3f, ConstEnemies.ZOMBIE_SIZE_HEIGHT + 0.1f);
                if (attackAnimation.isAnimationFinished(attackTime) && plantAttack) {
                    collisionPlant(takenDamage);
                    attackTime = 0;
                } else if (attackAnimation.isAnimationFinished(attackTime) && attack) {
                    tree.setHp(ConstEnemies.ZOMBIE_DEFAULT_DAMAGE);
                    attackTime = 0;
                }
                body.setLinearVelocity(0, ConstEnemies.VELOCITY_Y);
                setRegion(getFrame());
                attackTime += delta;
                break;
            default:
                setSize(ConstEnemies.ZOMBIE_SIZE_WIDTH - 0.5f, ConstEnemies.ZOMBIE_SIZE_HEIGHT);
                if (direction) {
                    body.setLinearVelocity(ConstEnemies.ZOMBIE_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                } else {
                    body.setLinearVelocity(-ConstEnemies.ZOMBIE_VELOCITY_X, ConstEnemies.VELOCITY_Y);
                }
                setRegion(getFrame());
                time += delta;
                break;
        }
    }

    @Override
    public int getEnemyDamage() {
        return ConstEnemies.ZOMBIE_DEFAULT_DAMAGE;
    }

    @Override
    public int getScore() {
        return ConstEnemies.ZOMBIE_DEFAULT_SCORE;
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
        fixtureDef.density = ConstEnemies.ZOMBIE_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape corpus = new CircleShape();
        corpus.setRadius(getSize(ConstEnemies.ZOMBIE_BODY_SIZE, ConstEnemies.ZOMBIE_BODY_DIFF_SIZE));
        corpus.setPosition(new Vector2(0, corpus.getRadius() + ConstEnemies.LEGS_SIZE));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT | ConstGame.TREE_LOW_BIT | ConstGame.ARMOR_ENEMY_BIT;
        fixtureDef.shape = corpus;
        fixtureDef.density = ConstEnemies.ZOMBIE_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);

        final CircleShape head = new CircleShape();
        head.setRadius(getSize(ConstEnemies.ZOMBIE_HEAD_SIZE, ConstEnemies.ZOMBIE_HEAD_DIFF_SIZE));
        head.setPosition(new Vector2(0, ConstEnemies.LEGS_SIZE + head.getRadius() + corpus.getRadius() * 2));
        fixtureDef.filter.categoryBits = ConstGame.ENEMY_BIT;
        fixtureDef.filter.maskBits = ConstGame.ARMOR_BIT;
        fixtureDef.shape = head;
        fixtureDef.density = ConstEnemies.ZOMBIE_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
        body.setFixedRotation(true);
    }
}
