package com.dmtrdev.monsters.utils;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.Disposable;
import com.dmtrdev.monsters.DefenderOfNature;

public class MusicManager implements Disposable {

    private Music mMusic;
    private final Options mOptions;

    public MusicManager(final Options pOptions) {
        mOptions = pOptions;
        mMusic = getGameMenuMusic();
        play();
    }

    public void setVolume(final float pVolume){
        mMusic.setVolume(pVolume);
    }

    public void changeMusic(final boolean pFlag) {
        stop();
        if (pFlag) {
            mMusic = getGameMusic();
        } else {
            mMusic = getGameMenuMusic();
        }
        play();
    }

    public void stop() {
        mMusic.stop();
    }

    public void pause(){
        mMusic.pause();
    }

    public void play() {
        if (mOptions.getMusicCheck()) {
            mMusic.setLooping(true);
            mMusic.setVolume(mOptions.getMusicVolume());
            mMusic.play();
        }
    }

    public static Music getGameMenuMusic() {
        return DefenderOfNature.manager.get("music/game_menu_music.mp3", Music.class);
    }

    public static Music getGameMusic() {
        return DefenderOfNature.manager.get("music/game_music.mp3", Music.class);
    }

    @Override
    public void dispose() {
        mMusic.dispose();
    }
}