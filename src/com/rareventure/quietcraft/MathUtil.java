package com.rareventure.quietcraft;

import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Created by tim on 9/12/16.
 */
public class MathUtil {
    private static final double _2PI = 2 * Math.PI;

    /**
     * The normal distribution with a min and max cut off as an int.
     */
    public static int normalRandomInt(RandomNormalParams params) {
        double val = normalRandom(params);
        int iv = (int)(Math.round(val));
        if(iv < params.min)
            iv = (int)Math.ceil(params.min);
        else if(iv > params.max)
            iv = (int)Math.floor(params.max);
        return iv;
    }

    public static Random random = new Random();

    /**
     * The normal (bell curve) distribution with a min and max.
     */
    public static double normalRandom(RandomNormalParams params) {
        synchronized (random)
        {
            double v = random.nextGaussian() * params.std + params.mean;

            return Math.max(Math.min(v,params.max),params.min);
        }
    }

    /**
     * Returns the yaw of a vector in degrees where:
     * <ul>
     * <li>+z points forward, 0 degrees
     * <li>-z points backwards, 180 degrees
     * <li>+x points left, -90 degrees
     * <li>-x points right, +90 degrees
     * </ul>
     * <p>
     * This matches minecraft coordinate system</p>
     */
    static double getYawOfVector(Vector vector) {
        final double x = vector.getX();
        final double z = vector.getZ();

        if (x == 0 && z == 0) {
            return 0;
        }

        double theta = Math.atan2(-x, z);
        return Math.toDegrees((theta + _2PI) % _2PI);
    }

    /**
     * Returns the pitch of vector in degrees. As with minecraft pitch,
     * down is positive and negative is up.
     */
    static double getPitchOfVector(Vector vector) {
        double x = vector.getX();
        double z = vector.getZ();

        double x2 = x*x;
        double z2 = z*z;
        double xz = Math.sqrt(x2 + z2);
        return (double) Math.toDegrees(Math.atan(-vector.getY() / xz));
    }

    public static double sqr(double v) {
        return v*v;
    }

    public static class RandomNormalParams {
        public double mean,std,min,max;

        public RandomNormalParams(double mean, double std, double min, double max) {
            this.mean = mean;
            this.std = std;
            this.min = min;
            this.max = max;
        }
    }

    public static void main(String []argv)
    {
        RandomNormalParams r = new RandomNormalParams(10,3,7,15);
        for(int i = 0; i < 20; i++)
        {
            for(int j = 0; j < 5; j++)
            {
                System.out.print(String.format("%8.3f ",normalRandom(r)));
            }
            System.out.println();
        }
        System.out.println();
        for(int i = 0; i < 20; i++)
        {
            for(int j = 0; j < 5; j++)
            {
                System.out.print(String.format("%8d ",normalRandomInt(r)));
            }
            System.out.println();
        }
    }
}
