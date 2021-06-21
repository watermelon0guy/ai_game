package com.watermelon0guy.ai_game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.watermelon0guy.ai_game.ai.NeuralNetwork;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class Creature implements Comparable<Creature>{
    public float fitness;
    public int pointOfContacts;
    public ArrayList<BoneClass> bones;
    public ArrayList<JointClass> joints;
    public ArrayList<MuscleClass> muscles;

    public Vector2 position = new Vector2();
    public NeuralNetwork brain;
    public float[] inputsOfNN = new float[6];
    public Vector2 bottomJoint = new Vector2(0,1);

    private int lastUsedId;

    public Creature()
    {
        joints = new ArrayList<JointClass>();
        bones = new ArrayList<BoneClass>();
        muscles = new ArrayList<MuscleClass>();

    }

    public Creature(Creature parent, World world, BodyDef bodyDefBone,BodyDef bodyDefJoint, CircleShape circleShape, PolygonShape shape, FixtureDef fixture, DistanceJointDef springDef)
    {
        joints = new ArrayList<JointClass>();
        bones = new ArrayList<BoneClass>();
        muscles = new ArrayList<MuscleClass>();

        for (JointClass jointClass: parent.joints) {
            new JointClass(jointClass.pos().add(bottomJoint),world,this,bodyDefJoint,circleShape,fixture).setId(jointClass);
        }
        for (BoneClass boneClass: parent.bones) {
            int j1 = boneClass.connectedJoints.get(0).id;
            int j2 = boneClass.connectedJoints.get(1).id;

            JointClass joint1 = null;
            JointClass joint2 = null;
            for (JointClass jointClass:joints)
            {
                if (jointClass.id == j1)
                {
                    joint1 = jointClass;
                }
                else if (jointClass.id == j2)
                {
                    joint2 = jointClass;
                }
            }

            new BoneClass(joint1,joint2,world,this,bodyDefBone,shape,fixture).setId(boneClass);
        }

        for (MuscleClass muscleClass: parent.muscles)
        {
            int b1 = muscleClass.connectedBones.get(0).id;
            int b2 = muscleClass.connectedBones.get(1).id;

            BoneClass bone1 = null;
            BoneClass bone2 = null;

            for (BoneClass boneClass: bones)
            {
                if (boneClass.id == b1)
                {
                    bone1 = boneClass;
                }
                else if (boneClass.id == b2)
                {
                    bone2 = boneClass;
                }
            }

            new MuscleClass(bone1,bone2,world,this,springDef).setId(muscleClass);
        }

        brain = new NeuralNetwork(6,5,1,muscles.size());
    }

    public void findBottomLeftJoint()
    {
        for (JointClass jointClass: joints) {
            if (jointClass.pos().y < bottomJoint.y)
            {
                bottomJoint.y = jointClass.pos().y;
            }
        }
    }

    public void render(ShapeDrawer shapeDrawer)
    {
        for(MuscleClass m: muscles)
        {
            m.render(shapeDrawer);
        }
        for(BoneClass b: bones)
        {
            b.render(shapeDrawer);
        }
        for(JointClass j: joints)
        {
            j.render(shapeDrawer);
        }
    }

    public  void muscleControl(float[] commands)
    {
        for(int i = 0; i < muscles.size();i++)
        {
            muscles.get(i).setLength(commands[i]);
        }
    }

    public void calculateAndDoMotions()
    {
        fitness = pos().x;
        createInputsForNN();
        int inp = 0;
        for(float input: inputsOfNN)
        {
            brain.inputNodes[inp][0] = input;
            inp++;
        }
        double[][] output = brain.calculate();
        float[] controlData = new float[muscles.size()];
        for(int i = 0; i < output.length;i++)
        {
            controlData[i] = (float)output[i][0];
        }

        muscleControl(controlData);
    }

    public float getXPosition() {

        float total = 0;

        int jointsCount = joints.size();
        for (int i = 0; i < jointsCount; i++) {
            total += joints.get(i).pos().x;
        }

        return jointsCount == 0 ? 0 : total / jointsCount ;
    }

    public float getYPosition() {

        float total = 0;

        int jointsCount = joints.size();
        for (int i = 0; i < jointsCount; i++) {
            total += joints.get(i).pos().y;
        }

        return jointsCount == 0 ? 0 : total / jointsCount ;
    }

    public void createInputsForNN()
    {

        inputsOfNN[0] = pointOfContacts;//кол-во точек соприкосновения
        for(JointClass j: joints)
        {
            inputsOfNN[1] += j.pBody.getLinearVelocity().x;//скорость
            inputsOfNN[2] += j.pBody.getLinearVelocity().y;
            if (j.pos().y > inputsOfNN[5])
                inputsOfNN[5] = j.pos().y;
            if (j.pos().y < inputsOfNN[5])//растояние до земли
                inputsOfNN[5] = j.pos().y;
        }
        inputsOfNN[1] /= joints.size();
        inputsOfNN[2] /= joints.size();

        for(BoneClass b: bones)//угол поворота всех костей в теле
        {
            inputsOfNN[3] += b.pBody.getAngle();
            inputsOfNN[4] += b.pBody.getAngularVelocity();
        }
        inputsOfNN[3] /= bones.size();
        inputsOfNN[4] /= bones.size();
    }

    public Vector2 pos()
    {
        float x = 0;
        float y = 0;
        for(JointClass j: joints)
        {
            x += j.pos().x;
            y += j.pos().y;
        }
        return new Vector2(x/joints.size(), y/joints.size());
    }

    public int getUniqueId()
    {
        lastUsedId +=1;
        return lastUsedId;
    }

    @Override
    public int compareTo(@NotNull Creature o) {
        if (this.fitness == o.fitness)
        {
            return 0;
        }
        return this.fitness > o.fitness ? 1 : -1;
    }
}