package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.samsung.gamestudio.components.MovingBackground;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.PointCounter;
import ru.samsung.gamestudio.components.TextButton;

public class ScreenRestart implements Screen {

    MyGdxGame myGdxGame;

    MovingBackground background;
    PointCounter pointCounter;
    PointCounter bestScoreCounter; // счётчик рекорда
    TextButton buttonRestart;
    TextButton buttonMenu;

    int gamePoints;

    // --- Музыка ---
    // MUSIC: чтобы добавить музыку на экран рестарта — положи файл в assets/music/
    // и раскомментируй строки ниже
    // Music menuMusic;

    public ScreenRestart(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        pointCounter     = new PointCounter(750, 530);
        bestScoreCounter = new PointCounter(750, 440); // чуть ниже основного счёта
        buttonRestart = new TextButton(100, 400, "Restart");
        buttonMenu    = new TextButton(100, 150, "Menu");
        background    = new MovingBackground("backgrounds/restart_bg.png");

        // MUSIC: раскомментируй чтобы загрузить музыку экрана рестарта
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

        // Android: кнопка Back → возврат в меню
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            myGdxGame.setScreen(myGdxGame.screenMenu);
            return;
        }

        if (Gdx.input.justTouched()) {

            Vector3 touch = myGdxGame.camera.unproject(
                    new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)
            );

            if (buttonRestart.isHit((int) touch.x, (int) touch.y)) {
                // MUSIC: if (menuMusic != null) menuMusic.stop();
                myGdxGame.setScreen(myGdxGame.screenGame);
            }
            if (buttonMenu.isHit((int) touch.x, (int) touch.y)) {
                // MUSIC: if (menuMusic != null) menuMusic.stop();
                myGdxGame.setScreen(myGdxGame.screenMenu);
            }
        }

        ScreenUtils.clear(1, 0, 0, 1);
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        myGdxGame.batch.begin();

        background.draw(myGdxGame.batch);
        buttonMenu.draw(myGdxGame.batch);
        buttonRestart.draw(myGdxGame.batch);

        // Текущий счёт
        pointCounter.draw(myGdxGame.batch, gamePoints);

        // Лучший счёт берём из MyGdxGame (сохранён в Preferences)
        bestScoreCounter.drawBest(myGdxGame.batch, myGdxGame.getBestScore());

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
        buttonRestart.dispose();
        buttonMenu.dispose();
        pointCounter.dispose();
        bestScoreCounter.dispose();
        // MUSIC: if (menuMusic != null) menuMusic.dispose();
    }
}
