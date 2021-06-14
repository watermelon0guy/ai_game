package com.watermelon0guy.ai_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.watermelon0guy.ai_game.ai.NeuralNetwork;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import space.earlygrey.shapedrawer.ShapeDrawer;

public class GeneticAlgorithm {
    public Creature creature;
    public Vector2 spawnPoint;
    int currentGeneration = 0;
    public int countOfAgentsInOnePopulation = 15;
    public int mutationTimes = 5;
    public float mutantChance = 0.1f;
    public int countOfAlive = 20;
    public float timeForOnePopulation = 20;
    public float currentTimeForOnePopulation = 20;
    public ArrayList<Creature> currentPopulation = new ArrayList<Creature>();
    public ArrayList<Creature> nextPopulation = new ArrayList<Creature>();

    public boolean nextWave = true;
    public boolean start = true;
    public World world;
    Random random;

    public GeneticAlgorithm(World world)
    {
        random = new Random();
        this.world = world;
    }

    public void spawnPopulation()
    {
        for (int i = 0; i < countOfAgentsInOnePopulation; i++)
        {
            currentPopulation.add(hardcodeCreature());
        }
    }
     public void act()
     {
         if(currentTimeForOnePopulation>=0)
         {
             currentTimeForOnePopulation -= Gdx.graphics.getDeltaTime();
             move();
         }
         else
         {
             currentTimeForOnePopulation = timeForOnePopulation;
             Repopulate();
         }
     }

    private void fitnessCalculate(ArrayList<Creature> currentPopulation) {
        Collections.sort(currentPopulation);
        Creature best = currentPopulation.get(0);
        float bestResult = currentPopulation.get(0).fitness;
    }

    public void Repopulate()
    {
        currentGeneration += 1;
        fitnessCalculate(currentPopulation);

        for (int i = 0; i < 5; i++)
        {
            nextPopulation.add(0,hardcodeCreature());
            nextPopulation.get(0).brain = NetCopy(currentPopulation.get(0).brain);
        }

        ArrayList<NeuralNetwork> parents = TournamentChoice(currentPopulation);

        for (int i = 0; i < parents.size() - 1; i+=2)
        {
            ArrayList<NeuralNetwork> childs = Crossover(parents.get(i), parents.get(i + 1));
            nextPopulation.add(0,hardcodeCreature());
            nextPopulation.get(0).brain = NetCopy(childs.get(0));

            if(MathUtils.random() < mutantChance)
            {
                Mutate(nextPopulation.get(0).brain,mutationTimes);
            }

            nextPopulation.add(hardcodeCreature());
            nextPopulation.get(0).brain = NetCopy(childs.get(1));

            if (MathUtils.random() < mutantChance)
            {
                Mutate( nextPopulation.get(0).brain, mutationTimes);
            }
        }

        for(Creature creature: currentPopulation)
        {
            for(JointClass j: creature.joints)
            {
                world.destroyBody(j.pBody);
            }

            for(BoneClass j: creature.bones)
            {
                world.destroyBody(j.pBody);
            }
        }
        currentPopulation.clear();
        for (Creature creature: nextPopulation)
        {
            currentPopulation.add(creature);
        }
        nextPopulation.clear();
    }

