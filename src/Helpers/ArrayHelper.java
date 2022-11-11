package Helpers;

public class ArrayHelper {
    /**
     * Clones the provided array
     *
     * @param src
     * @return a new clone of the provided array
     */
    public static int[][] cloneArray(int[][] src) {
        int length = src.length;
        int[][] target = new int[length][src[0].length];
        for (int i = 0; i < length; i++) {
            System.arraycopy(src[i], 0, target[i], 0, src[i].length);
        }
        return target;
    }
}
