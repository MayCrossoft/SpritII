package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class PointCounter {

    int x, y;
    BitmapFont font;

    public PointCounter(int x, int y) {
        this.x = x;
        this.y = y;

        font = new BitmapFont();
        font.getData().setScale(5f);
        font.setColor(Color.WHITE);
    }

    /** Рисует текущий счёт: "Score: N" */
    public void draw(Batch batch, int countOfPoints) {
        font.setColor(Color.WHITE);
        font.draw(batch, "Score: " + countOfPoints, x, y);
    }

    /** Рисует лучший счёт: "Best: N" — золотым цветом */
    public void drawBest(Batch batch, int bestScore) {
        font.setColor(new Color(1f, 0.85f, 0f, 1f)); // золотой
        font.draw(batch, "Best:  " + bestScore, x, y);
        font.setColor(Color.WHITE); // сбрасываем цвет
    }

    public void dispose() {
        font.dispose();
    }
}
