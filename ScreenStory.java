package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

import ru.samsung.gamestudio.MyGdxGame;
import ru.samsung.gamestudio.components.MovingBackground;

public class ScreenStory implements Screen {

    MyGdxGame myGdxGame;
    MovingBackground background;

    BitmapFont titleFont;
    BitmapFont textFont;
    BitmapFont hintFont;

    // Страницы истории
    String[] pages = {
        "Давным-давно в одном уютном городе\nжил Rain-Gosling — обычный улиткин\nсосед, который просто хотел жить спокойно.",

        "Но однажды к нему явилась Holy-Snail —\nагент компании Gaijin.\nОна несла с собой один контракт:\nкупить Turms-T. Навсегда.",

        "Rain-Gosling отказался.\n\n«Я не куплю твой Turms-T!» — закричал он\nи бросился бежать со всех ног.",

        "Holy-Snail не отстаёт.\nОна мчится следом, размахивая договором.\n\nПомоги Rain-Gosling сбежать!\nНажми чтобы начать."
    };

    int currentPage = 0;
    float pageTimer = 0f;
    final float AUTO_ADVANCE_TIME = 5f; // секунд до автоперехода

    float alpha = 0f;       // для fade-in
    boolean fadingIn = true;

    public ScreenStory(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;
        background = new MovingBackground("backgrounds/restart_bg.png");

        titleFont = new BitmapFont();
        titleFont.getData().setScale(4f);
        titleFont.setColor(Color.YELLOW);

        textFont = new BitmapFont();
        textFont.getData().setScale(2.8f);
        textFont.setColor(Color.WHITE);

        hintFont = new BitmapFont();
        hintFont.getData().setScale(2f);
        hintFont.setColor(new Color(1f, 1f, 1f, 0.6f));
    }

    @Override
    public void show() {
        currentPage = 0;
        pageTimer = 0f;
        alpha = 0f;
        fadingIn = true;
    }

    @Override
    public void render(float delta) {
        background.move(delta);
        pageTimer += delta;

        // Fade in
        if (fadingIn) {
            alpha += delta * 2f;
            if (alpha >= 1f) { alpha = 1f; fadingIn = false; }
        }

        // Автопереход
        if (pageTimer >= AUTO_ADVANCE_TIME) {
            nextPage();
        }

        // Тап — переход
        if (Gdx.input.justTouched()) {
            nextPage();
        }

        ScreenUtils.clear(0.05f, 0.05f, 0.15f, 1);
        myGdxGame.camera.update();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        myGdxGame.batch.begin();

        background.draw(myGdxGame.batch);

        // Затемнение поверх фона для читаемости текста
        // (фон рисуется с opacity через batch color)
        myGdxGame.batch.setColor(0f, 0f, 0f, 0.5f);
        // Рисуем полупрозрачный прямоугольник через белую текстуру недоступна без ShapeRenderer,
        // поэтому просто рисуем текст с тенью ниже

        myGdxGame.batch.setColor(1f, 1f, 1f, alpha);

        // Заголовок
        String title = "Escape from Turmsik";
        GlyphLayout titleLayout = new GlyphLayout(titleFont, title);
        titleFont.setColor(new Color(1f, 0.85f, 0f, alpha));
        titleFont.draw(myGdxGame.batch, title,
                (MyGdxGame.SCR_WIDTH - titleLayout.width) / 2f,
                MyGdxGame.SCR_HEIGHT - 80);

        // Номер страницы
        String pageIndicator = (currentPage + 1) + " / " + pages.length;
        GlyphLayout pageLayout = new GlyphLayout(hintFont, pageIndicator);
        hintFont.setColor(new Color(1f, 1f, 1f, alpha * 0.7f));
        hintFont.draw(myGdxGame.batch, pageIndicator,
                (MyGdxGame.SCR_WIDTH - pageLayout.width) / 2f,
                MyGdxGame.SCR_HEIGHT - 150);

        // Текст страницы
        textFont.setColor(new Color(1f, 1f, 1f, alpha));
        GlyphLayout textLayout = new GlyphLayout(textFont, pages[currentPage]);
        textFont.draw(myGdxGame.batch, pages[currentPage],
                (MyGdxGame.SCR_WIDTH - textLayout.width) / 2f,
                MyGdxGame.SCR_HEIGHT / 2f + textLayout.height / 2f + 60);

        // Подсказка внизу
        String hint = currentPage < pages.length - 1
                ? "Нажми чтобы продолжить..."
                : "Нажми чтобы начать!";
        // Мигание подсказки
        float hintAlpha = (float)(Math.sin(pageTimer * 3f) * 0.5f + 0.5f) * alpha;
        GlyphLayout hintLayout = new GlyphLayout(hintFont, hint);
        hintFont.setColor(new Color(1f, 1f, 0.5f, hintAlpha));
        hintFont.draw(myGdxGame.batch, hint,
                (MyGdxGame.SCR_WIDTH - hintLayout.width) / 2f,
                100);

        // Прогресс-бар времени до автоперехода
        float progress = pageTimer / AUTO_ADVANCE_TIME;
        float barWidth = MyGdxGame.SCR_WIDTH * 0.6f;
        float barX = (MyGdxGame.SCR_WIDTH - barWidth) / 2f;
        // Нарисуем через batch цвет (белая текстура недоступна, пропускаем)

        myGdxGame.batch.setColor(1f, 1f, 1f, 1f); // сбрасываем цвет

        myGdxGame.batch.end();
    }

    private void nextPage() {
        if (currentPage < pages.length - 1) {
            currentPage++;
            pageTimer = 0f;
            alpha = 0f;
            fadingIn = true;
        } else {
            myGdxGame.setScreen(myGdxGame.screenMenu);
        }
    }

    @Override public void resize(int width, int height) { }
    @Override public void pause() { }
    @Override public void resume() { }
    @Override public void hide() { }

    @Override
    public void dispose() {
        background.dispose();
        titleFont.dispose();
        textFont.dispose();
        hintFont.dispose();
    }
}
