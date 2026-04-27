package ru.samsung.gamestudio.characters;

import static ru.samsung.gamestudio.MyGdxGame.SCR_HEIGHT;
import static ru.samsung.gamestudio.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.Random;

public class Tube {

    Texture textureUpperTube;
    Texture textureDownTube;

    Random random;

    float x;
    int gapY;
    int distanceBetweenTubes;

    boolean isPointReceived;

    float speedPixelsPerSecond = 10 * 60; // пиксели в секунду
    final int width = 200;
    final int height = 700;
    int gapHeight = 400;
    int padding = 100;

    public Tube(int tubeCount, int tubeIdx) {
        random = new Random();

        // Равномерное распределение труб по ширине экрана
        distanceBetweenTubes = SCR_WIDTH / tubeCount;
        x = SCR_WIDTH + (float) distanceBetweenTubes * tubeIdx;

        gapY = gapHeight / 2 + padding + random.nextInt(SCR_HEIGHT - 2 * (padding + gapHeight / 2));

        textureUpperTube = new Texture("tubes/tube_flipped.png");
        textureDownTube  = new Texture("tubes/tube.png");

        isPointReceived = false;
    }

    public void draw(Batch batch) {
        batch.draw(textureUpperTube, x, gapY + gapHeight / 2, width, height);
        batch.draw(textureDownTube,  x, gapY - gapHeight / 2 - height, width, height);
    }

    public void move(float delta) {
        x -= speedPixelsPerSecond * delta;
        if (x < -width) {
            isPointReceived = false;
            x += SCR_WIDTH + distanceBetweenTubes;
            gapY = gapHeight / 2 + padding + random.nextInt(SCR_HEIGHT - 2 * (padding + gapHeight / 2));
        }
    }

    public boolean isHit(Bird bird) {
        // Нижняя труба
        if (bird.y <= gapY - gapHeight / 2 && bird.x + bird.width >= x && bird.x <= x + width)
            return true;
        // Верхняя труба
        if (bird.y + bird.height >= gapY + gapHeight / 2 && bird.x + bird.width >= x && bird.x <= x + width)
            return true;
        return false;
    }

    public boolean needAddPoint(Bird bird) {
        return !isPointReceived && bird.x > x + width;
    }

    public void setPointReceived() {
        isPointReceived = true;
    }

    public void dispose() {
        textureDownTube.dispose();
        textureUpperTube.dispose();
    }
}
