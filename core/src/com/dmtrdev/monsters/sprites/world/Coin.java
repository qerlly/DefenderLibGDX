package com.dmtrdev.monsters.sprites.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.dmtrdev.monsters.DefenderOfNature;
import com.dmtrdev.monsters.consts.ConstGame;
import com.dmtrdev.monsters.screens.PlayScreen;

public class Coin extends Sprite {

    private boolean destroyed;
    private Body body;
    private final Vector3 vector3;
    private float timer;
    private final float cost, radius;
    private Animation<TextureRegion> coinAnimation;
    private final PlayScreen playScreen;

    public Coin(final PlayScreen pPlayScreen, final int pScore, final float pX, final float pY) {
        setPosition(pX, pY);
        playScreen = pPlayScreen;
        if (pScore < 5) {
            setSize(1.6f, 2);
            cost = 0.1f;
            radius = 0.9f;
        } else if (pScore < 10) {
            setSize(1.8f, 2.2f);
            cost = 0.4f;
            radius = 1;
        } else {
            cost = 1;
            radius = 1.1f;
            setSize(2, 2.4f);
        }
        defineItem();
        destroyed = false;
        vector3 = new Vector3();
        final Array<TextureRegion> frames = new Array<>();
        timer =0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 2 && j == 2) {
                    break;
                }
                if (pScore < 5) {
                    frames.add(new TextureRegion(getItemsAtlas().findRegion("copper_coin"), j * 429, i * 429, 429, 429));
                } else if (pScore < 10) {
                    frames.add(new TextureRegion(getItemsAtlas().findRegion("silver_coin"), j * 429, i * 429, 429, 429));
                } else {
                    frames.add(new TextureRegion(getItemsAtlas().findRegion("gold_coin"), j * 429, i * 429, 429, 429));

                }
                coinAnimation = new Animation<>(0.05f, frames, Animation.PlayMode.LOOP);
            }
        }
        frames.clear();

        body.applyLinearImpulse(0, 12, 0, 0, true);
    }

    private static TextureAtlas getItemsAtlas() {
        return DefenderOfNature.manager.get("atlases/world/items_atlas.txt", TextureAtlas.class);
    }

    private void defineItem() {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(getX(), getY());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = playScreen.getWorld().createBody(bodyDef);

        final FixtureDef fixtureDef = new FixtureDef();
        final CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = ConstGame.COIN_BIT;
        fixtureDef.restitution = 0.6f;
        fixtureDef.filter.maskBits = ConstGame.GROUND_BIT;
        body.createFixture(fixtureDef).setUserData(this);
    }

    public void draw(final Batch pBatch) {
        if (!destroyed) {
            super.draw(pBatch);
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    private void handleInput() {
        if (Gdx.input.isTouched()) {
            vector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            playScreen.getCamera().unproject(vector3);
            final Shape2D textureBounds = new Rectangle(getX(), getY(), getWidth(), getHeight());
            if (textureBounds.contains(vector3.x, vector3.y)) {
                if (playScreen.getOptions().getSoundCheck()) {
                    DefenderOfNature.getCoinSound().play(playScreen.getOptions().getSoundVolume());
                }
                destroyed = true;
                playScreen.getOptions().setCoins(cost);
            }
        }
    }

    public void update(final float delta) {
        handleInput();
        if (timer > 6.5f) {
            destroyed = true;
            playScreen.getWorld().destroyBody(body);
        }else if (destroyed) {
            playScreen.getWorld().destroyBody(body);
        }
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
        setRegion(coinAnimation.getKeyFrame(timer));
        timer += delta;
    }
}