package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.samsung.gamestudio.components.MovingBackground;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.TextButton;

public class ScreenMenu implements Screen {

    MyGdxGame myGdxGame;

    MovingBackground background;
    TextButton buttonStart;
    TextButton buttonExit;

    // --- Музыка ---
    // MUSIC: чтобы добавить музыку главного меню — положи файл в assets/music/
    // и раскомментируй строки ниже
    // Music menuMusic;

    public ScreenMenu(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        buttonStart = new TextButton(100, 400, "Start");
        buttonExit  = new TextButton(700, 400, "Exit");
        background  = new MovingBackground("backgrounds/restart_bg.png");

        // MUSIC: раскомментируй чтобы загрузить музыку меню
        // menuMusic = Gdx.audio.newMusic(Gdx.files.internal("music/menu_track.mp3"));
        // menuMusic.setLooping(true);
        // menuMusic.setVolume(0.5f);
    }

    @Override
    public void show() {
        // MUSIC: if (menuMusic != null) menuMusic.play();
    }

    @Override
    public void render(float delta) {

        // Android: кнопка Back на главном меню = выход из игры
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            Gdx.app.exit();
            return;
        }

        if (Gdx.input.justTouched()) {

            Vector3 touch = myGdxGame.camera.unproject(
                    new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)
            );

            if (buttonStart.isHit((int) touch.x, (int) touch.y)) {
                // MUSIC: if (menuMusic != null) menuMusic.stop();
                myGdxGame.setScreen(myGdxGame.screenGame);
            }
            if (buttonExit.isHit((int) touch.x, (int) touch.y)) {
                Gdx.app.exit();
            }
        }

        background.move(delta);

        ScreenUtils.clear(1, 0, 0, 1);
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        myGdxGame.batch.begin();

        background.draw(myGdxGame.batch);
        buttonStart.draw(myGdxGame.batch);
        buttonExit.draw(myGdxGame.batch);

        myGdxGame.batch.end();
    }

    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }

    @Override
    public void hide() {
        // MUSIC: if (menuMusic != null) menuMusic.stop();
    }

    @Override
    public void dispose() {
        background.dispose();
        buttonExit.dispose();
        buttonStart.dispose();
        // MUSIC: if (menuMusic != null) menuMusic.dispose();
    }
}
