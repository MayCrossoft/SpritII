package ru.samsung.gamestudio.characters;

import static ru.samsung.gamestudio.MyGdxGame.SCR_HEIGHT;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Bird {

    int x;
    float y;
    float baseY; // исходная Y для анимации покачивания
    int width, height;

    float speedPixelsPerSecond;
    float jumpVelocity;
    boolean jump = false;

    int frameCounter;
    Texture[] framesArray;

    public Bird(int x, int y, float speedPixelsPerSecond, int width, int height) {
        this.x = x;
        this.y = y;
        this.baseY = y;
        this.speedPixelsPerSecond = speedPixelsPerSecond * 60; // конвертируем в пиксели/сек
        this.width = width;
        this.height = height;
        frameCounter = 0;

        framesArray = new Texture[]{
                new Texture("birdTiles/bird0.png"),
                new Texture("birdTiles/bird1.png"),
                new Texture("birdTiles/bird2.png"),
                new Texture("birdTiles/bird1.png"),
        };
    }

    public void setY(int y) {
        this.y = y;
        this.baseY = y;
        this.jump = false;
        this.jumpVelocity = 0;
    }

    public void onClick() {
        jump = true;
        // Прыжок всегда на фиксированную высоту, не зависит от текущей Y
        jumpVelocity = speedPixelsPerSecond;
    }

    public void fly(float delta) {
        if (jump) {
            y += speedPixelsPerSecond * delta;
            jumpVelocity -= speedPixelsPerSecond * delta;
            if (jumpVelocity <= 0) {
                jump = false;
            }
        } else {
            y -= speedPixelsPerSecond * delta;
        }
    }

    /**
     * Плавное покачивание на месте — для экрана ожидания старта.
     * @param timer нарастающий таймер из ScreenGame
     */
    public void hover(float timer) {
        float amplitude = 20f; // пикселей вверх-вниз
        float frequency = 2f;  // циклов в секунду
        y = baseY + (float)(Math.sin(timer * frequency * Math.PI * 2f) * amplitude);
    }

    public boolean isInField() {
        if (y + height < 0) return false;
        if (y > SCR_HEIGHT) return false;
        return true;
    }

    public void draw(Batch batch) {
        int frameMultiplier = 10;
        batch.draw(framesArray[frameCounter / frameMultiplier], x, y, width, height);
        if (frameCounter++ == framesArray.length * frameMultiplier - 1) frameCounter = 0;
    }

    public void dispose() {
        for (Texture texture : framesArray) {
            texture.dispose();
        }
    }
}
