package ru.samsung.gamestudio;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.samsung.gamestudio.screens.ScreenGame;
import ru.samsung.gamestudio.screens.ScreenMenu;
import ru.samsung.gamestudio.screens.ScreenRestart;
import ru.samsung.gamestudio.screens.ScreenStory;

public class MyGdxGame extends Game {

    public SpriteBatch batch;
    public OrthographicCamera camera;

    public static final int SCR_WIDTH = 1280;
    public static final int SCR_HEIGHT = 720;

    public ScreenGame screenGame;
    public ScreenMenu screenMenu;
    public ScreenRestart screenRestart;
    public ScreenStory screenStory;

    // Хранилище лучшего счёта (сохраняется между сессиями)
    private Preferences prefs;
    private static final String PREFS_NAME = "escape_from_turmsik";
    private static final String KEY_BEST_SCORE = "best_score";

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCR_WIDTH, SCR_HEIGHT);

        prefs = com.badlogic.gdx.Gdx.app.getPreferences(PREFS_NAME);

        screenGame    = new ScreenGame(this);
        screenMenu    = new ScreenMenu(this);
        screenRestart = new ScreenRestart(this);
        screenStory   = new ScreenStory(this);

        // Запуск со страницы истории
        setScreen(screenStory);
    }

    public int getBestScore() {
        return prefs.getInteger(KEY_BEST_SCORE, 0);
    }

    public void saveBestScore(int score) {
        if (score > getBestScore()) {
            prefs.putInteger(KEY_BEST_SCORE, score);
            prefs.flush();
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
