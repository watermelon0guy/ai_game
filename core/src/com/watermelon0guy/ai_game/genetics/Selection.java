package com.watermelon0guy.ai_game.genetics;

import com.watermelon0guy.ai_game.Creature;
import com.watermelon0guy.ai_game.ai.NeuralNetwork;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Selection {


    public static ArrayList<NeuralNetwork> tournamentSelection(ArrayList<Creature> population, int count, Random random)
    {
        ArrayList<NeuralNetwork> selected = new ArrayList<>();

        int batch = population.size()/5;
        Creature[] c = new Creature[batch];
        for (int i = 0; i < count; i++)
        {
            for (int j = 0; j < batch; j++)
            {
                c[j] = population.get(random.nextInt(population.size()));
            }
            Arrays.sort(c);
            if (!selected.contains(c[0].brain)) selected.add(c[0].brain);
            else i -= 1;
            Arrays.fill(c, null);
        }
        return selected;
    }
}
