package ru.samsung.gamestudio.screens;

import static ru.samsung.gamestudio.MyGdxGame.SCR_HEIGHT;
import static ru.samsung.gamestudio.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.samsung.gamestudio.characters.Bird;
import ru.samsung.gamestudio.components.MovingBackground;
import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.PointCounter;
import ru.samsung.gamestudio.characters.Tube;

public class ScreenGame implements Screen {

    final int pointCounterMarginTop = 60;
    final int pointCounterMarginRight = 400;

    MyGdxGame myGdxGame;

    Bird bird;
    PointCounter pointCounter;
    MovingBackground background;

    int tubeCount = 3;
    Tube[] tubes;

    int gamePoints;
    boolean isGameOver;

    // --- Стартовая анимация ---
    boolean isWaitingToStart; // true = игра на паузе, ждём первого тапа
    float startAnimTimer = 0f;
    BitmapFont startFont;

    // --- Музыка ---
    // MUSIC: чтобы добавить музыку — положи файл в assets/music/
    // и раскомментируй строки ниже, заменив "music/your_track.mp3" на путь к файлу
    // Music gameMusic;

    public ScreenGame(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        background = new MovingBackground("backgrounds/game_bg.png");
        bird = new Bird(20, SCR_HEIGHT / 2, 10, 250, 200);
        pointCounter = new PointCounter(SCR_WIDTH - pointCounterMarginRight, SCR_HEIGHT - pointCounterMarginTop);

        startFont = new BitmapFont();
        startFont.getData().setScale(3.5f);
        startFont.setColor(Color.YELLOW);

        initTubes();

        // MUSIC: раскомментируй чтобы загрузить музыку
        // gameMusic = Gdx.audio.newMusic(Gdx.files.internal("music/your_track.mp3"));
        // gameMusic.setLooping(true);
        // gameMusic.setVolume(0.7f);
    }

    @Override
    public void show() {
        gamePoints = 0;
        isGameOver = false;
        isWaitingToStart = true;   // каждый раз при входе — ждём тапа
        startAnimTimer = 0f;
        bird.setY(SCR_HEIGHT / 2);
        disposeTubes();            // исправлено: сначала dispose старых труб
        initTubes();

        // MUSIC: раскомментируй чтобы запустить музыку при входе на экран
        // if (gameMusic != null) gameMusic.play();
    }

    @Override
    public void render(float delta) {

        // Android: кнопка Back → возврат в меню
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            // MUSIC: if (gameMusic != null) gameMusic.stop();
            myGdxGame.setScreen(myGdxGame.screenMenu);
            return;
        }

        startAnimTimer += delta;

        // ---- Режим ожидания старта ----
        if (isWaitingToStart) {
            background.move(delta);
            bird.hover(startAnimTimer); // плавное покачивание птицы на месте

            if (Gdx.input.justTouched()) {
                isWaitingToStart = false;
                bird.onClick(); // сразу прыгаем при первом тапе
            }

            drawScene();
            drawStartPrompt();
            return;
        }

        // ---- Основная игровая логика ----
        if (Gdx.input.justTouched()) {
            bird.onClick();
        }

        background.move(delta);
        bird.fly(delta);

        if (!bird.isInField()) {
            Gdx.app.log("ScreenGame", "Bird out of field");
            isGameOver = true;
        }

        for (Tube tube : tubes) {
            tube.move(delta);
            if (tube.isHit(bird)) {
                Gdx.app.log("ScreenGame", "Bird hit tube");
                isGameOver = true;
            } else if (tube.needAddPoint(bird)) {
                gamePoints += 1;
                tube.setPointReceived();
                Gdx.app.log("ScreenGame", "Points: " + gamePoints);
            }
        }

        drawScene();

        // Game over ПОСЛЕ отрисовки последнего кадра
        if (isGameOver) {
            myGdxGame.saveBestScore(gamePoints);
            myGdxGame.screenRestart.gamePoints = gamePoints;
            // MUSIC: if (gameMusic != null) gameMusic.stop();
            myGdxGame.setScreen(myGdxGame.screenRestart);
        }
    }

    /** Отрисовка всей сцены */
    private void drawScene() {
        ScreenUtils.clear(1, 0, 0, 1);
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        myGdxGame.batch.begin();

        background.draw(myGdxGame.batch);
        bird.draw(myGdxGame.batch);
        for (Tube tube : tubes) tube.draw(myGdxGame.batch);
        pointCounter.draw(myGdxGame.batch, gamePoints);

        myGdxGame.batch.end();
    }

    /** Надпись поверх сцены перед стартом */
    private void drawStartPrompt() {
        myGdxGame.batch.begin();

        String line1 = "Holy-Snail уже близко...";
        String line2 = "Нажми чтобы бежать!";

        float blink = (float)(Math.sin(startAnimTimer * 4f) * 0.5f + 0.5f);

        GlyphLayout layout1 = new GlyphLayout(startFont, line1);
        startFont.setColor(new Color(1f, 0.85f, 0f, 1f));
        startFont.draw(myGdxGame.batch, line1,
                (SCR_WIDTH - layout1.width) / 2f,
                SCR_HEIGHT / 2f + 80);

        GlyphLayout layout2 = new GlyphLayout(startFont, line2);
        startFont.setColor(new Color(1f, 1f, 1f, blink));
        startFont.draw(myGdxGame.batch, line2,
                (SCR_WIDTH - layout2.width) / 2f,
                SCR_HEIGHT / 2f - 20);

        myGdxGame.batch.end();
    }

    @Override public void resize(int width, int height) { }

    @Override
    public void pause() {
        // MUSIC: if (gameMusic != null) gameMusic.pause();
    }

    @Override
    public void resume() {
        // MUSIC: if (gameMusic != null && !isWaitingToStart && !isGameOver) gameMusic.play();
    }

    @Override
    public void hide() {
        // MUSIC: if (gameMusic != null) gameMusic.stop();
    }

    @Override
    public void dispose() {
        bird.dispose();
        background.dispose();
        pointCounter.dispose();
        startFont.dispose();
        disposeTubes();
        // MUSIC: if (gameMusic != null) gameMusic.dispose();
    }

    void initTubes() {
        tubes = new Tube[tubeCount];
        for (int i = 0; i < tubeCount; i++) {
            tubes[i] = new Tube(tubeCount, i);
        }
    }

    /** Исправлено: dispose текстур труб перед каждым рестартом */
    void disposeTubes() {
        if (tubes != null) {
            for (Tube tube : tubes) {
                if (tube != null) tube.dispose();
            }
        }
    }
}
