package com.watermelon0guy.ai_game.ai;

import java.util.Random;

public class NeuralNetwork {
    Random random = new Random();
    public int number;

    public int inputLayer, hiddenLayer, hiddenLayerCount, outputLayer;

    public double[][] inputNodes;
    public double[][] hiddenNodes;
    public double[][] outputNodes;
    public double[][] weightsInputLayer;
    public double[][] weightsHiddenLayers;
    public double[][] biasesInputLayer;
    public double[][] biasesHiddenLayers;

    public NeuralNetwork(int inputLayer,int hiddenLayer, int hiddenLayerCount, int outputLayer)
    {
        this.inputLayer = inputLayer;
        this.hiddenLayer = hiddenLayer;
        this.hiddenLayerCount = hiddenLayerCount;
        this.outputLayer = outputLayer;


        inputNodes = new double[inputLayer][1];
        hiddenNodes = new double[hiddenLayer][hiddenLayerCount];
        outputNodes = new double[outputLayer][1];
        weightsInputLayer = new double[inputLayer][hiddenLayer];
        weightsHiddenLayers = new double[hiddenLayer][outputLayer];
        biasesInputLayer = new double[hiddenLayer][1];
        biasesHiddenLayers = new double[outputLayer][1];
        RandomiseValue();
    }

    NeuralNetwork(){}

    //public kindsOfActivationFunction kindOfActivationFunction = kindsOfActivationFunction.TanH;
    public enum kindsOfActivationFunction
    {
        Sigmoid,
        TanH
    }

    public void RandomiseValue()
    {
        number = random.nextInt(10000)+1;

        for (int i = 0; i < inputLayer; i++)//случайно выбираем веса входного слоя
        {
            for (int j = 0; j < hiddenLayer; j++)
            {
                weightsInputLayer[i][j] = randInt(-0.5f, 0.5f,random);
            }
        }

        for (int i = 0; i < hiddenLayer; i++)//случайно выбираем веса скрытых слоёв
        {
            for (int j = 0; j < outputLayer; j++)
            {
                weightsHiddenLayers[i][j] = randInt(-0.5f, 0.5f,random);
            }
        }

        for (int i = 0; i < hiddenLayer; i++)//случайно выбираем сдвиги входного слоя
        {
            biasesInputLayer[i][0] = randInt(-0.5f, 0.5f,random);
        }

        for (int i = 0; i < outputLayer; i++)//случайно выбираем сдвиги входного слоя
        {
            biasesHiddenLayers[i][0] = randInt(-0.5f, 0.5f,random);
        }
    }

    public double[][] Calculate()
    {
        hiddenNodes = Sigmoid(Add(DotProduct(Transpose(weightsInputLayer), inputNodes), biasesInputLayer));
        outputNodes = Sigmoid(Add(DotProduct(Transpose(weightsHiddenLayers), hiddenNodes), biasesHiddenLayers));
        return outputNodes;
    }

    private double[][] DotProduct(double [][] m1, double [][] m2)//так как наши массивы представляют из себя анналог матрицы, то мы используем метод Dot Product для перемножения двух матриц
    {

        int rowsA = m1.length;
        int colsA = m1[0].length;

        int rowsB = m2.length;
        int colsB = m2[0].length;

        double[][] result = new double[rowsA][colsB];
        int rowsRes = result.length;
        int colsRes = result[0].length;

        for (int i = 0; i < rowsRes; i++)//случайно выбираем веса скрытых слоёв
        {
            for (int j = 0; j < colsRes; j++)
            {
                double sum = 0;
                for (int k = 0; k < colsA;k++)
                {
                    sum += m1[i][k] * m2[k][j];
                }

                result[i][j] = sum;
            }
        }
        return result;
    }

    private double[][] Transpose(double[][] m)//функция для отражения матрицы-массива по диагонали
    {
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
        {
            for (int j = 0; j < m[0].length; j++)
            {
                temp[j][i] = m[i][j];
            }
        }
        return temp;
    }

    private double[][] Add(double[][] m1, double[][] m2)
    {
        double[][] temp = new double[m1.length][ m1[0].length];
        for (int i = 0; i < m1[0].length; i++)
        {
            for (int j = 0; j < m1.length; j++)
            {
                temp[j][i] = m1[j][i] + m2[j][i];
            }
        }
        return temp;
    }
    ///////////////////////////////////////////////////////////////////
    private double Sigmoid(double x)
    {
        return (1.0f / (1.0f + Math.exp((float)-x)));
    }

    private double[][] Sigmoid(double[][] m)
    {
        for (int i = 0; i < m.length; i++)
        {
            for (int j = 0; j < m[0].length; j++)
            {
                m[i][j] = Sigmoid(m[i][j]);
            }
        }
        return m;
    }

    private double TanH(double x)
    {
        return 2 * Sigmoid(2*x) - 1;
    }

    private double[][] TanH(double[][] m)
    {
        for (int i = 0; i < m.length; i++)
        {
            for (int j = 0; j < m[0].length; j++)
            {
                m[i][j] = TanH(m[i][j]);
            }
        }
        return m;
    }

    public static float randInt(float min, float max, Random rand)
    {
        float randomNum = min + rand.nextFloat() * (max - min);
        System.out.println(randomNum);
        return randomNum;
    }
    ///////////////////////////////////////////////////////////////////
}
