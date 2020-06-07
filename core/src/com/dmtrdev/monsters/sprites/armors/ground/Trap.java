package com.dmtrdev.monsters.sprites.armors.ground;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.screens.PlayScreen;
import com.dmtrdev.monsters.sprites.armors.Armor;
import com.dmtrdev.monsters.consts.ConstArmor;
import com.dmtrdev.monsters.consts.ConstGame;

public class Trap extends Armor {

    public Trap(final PlayScreen pPlayScreen, final float pX, final float pY, final boolean pDirection) {
        super(pPlayScreen, pX, pY, pDirection);
        setRegion(DefenderOfNature.getArmorsAtlas().findRegion("trap_armor"), 0, 0, 428, 280);
        setSize(ConstArmor.TRAP_SIZE_WIDTH - 0.4f, ConstArmor.TRAP_SIZE_HEIGHT);
        setOriginCenter();
        for (int i = 0; i < 5; i++) {
            frames.add(new TextureRegion(DefenderOfNature.getArmorsAtlas().findRegion("trap_armor"), 0, i * 280, 428, 280));
            destroyAnimation = new Animation<TextureRegion>(ConstArmor.TRAP_SPEED, frames);
        }
        frames.clear();
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
        return ConstArmor.TRAP_DAMAGE;
    }

    @Override
    public void update(final float delta) {
        if (setToDestroy) {
            if (!destroy) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getTrapSound().play(playScreen.getOptions().getSoundVolume());
                }
                world.destroyBody(body);
                destroy = true;
            } else if (destroyAnimation.isAnimationFinished(time)) {
                destroyed = true;
            }
            setRegion(textureRegion = destroyAnimation.getKeyFrame(time));
            time += delta;
        } else {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - 0.3f);
        }
    }

    @Override
    protected void defineArmor() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final PolygonShape shape = new PolygonShape();
        shape.setAsBox(ConstArmor.TRAP_BODY_SIZE + 0.1f, ConstArmor.TRAP_BODY_SIZE);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.DISABLE_BIT;
        fixtureDef.filter.maskBits = ConstGame.ENEMY_DOWN_BIT | ConstGame.GROUND_BIT;
        fixtureDef.density = ConstArmor.TRAP_DENSITY;
        body.createFixture(fixtureDef).setUserData(this);
    }
}
