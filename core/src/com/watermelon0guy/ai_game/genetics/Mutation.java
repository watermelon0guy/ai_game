package com.watermelon0guy.ai_game.genetics;

import com.watermelon0guy.ai_game.ai.NeuralNetwork;

import java.util.Random;

import static com.watermelon0guy.ai_game.utils.randFloat;


public class Mutation {

    public static NeuralNetwork mutateKartoDaRabotaet(NeuralNetwork experimental, int mutationTimes, Random random) {
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
}
