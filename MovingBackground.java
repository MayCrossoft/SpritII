package ru.samsung.gamestudio.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import ru.samsung.gamestudio.MyGdxGame;

public class MovingBackground {

    Texture texture;

    float texture1X, texture2X;
    float speedPixelsPerSecond = 2 * 60; // пиксели в секунду

    public MovingBackground(String pathToTexture) {
        texture1X = 0;
        texture2X = MyGdxGame.SCR_WIDTH;
        texture = new Texture(pathToTexture);
    }

    public void move(float delta) {
        texture1X -= speedPixelsPerSecond * delta;
        texture2X -= speedPixelsPerSecond * delta;

        if (texture1X <= -MyGdxGame.SCR_WIDTH) {
            texture1X = MyGdxGame.SCR_WIDTH;
        }
        if (texture2X <= -MyGdxGame.SCR_WIDTH) {
            texture2X = MyGdxGame.SCR_WIDTH;
        }
    }

    public void draw(Batch batch) {
        batch.draw(texture, texture1X, 0, MyGdxGame.SCR_WIDTH, MyGdxGame.SCR_HEIGHT);
        batch.draw(texture, texture2X, 0, MyGdxGame.SCR_WIDTH, MyGdxGame.SCR_HEIGHT);
    }

    public void dispose() {
        texture.dispose();
    }
}
