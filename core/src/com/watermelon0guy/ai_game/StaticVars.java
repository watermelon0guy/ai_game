package com.watermelon0guy.ai_game;

public final class StaticVars {
    public static short STATIC_FLOOR = (short)2;
    public static short JOINTS = (short)4;
    public static short BONES = (short)8;

    //region muscle settings
    public static float damping = 3;
    public static float frequency = 7;
    public static float maxForce = 2000;
    //endregion

    //region настройки сустава
    public static float radius = 0.1f;
    //endregion

    //region настройки кости
    public static float densityBone = 500;
    public static float frictionBone = 1;
    //endregion

    //region настройки нейросети
    public static float minWeight = -0.99f;
    public static float maxWeight = 0.99f;
    //endregion

    public static float geneticTimeStep = 0.05f;
}
