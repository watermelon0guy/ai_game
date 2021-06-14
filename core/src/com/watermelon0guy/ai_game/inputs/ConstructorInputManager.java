package com.watermelon0guy.ai_game.inputs;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.watermelon0guy.ai_game.BoneClass;
import com.watermelon0guy.ai_game.JointClass;
import com.watermelon0guy.ai_game.MuscleClass;
import com.watermelon0guy.ai_game.screens.CreatureConstructorScreen;
import com.watermelon0guy.ai_game.utils;

public class ConstructorInputManager implements InputProcessor {
    public enum constructorStatus{Bone,Joint,Muscle,Move,Delete}
    public constructorStatus status = constructorStatus.Joint;
    CreatureConstructorScreen CCS;

    float margin = 0.1f;// попадать по объектам довольно трудно. это расширяет "размер" нажатия(в "метрах" box2d)
    //region Переменные для кости
    JointClass joint1 = null;
    JointClass joint2 = null;
    //endregion

    //region Переменные для мышцы
    BoneClass bone1 = null;
    BoneClass bone2 = null;
    //endregion

    //region Переменные движения
    JointClass moveJoint;
    //endregion

    public ConstructorInputManager(CreatureConstructorScreen ccs){CCS = ccs;}

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
            case Move:
            {
                return moveDown(screenX, screenY, pointer, button);
            }
            case Delete:
            {
                return deleteDown(screenX, screenY, pointer, button);
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("up");
        switch (status)
        {
            case Joint:
            {
                return jointUp(screenX, screenY, pointer, button);
            }
            case Bone:
            {
                return boneUp(screenX, screenY, pointer, button);
            }
            case Muscle:
            {
                return muscleUp(screenX, screenY, pointer, button);
            }
            case Move:
            {
                return moveUp(screenX, screenY, pointer, button);
            }
            case Delete:
            {
                return deleteUp(screenX, screenY, pointer, button);
            }
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //System.out.println("drag");
        switch (status)
        {
            case Joint:
            {
                return false;
            }
            case Bone:
            {
                return false;
            }
            case Muscle:
            {
                return false;
            }
            case Move:
            {
                return moveDrag(screenX,screenY,pointer);
            }
            case Delete:
            {
                return false;
            }
        }
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

    public boolean jointUp(int screenX, int screenY, int pointer, int button)
    {
        System.out.println("Joint Mode: up");
        new JointClass(utils.screenToWorldPosV2(CCS.camera, screenX,screenY), CCS.world, CCS.creature, CCS.bodyDefJoint, CCS.circleShape, CCS.fixture);
        return true;
    }
    //endregion
    //region Кость
    public boolean boneDown(int screenX, int screenY, int pointer, int button)
    {
        System.out.println("Bone Mode: down");
        Vector2 worldPos = utils.screenToWorldPosV2(CCS.camera,screenX,screenY);
        CCS.world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                if (fixture.getUserData() instanceof JointClass)
                {
                    joint1 = null;
                    joint2 = null;
                    joint1 = (JointClass)fixture.getUserData();
                    return false;
                }
                return true;
            }
        },worldPos.x-margin,worldPos.y-margin,worldPos.x+margin,worldPos.y+margin);
        return true;
    }

    public boolean boneUp(int screenX, int screenY, int pointer, int button)
    {
        System.out.println("Bone Mode: up");
        Vector2 worldPos = utils.screenToWorldPosV2(CCS.camera,screenX,screenY);
        CCS.world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                if (fixture.getUserData() instanceof JointClass & joint1 != null)
                {
                    joint2 = (JointClass)fixture.getUserData();
                    for (BoneClass bone: CCS.creature.bones) {
                        if (bone.connectedJoints.contains(joint1) && (bone.connectedJoints.contains(joint2))) return false;
                    }
                    BoneClass bone = new BoneClass(joint1,joint2, CCS.world, CCS.creature, CCS.bodyDefBone,CCS.shape,CCS.fixture);
                    return false;
                }
                return true;
            }
        },worldPos.x-margin,worldPos.y-margin,worldPos.x+margin,worldPos.y+margin);
        return true;
    }
    //endregion
    //region Мышца
    public boolean muscleDown(int screenX, int screenY, int pointer, int button)
    {
        System.out.println("Muscle Mode: down");
        Vector2 worldPos = utils.screenToWorldPosV2(CCS.camera,screenX,screenY);
        CCS.world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                if (fixture.getUserData() instanceof BoneClass)
                {
                    bone1 = null;
                    bone2 = null;
                    bone1 = (BoneClass)fixture.getUserData();
                    return false;
                }
                return true;
            }
        },worldPos.x-margin,worldPos.y-margin,worldPos.x+margin,worldPos.y+margin);
        return true;
    }

    public boolean muscleUp(int screenX, int screenY, int pointer, int button)
    {
        System.out.println("Muscle Mode: up");
        final Vector2 worldPos = utils.screenToWorldPosV2(CCS.camera,screenX,screenY);
        CCS.world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                if (fixture.getUserData() instanceof BoneClass & bone1 != null)
                {
                    bone2 = (BoneClass)fixture.getUserData();
                    for (MuscleClass muscle: CCS.creature.muscles) {
                        if (muscle.connectedBones.contains(bone1) && (muscle.connectedBones.contains(bone2))) return false;
                    }
                    new MuscleClass(bone1,bone2, CCS.world, CCS.creature, CCS.springDef);
                    return false;
                }
                bone1 = null;
                bone2 = null;
                return true;
            }
        },worldPos.x-margin,worldPos.y-margin,worldPos.x+margin,worldPos.y+margin);
        return true;
    }
    //endregion
    //region Move
    public boolean moveDown(int screenX, int screenY, int pointer, int button)
    {
        System.out.println("Move Mode: down");
        Vector2 worldPos = utils.screenToWorldPosV2(CCS.camera,screenX,screenY);
        CCS.world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                if (fixture.getUserData() instanceof JointClass)
                {
                    moveJoint = (JointClass)fixture.getUserData();
                    return false;
                }
                return true;
            }
        },worldPos.x-margin,worldPos.y-margin,worldPos.x+margin,worldPos.y+margin);
        return true;
    }

    public boolean moveUp(int screenX, int screenY, int pointer, int button)
    {
        System.out.println("Move Mode: up");



        moveJoint = null;
        return true;
    }

    public boolean moveDrag(int screenX, int screenY, int pointer)
    {
        if (moveJoint == null) return true;
        Vector2 worldPos = utils.screenToWorldPosV2(CCS.camera,screenX,screenY);
        moveJoint.pBody.setTransform(worldPos,0);
        for (BoneClass bone: moveJoint.connectedBones)
        {
            bone.rebuild(CCS.fixture, CCS.shape);
            for (MuscleClass muscle: bone.connectedMuscles)
            {
                muscle.rebuild(CCS.world, CCS.springDef);
            }
        }
        return true;
    }
    //endregion
    //region Delete
    public boolean deleteDown(int screenX, int screenY, int pointer, int button)
    {
        System.out.println("Delete Mode: down");
        return true;
    }

    public boolean deleteUp(int screenX, int screenY, int pointer, int button)
    {
        System.out.println("Delete Mode: up");
        Vector2 worldPos = utils.screenToWorldPosV2(CCS.camera,screenX,screenY);
        CCS.world.QueryAABB(new QueryCallback() {
            @Override
            public boolean reportFixture(Fixture fixture) {
                if (fixture.getUserData() instanceof JointClass)
                {
                    JointClass jointClass = (JointClass)fixture.getUserData();
                    jointClass.delete(CCS.world);
                    return false;
                }
                if (fixture.getUserData() instanceof BoneClass)
                {
                    BoneClass boneClass = (BoneClass)fixture.getUserData();
                    boneClass.delete(CCS.world);
                    return false;
                }
                if (fixture.getUserData() instanceof MuscleClass)
                {
                    return false;
                }
                return true;
            }
        },worldPos.x-margin,worldPos.y-margin,worldPos.x+margin,worldPos.y+margin);
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
