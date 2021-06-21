package com.watermelon0guy.ai_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.watermelon0guy.ai_game.ai.NeuralNetwork;
import com.watermelon0guy.ai_game.screens.CreatureConstructorScreen;

import static com.watermelon0guy.ai_game.genetics.Crossover.crossoverMultPoint;
import static com.watermelon0guy.ai_game.genetics.Mutation.mutateKartoDaRabotaet;
import static com.watermelon0guy.ai_game.utils.*;
import static com.watermelon0guy.ai_game.genetics.Selection.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class GeneticAlgorithm {
    public Vector2 spawnPoint;
    int currentGeneration = 0;
    public static int countOfAgentsInOnePopulation = 15;
    public static int savedCount = countOfAgentsInOnePopulation / 10;
    public int mutationTimes = 5;
    public float mutantChance = 0.4f;
    public static float timeForOnePopulation = 10;
    public static float currentTimeForOnePopulation = timeForOnePopulation;
    public ArrayList<Creature> currentPopulation = new ArrayList<Creature>();
    public ArrayList<Creature> nextPopulation = new ArrayList<Creature>();

    public World world;
    Random random;
    CreatureConstructorScreen CCS;
    public SimulationStep simStatus = SimulationStep.SPAWN;
    public enum SimulationStep
    {
        SPAWN,SIM,RESPAWN
    }

    public GeneticAlgorithm(World world, CreatureConstructorScreen CCS) {
        random = new Random();
        this.world = world;
        this.CCS = CCS;
    }

    public void spawnPopulation() {
        for (int i = 0; i < countOfAgentsInOnePopulation; i++)
        {
            currentPopulation.add(newCreature());
        }
    }

    public void act()
    {
        if (currentTimeForOnePopulation >= 0) {
            currentTimeForOnePopulation -= StaticVars.geneticTimeStep;
            move();
        } else {
            currentTimeForOnePopulation = timeForOnePopulation;
            repopulate();
        }
        System.out.println("act");
    }

    Creature newCreature()
    {
        return new Creature(CCS.creature,world, CCS.bodyDefBone, CCS.bodyDefJoint, CCS.circleShape, CCS.shape,CCS.fixture, CCS.springDef);
    }

    public void repopulate()
    {
        currentGeneration += 1;

        int parentsCount = countOfAgentsInOnePopulation - savedCount;
        if (!(parentsCount % 2 == 0)) parentsCount += 1;
        ArrayList<NeuralNetwork> parents = tournamentSelection(currentPopulation,parentsCount,random);

        for (int i = 0; i < parents.size(); i += 2) {
            ArrayList<NeuralNetwork> childs = crossoverMultPoint(parents.get(i), parents.get(i + 1),random);
            nextPopulation.add(0, newCreature());
            nextPopulation.get(0).brain = NetCopy(childs.get(0));

            if (MathUtils.random() < mutantChance) {
                mutateKartoDaRabotaet(nextPopulation.get(0).brain, mutationTimes,random);
            }

            nextPopulation.add(newCreature());
            nextPopulation.get(0).brain = NetCopy(childs.get(1));

            if (MathUtils.random() < mutantChance) {
                mutateKartoDaRabotaet(nextPopulation.get(0).brain, mutationTimes,random);
            }
        }

        Collections.sort(currentPopulation);

        for (int i = 0; i < savedCount; i++) {
            nextPopulation.add(0, newCreature());
            nextPopulation.get(0).brain = NetCopy(currentPopulation.get(0).brain);
        }

        for (Creature creature : currentPopulation) {
            for (JointClass j : creature.joints) {
                world.destroyBody(j.pBody);
            }

            for (BoneClass j : creature.bones) {
                world.destroyBody(j.pBody);
            }
        }
        currentPopulation.clear();
        for (Creature creature : nextPopulation) {
            currentPopulation.add(creature);
        }
        nextPopulation.clear();
    }

    private NeuralNetwork Mutate(NeuralNetwork experimental, int mutationTimes) {
        for (int i = 0; i < mutationTimes; i++) {
            int chance = random.nextInt(4);
            switch (chance) {
                case 0: {
                    experimental.weightsInputLayer[random.nextInt(experimental.weightsInputLayer.length)][random.nextInt(experimental.weightsInputLayer[0].length)] = randFloat(0.99f, -0.99f, random);
                    return experimental;
                }

                case 1: {
                    experimental.weightsHiddenLayers[random.nextInt(experimental.weightsHiddenLayers.length)][random.nextInt(experimental.weightsHiddenLayers[0].length)] = randFloat(0.99f, -0.99f, random);
                    return experimental;
                }

                case 2: {
                    experimental.biasesInputLayer[random.nextInt(experimental.biasesInputLayer.length)][0] = randFloat(0.99f, -0.99f, random);
                    return experimental;
                }

                case 3: {
                    experimental.biasesHiddenLayers[random.nextInt(experimental.biasesHiddenLayers.length)][0] = randFloat(0.99f, -0.99f, random);
                    return experimental;
                }
            }
        }
        return experimental;
    }

    private ArrayList<NeuralNetwork> Crossover(NeuralNetwork parent1, NeuralNetwork parent2) {
        NeuralNetwork childNet1;
        NeuralNetwork childNet2;
        NeuralNetwork p1Net = parent1;
        NeuralNetwork p2Net = parent2;
        childNet1 = NetCopy(p1Net);
        childNet2 = NetCopy(p2Net);


        //веса c входного слоя на скрытый
        for (int i = 0; i < p1Net.weightsInputLayer.length; i++) {
            for (int j = 0; j < p1Net.weightsInputLayer[0].length; j++) {
                if (random.nextFloat() > 0.5f) {
                    childNet1.weightsInputLayer[i][j] = p2Net.weightsInputLayer[i][j];
                    childNet2.weightsInputLayer[i][j] = p1Net.weightsInputLayer[i][j];
                }
            }
        }

        //веса со скрытого на выходной
        for (int i = 0; i < random.nextInt(p1Net.weightsHiddenLayers.length); i++) {
            for (int j = 0; j < random.nextInt(p1Net.weightsHiddenLayers[0].length); j++) {
                if (random.nextFloat() > 0.5f) {
                    childNet1.weightsHiddenLayers[i][j] = p2Net.weightsHiddenLayers[i][j];
                    childNet2.weightsHiddenLayers[i][j] = p1Net.weightsHiddenLayers[i][j];
                }
            }
        }

        //веса входного слоя
        for (int i = 0; i < random.nextInt(p1Net.biasesInputLayer.length); i++) {
            if (random.nextFloat() > 0.5f) {
                childNet1.biasesInputLayer[i][0] = p2Net.biasesInputLayer[i][0];
                childNet2.biasesInputLayer[i][0] = p1Net.biasesInputLayer[i][0];
            }
        }

        //веса скрытого слоя
        for (int i = 0; i < random.nextInt(p1Net.biasesHiddenLayers.length); i++) {
            if (random.nextFloat() > 0.5f) {
                childNet1.biasesHiddenLayers[i][0] = p2Net.biasesHiddenLayers[i][0];
                childNet1.biasesHiddenLayers[i][0] = p1Net.biasesHiddenLayers[i][0];
            }
        }


        ArrayList<NeuralNetwork> childs = new ArrayList<NeuralNetwork>();
        childs.add(childNet1);
        childs.add(childNet2);

        return childs;
    }

    private ArrayList<NeuralNetwork> TournamentChoice(ArrayList<Creature> population) {
        ArrayList<NeuralNetwork> newNets = new ArrayList<NeuralNetwork>();
        while (newNets.size() < countOfAgentsInOnePopulation - 4) {
            Creature first = population.get(random.nextInt(population.size()));
            Creature second = population.get(random.nextInt(population.size()));
            while (first == second) {
                second = population.get(random.nextInt(population.size()));
            }
            if (first.fitness > second.fitness) {
                if (newNets.contains(first.brain)) {
                    continue;
                }
                newNets.add(NetCopy(first.brain));
            } else {
                if (newNets.contains(second.brain)) {
                    continue;
                }
                newNets.add(NetCopy(second.brain));
            }
        }

        return newNets;
    }

    public void move() {
        for (Creature creature : currentPopulation) {
            creature.calculateAndDoMotions();
        }
    }

    public void render(ShapeDrawer shapeDrawer) {
        for (Creature creature : currentPopulation) {
            creature.render(shapeDrawer);
        }
    }

    public static NeuralNetwork NetCopy(NeuralNetwork net) {
        NeuralNetwork copy = new NeuralNetwork(net.inputLayer, net.hiddenLayer, net.hiddenLayerCount, net.outputLayer);
        copy.number = net.number;
        for (int i = 0; i < net.weightsInputLayer.length; i++) {
            for (int j = 0; j < net.weightsInputLayer[0].length; j++) {
                copy.weightsInputLayer[i][j] = net.weightsInputLayer[i][j];
            }
        }

        for (int i = 0; i < net.weightsHiddenLayers.length; i++) {
            for (int j = 0; j < net.weightsHiddenLayers[0].length; j++) {
                copy.weightsHiddenLayers[i][j] = net.weightsHiddenLayers[i][j];
            }
        }

        for (int i = 0; i < net.biasesInputLayer.length; i++) {
            for (int j = 0; j < net.biasesInputLayer[0].length; j++) {
                copy.biasesInputLayer[i][j] = net.biasesInputLayer[i][j];
            }
        }

        for (int i = 0; i < net.biasesHiddenLayers.length; i++) {
            for (int j = 0; j < net.biasesHiddenLayers[0].length; j++) {
                copy.biasesHiddenLayers[i][j] = net.biasesHiddenLayers[i][j];
            }
        }
        return copy;
    }
}
