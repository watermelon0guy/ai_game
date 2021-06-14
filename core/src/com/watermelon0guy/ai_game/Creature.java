package com.watermelon0guy.ai_game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
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

    public NeuralNetwork brain;
    public float[] inputsOfNN = new float[6];

    public Creature()
    {
        joints = new ArrayList<JointClass>();
        bones = new ArrayList<BoneClass>();
        muscles = new ArrayList<MuscleClass>();

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


    public void createMuscleStructure(World world)
    {
        for (MuscleClass musle: muscles)
        {

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
        createInputsForNN();
        int inp = 0;
        for(float input: inputsOfNN)
        {
            brain.inputNodes[inp][0] = input;
            inp++;
        }
        double[][] output = brain.Calculate();
        float[] controllData = new float[muscles.size()];
        for(int i = 0; i < output.length;i++)
        {
            controllData[i] = (float)output[i][0];
        }
        muscleControl(controllData);
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

    @Override
    public int compareTo(@NotNull Creature o) {
        if (this.fitness == o.fitness)
        {
            return 0;
        }
        return this.fitness > o.fitness ? 1 : -1;
    }
}
