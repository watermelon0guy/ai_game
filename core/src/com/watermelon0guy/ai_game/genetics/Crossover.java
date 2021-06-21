package com.watermelon0guy.ai_game.genetics;

import com.badlogic.gdx.math.MathUtils;
import com.watermelon0guy.ai_game.ai.NeuralNetwork;

import java.util.ArrayList;
import java.util.Random;

import static com.watermelon0guy.ai_game.GeneticAlgorithm.NetCopy;

public class Crossover {

    public static ArrayList<NeuralNetwork> crossoverMultPoint(NeuralNetwork parent1, NeuralNetwork parent2, Random random) {
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
}
