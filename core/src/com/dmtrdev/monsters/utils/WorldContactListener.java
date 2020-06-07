package com.dmtrdev.monsters.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.sprites.Player;
import com.dmtrdev.monsters.sprites.armors.Armor;
import com.dmtrdev.monsters.sprites.enemies.Enemy;

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(final Contact contact) {
        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();
        final int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (cDef) {
            case ConstGame.ENEMY_BIT | ConstGame.ARMOR_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_BIT) {
                    ((Enemy) fixtureA.getUserData()).damaged(((Armor) fixtureB.getUserData()).getArmorDamage(), fixtureB.getUserData().getClass().getSimpleName());
                    ((Armor) fixtureB.getUserData()).collisionEnemy();
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_BIT) {
                    ((Armor) fixtureA.getUserData()).collisionEnemy();
                    ((Enemy) fixtureB.getUserData()).damaged(((Armor) fixtureA.getUserData()).getArmorDamage(), fixtureA.getUserData().getClass().getSimpleName());
                }
                break;
            case ConstGame.PLAYER_BIT | ConstGame.ENEMY_SHOTS_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.PLAYER_BIT) {
                    ((Player) fixtureA.getUserData()).collision(((Enemy) fixtureB.getUserData()).getDirection(), new Vector2(12, 8));
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.PLAYER_BIT) {
                    ((Player) fixtureB.getUserData()).collision(((Enemy) fixtureA.getUserData()).getDirection(), new Vector2(12, 8));
                }
                break;
            case ConstGame.GROUND_BIT | ConstGame.ARMOR_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.GROUND_BIT) {
                    ((Armor) fixtureB.getUserData()).collisionGround();
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.GROUND_BIT) {
                    ((Armor) fixtureA.getUserData()).collisionGround();
                }
                break;
            case ConstGame.GROUND_BIT | ConstGame.PLAYER_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.PLAYER_BIT) {
                    ((Player) fixtureA.getUserData()).setDestroy(true);
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.PLAYER_BIT) {
                    ((Player) fixtureB.getUserData()).setDestroy(true);
                }
                break;
            case ConstGame.TREE_LOW_BIT | ConstGame.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.TREE_LOW_BIT) {
                    ((Enemy) fixtureB.getUserData()).setAttack(true);
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.TREE_LOW_BIT) {
                    ((Enemy) fixtureA.getUserData()).setAttack(true);
                }
                break;
            case ConstGame.ENEMY_DOWN_BIT | ConstGame.FIRE_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_DOWN_BIT) {
                    ((Enemy) fixtureA.getUserData()).setBurn(true);
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_DOWN_BIT) {
                    ((Enemy) fixtureB.getUserData()).setBurn(true);
                }
                break;
            case ConstGame.ENEMY_DOWN_BIT | ConstGame.DISABLE_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_DOWN_BIT) {
                    ((Enemy) fixtureA.getUserData()).disabled(((Armor) fixtureB.getUserData()).getArmorDamage(), fixtureB.getUserData().getClass().getSimpleName(), true);
                    ((Armor) fixtureB.getUserData()).collisionEnemy();
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_DOWN_BIT) {
                    ((Enemy) fixtureB.getUserData()).disabled(((Armor) fixtureA.getUserData()).getArmorDamage(), fixtureA.getUserData().getClass().getSimpleName(), true);
                    ((Armor) fixtureA.getUserData()).collisionEnemy();
                }
                break;
            case ConstGame.ENEMY_DOWN_BIT | ConstGame.ARMOR_ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_DOWN_BIT) {
                    ((Enemy) fixtureA.getUserData()).collisionPlant(((Armor) fixtureB.getUserData()).getArmorDamage());
                    ((Armor) fixtureB.getUserData()).collisionEnemy(((Enemy) fixtureA.getUserData()).getEnemyDamage(), true);
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_DOWN_BIT) {
                    ((Enemy) fixtureB.getUserData()).collisionPlant(((Armor) fixtureA.getUserData()).getArmorDamage());
                    ((Armor) fixtureA.getUserData()).collisionEnemy(((Enemy) fixtureB.getUserData()).getEnemyDamage(), true);
                }
                break;
            case ConstGame.ENEMY_BIT | ConstGame.ARMOR_ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_BIT) {
                    ((Enemy) fixtureA.getUserData()).collisionPlant(((Armor) fixtureB.getUserData()).getArmorDamage());
                    ((Armor) fixtureB.getUserData()).collisionEnemy(((Enemy) fixtureA.getUserData()).getEnemyDamage(), true);
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_BIT) {
                    ((Enemy) fixtureB.getUserData()).collisionPlant(((Armor) fixtureA.getUserData()).getArmorDamage());
                    ((Armor) fixtureA.getUserData()).collisionEnemy(((Enemy) fixtureB.getUserData()).getEnemyDamage(), true);
                }
                break;
            case ConstGame.GROUND_BIT | ConstGame.ENEMY_SHOTS_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_SHOTS_BIT) {
                    ((Enemy) fixtureA.getUserData()).setDirection();
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_SHOTS_BIT) {
                    ((Enemy) fixtureB.getUserData()).setDirection();
                }
                break;
            case ConstGame.ENEMY_SHOTS_BIT | ConstGame.ARMOR_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_SHOTS_BIT) {
                    ((Enemy) fixtureA.getUserData()).damaged(((Armor) fixtureB.getUserData()).getArmorDamage(), fixtureB.getUserData().getClass().getSimpleName());
                    ((Armor) fixtureB.getUserData()).collisionEnemy();
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_SHOTS_BIT) {
                    ((Armor) fixtureA.getUserData()).collisionEnemy();
                    ((Enemy) fixtureB.getUserData()).damaged(((Armor) fixtureA.getUserData()).getArmorDamage(), fixtureA.getUserData().getClass().getSimpleName());
                }
                break;
            case ConstGame.ENEMY_SHOTS_BIT | ConstGame.TREE_LOW_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.TREE_LOW_BIT) {
                    ((Enemy) fixtureB.getUserData()).setAttack(true);
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.TREE_LOW_BIT) {
                    ((Enemy) fixtureA.getUserData()).setAttack(true);
                }
                break;
            case ConstGame.ENEMY_SHOTS_BIT | ConstGame.ARMOR_ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_SHOTS_BIT) {
                    ((Armor) fixtureB.getUserData()).collisionEnemy();
                    ((Enemy) fixtureA.getUserData()).collisionPlant(((Armor) fixtureB.getUserData()).getArmorDamage());
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_SHOTS_BIT) {
                    ((Armor) fixtureA.getUserData()).collisionEnemy();
                    ((Enemy) fixtureB.getUserData()).collisionPlant(((Armor) fixtureA.getUserData()).getArmorDamage());
                }
                break;
            case ConstGame.GROUND_BIT | ConstGame.ARMOR_ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.GROUND_BIT) {
                    ((Armor) fixtureB.getUserData()).collisionGround();
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.GROUND_BIT) {
                    ((Armor) fixtureA.getUserData()).collisionGround();
                }
                break;
            case ConstGame.GROUND_BIT | ConstGame.SKY_ARMOR_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.GROUND_BIT) {
                    ((Armor) fixtureB.getUserData()).collisionGround();
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.GROUND_BIT) {
                    ((Armor) fixtureA.getUserData()).collisionGround();
                }
                break;
            case ConstGame.ENEMY_SHOTS_BIT | ConstGame.SKY_ARMOR_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_SHOTS_BIT) {
                    ((Enemy) fixtureA.getUserData()).collisionPlant(((Armor) fixtureB.getUserData()).getArmorDamage());
                    ((Armor) fixtureB.getUserData()).collisionEnemy(((Enemy) fixtureA.getUserData()).getEnemyDamage(), true);
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_SHOTS_BIT) {
                    ((Enemy) fixtureB.getUserData()).collisionPlant(((Armor) fixtureA.getUserData()).getArmorDamage());
                    ((Armor) fixtureA.getUserData()).collisionEnemy(((Enemy) fixtureB.getUserData()).getEnemyDamage(), true);
                }
                break;
        }
    }

    @Override
    public void endContact(final Contact contact) {
        final Fixture fixtureA = contact.getFixtureA();
        final Fixture fixtureB = contact.getFixtureB();
        final int cDef = fixtureA.getFilterData().categoryBits | fixtureB.getFilterData().categoryBits;

        switch (cDef) {
            case ConstGame.TREE_LOW_BIT | ConstGame.ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_BIT) {
                    ((Enemy) fixtureA.getUserData()).setAttack(false);
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_BIT) {
                    ((Enemy) fixtureB.getUserData()).setAttack(false);
                }
                break;
            case ConstGame.TREE_LOW_BIT | ConstGame.ENEMY_SHOTS_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_SHOTS_BIT) {
                    ((Enemy) fixtureA.getUserData()).setAttack(false);
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_SHOTS_BIT) {
                    ((Enemy) fixtureB.getUserData()).setAttack(false);
                }
                break;
            case ConstGame.ENEMY_DOWN_BIT | ConstGame.FIRE_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_DOWN_BIT) {
                    ((Enemy) fixtureA.getUserData()).setBurn(false);
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_DOWN_BIT) {
                    ((Enemy) fixtureB.getUserData()).setBurn(false);
                }
                break;
            case ConstGame.ENEMY_DOWN_BIT | ConstGame.ARMOR_ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_DOWN_BIT) {
                    ((Enemy) fixtureA.getUserData()).setPlantAttack(false);
                    ((Armor) fixtureB.getUserData()).collisionEnemy(((Enemy) fixtureA.getUserData()).getEnemyDamage(), false);
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_DOWN_BIT) {
                    ((Enemy) fixtureB.getUserData()).setPlantAttack(false);
                    ((Armor) fixtureA.getUserData()).collisionEnemy(((Enemy) fixtureB.getUserData()).getEnemyDamage(), false);
                }
                break;
            case ConstGame.ENEMY_BIT | ConstGame.ARMOR_ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_BIT) {
                    ((Enemy) fixtureA.getUserData()).setPlantAttack(false);
                    ((Armor) fixtureB.getUserData()).collisionEnemy(((Enemy) fixtureA.getUserData()).getEnemyDamage(), false);
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_BIT) {
                    ((Enemy) fixtureB.getUserData()).setPlantAttack(false);
                    ((Armor) fixtureA.getUserData()).collisionEnemy(((Enemy) fixtureB.getUserData()).getEnemyDamage(), false);
                }
                break;
            case ConstGame.ENEMY_SHOTS_BIT | ConstGame.SKY_ARMOR_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_SHOTS_BIT) {
                    ((Enemy) fixtureA.getUserData()).setPlantAttack(false);
                    ((Armor) fixtureB.getUserData()).collisionEnemy(((Enemy) fixtureA.getUserData()).getEnemyDamage(), false);
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_SHOTS_BIT) {
                    ((Enemy) fixtureB.getUserData()).setPlantAttack(false);
                    ((Armor) fixtureA.getUserData()).collisionEnemy(((Enemy) fixtureB.getUserData()).getEnemyDamage(), false);
                }
                break;
            case ConstGame.ENEMY_SHOTS_BIT | ConstGame.ARMOR_ENEMY_BIT:
                if (fixtureA.getFilterData().categoryBits == ConstGame.ENEMY_SHOTS_BIT) {
                    ((Armor) fixtureB.getUserData()).collisionEnemy();
                    ((Enemy) fixtureA.getUserData()).setPlantAttack(false);
                } else if (fixtureB.getFilterData().categoryBits == ConstGame.ENEMY_SHOTS_BIT) {
                    ((Armor) fixtureA.getUserData()).collisionEnemy();
                    ((Enemy) fixtureB.getUserData()).setPlantAttack(false);
                }
                break;
        }
    }

    @Override
    public void preSolve(final Contact contact, final Manifold oldManifold) {
    }

    @Override
    public void postSolve(final Contact contact, final ContactImpulse impulse) {
    }
}