    private NeuralNetwork Mutate(NeuralNetwork experimental, int mutationTimes) {
        for (int i = 0; i < mutationTimes; i++)
        {
            int chance = random.nextInt(4+1);///!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            switch (chance)
            {
                case 0:
                {
                    experimental.weightsInputLayer[random.nextInt(experimental.weightsInputLayer.length)][random.nextInt(experimental.weightsInputLayer[0].length)] = randFloat(0.99f, -0.99f,random);
                    return experimental;
                }

                case 1:
                {
                    experimental.weightsHiddenLayers[random.nextInt(experimental.weightsHiddenLayers.length)][random.nextInt(experimental.weightsHiddenLayers[0].length)] = randFloat(0.99f, -0.99f,random);
                    return experimental;
                }

                case 2:
                {
                    experimental.biasesInputLayer[random.nextInt(experimental.biasesInputLayer.length)][0] = randFloat(0.99f, -0.99f,random);
                    return experimental;
                }

                case 3:
                {
                    experimental.biasesHiddenLayers[random.nextInt(experimental.biasesHiddenLayers.length)][0] = randFloat(0.99f, -0.99f,random);
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
        for (int i = 0; i < p1Net.weightsInputLayer.length; i++)
        {
            for (int j = 0; j < p1Net.weightsInputLayer[0].length; j++)
            {
                if (random.nextFloat() > 0.5f)
                {
                    childNet1.weightsInputLayer[i][j] = p2Net.weightsInputLayer[i][j];
                    childNet2.weightsInputLayer[i][j] = p1Net.weightsInputLayer[i][j];
                }
            }
        }

        //веса со скрытого на выходной
        for (int i = 0; i < random.nextInt(p1Net.weightsHiddenLayers.length); i++)
        {
            for (int j = 0; j < random.nextInt(p1Net.weightsHiddenLayers[0].length); j++)
            {
                if (random.nextFloat() > 0.5f)
                {
                    childNet1.weightsHiddenLayers[i][j] = p2Net.weightsHiddenLayers[i][j];
                    childNet2.weightsHiddenLayers[i][j] = p1Net.weightsHiddenLayers[i][j];
                }
            }
        }

        //веса входного слоя
        for (int i = 0; i < random.nextInt(p1Net.biasesInputLayer.length); i++)
        {
            if (random.nextFloat() > 0.5f)
            {
                childNet1.biasesInputLayer[i][0] = p2Net.biasesInputLayer[i][0];
                childNet2.biasesInputLayer[i][0] = p1Net.biasesInputLayer[i][0];
            }
        }

        //веса скрытого слоя
        for (int i = 0; i < random.nextInt(p1Net.biasesHiddenLayers.length); i++)
        {
            if (random.nextFloat() > 0.5f)
            {
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
        while (newNets.size() < countOfAgentsInOnePopulation - 4)
        {
            Creature first = population.get(random.nextInt(population.size()));
            Creature second = population.get(random.nextInt(population.size()));
            while (first == second)
            {
                second = population.get(random.nextInt(population.size()));
            }
            if (first.fitness > second.fitness)
            {
                if (newNets.contains(first.brain))
                {
                    continue;
                }
                newNets.add(NetCopy(first.brain));
            }
            else
            {
                if (newNets.contains(second.brain))
                {
                    continue;
                }
                newNets.add(NetCopy(second.brain));
            }
        }

        return newNets;
    }

    public Creature hardcodeCreature()
    {
        creature = new Creature();
        return creature;
    }

    public void move()
    {
        for(Creature creature: currentPopulation)
        {
            creature.calculateAndDoMotions();
        }
    }

    public void render(ShapeDrawer shapeDrawer)
    {
        for(Creature creature: currentPopulation)
        {
            creature.render(shapeDrawer);
        }
    }

    public static float randFloat(float min, float max, Random rand)
    {
        float randomNum = min + rand.nextFloat() * (max - min);
        System.out.println(randomNum);
        return randomNum;
    }

    public static int randInt(int min, int max, Random rand) {

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    NeuralNetwork NetCopy(NeuralNetwork net)
    {
        NeuralNetwork copy = new NeuralNetwork(6,5,1, creature.muscles.size());
        copy.number = net.number;
        for (int i = 0; i < net.weightsInputLayer.length; i++)
        {
            for (int j = 0; j < net.weightsInputLayer[0].length; j++)
            {
                copy.weightsInputLayer[i][j] = net.weightsInputLayer[i][j];
            }
        }

        for (int i = 0; i < net.weightsHiddenLayers.length; i++)
        {
            for (int j = 0; j < net.weightsHiddenLayers[0].length; j++)
            {
                copy.weightsHiddenLayers[i][j] = net.weightsHiddenLayers[i][j];
            }
        }

        for (int i = 0; i < net.biasesInputLayer.length; i++)
        {
            for (int j = 0; j < net.biasesInputLayer[0].length; j++)
            {
                copy.biasesInputLayer[i][j] = net.biasesInputLayer[i][j];
            }
        }

        for (int i = 0; i < net.biasesHiddenLayers.length; i++)
        {
            for (int j = 0; j < net.biasesHiddenLayers[0].length; j++)
            {
                copy.biasesHiddenLayers[i][j] = net.biasesHiddenLayers[i][j];
            }
        }
        return copy;
    }
}
