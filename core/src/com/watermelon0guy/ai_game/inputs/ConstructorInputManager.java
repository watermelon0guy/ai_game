package com.watermelon0guy.ai_game.inputs;

import com.badlogic.gdx.InputProcessor;

public class ConstructorInputManager implements InputProcessor {
    public enum constructorStatus{Bone,Joint,Muscle}
    constructorStatus status = constructorStatus.Joint;


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("down");

        switch (status)
        {
            case Joint:
            {
                return jointDown(screenX, screenY, pointer, button);
            }
            case Bone:
            {
                return boneDown(screenX, screenY, pointer, button);
            }
            case Muscle:
            {
                return muscleDown(screenX, screenY, pointer, button);
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("up");
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        System.out.println("drag");
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    //region Сустав
    public boolean jointDown(int screenX, int screenY, int pointer, int button)
    {
        System.out.println("Joint Mode: down");

        return true;
    }
    //endregion
    //region Кость
    public boolean boneDown(int screenX, int screenY, int pointer, int button)
    {
        System.out.println("Bone Mode: down");
        return true;
    }
    //endregion
    //region Мышца
    public boolean muscleDown(int screenX, int screenY, int pointer, int button)
    {
        System.out.println("Muscle Mode: down");
        return true;
    }
    //endregion
    //region Не используемые события
    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    //endregion

}
