/**
 * Copyright 2019 jilan1990
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package polaris.fuck.util;

import java.util.ArrayList;
import java.util.List;

public final class CompareUtil {

    private final static int INVALID_NO = -1;

    private CompareUtil() {

    }

    public static List<int[]> compare(List<String> left, List<String> right) {
        List<int[]> result = new ArrayList<int[]>();

        int leftO = 0;
        int rightO = 0;

        do {
            int[] nextOs = getNextOs(left, leftO, right, rightO);
            if (nextOs[0] == INVALID_NO) {
                break;
            }
            int count = takeLines(left, right, nextOs, result);
            leftO = nextOs[0] + count;
            rightO = nextOs[1] + count;
        } while (leftO < left.size() || rightO < right.size());

        return result;
    }

    private static int takeLines(List<String> left, List<String> right, int[] os, List<int[]> pairs) {

        int count = getCount(left, os[0], right, os[1]);
        for (int i = 0; i < count; i++) {
            int[] pair = { os[0] + i, os[1] + i };
            pairs.add(pair);
        }

        return count;
    }

    private static int[] getNextOs(List<String> left, int leftO, List<String> right, int rightO) {
        int[] result = { INVALID_NO, INVALID_NO };

        int step = 0;
        outer: while ((leftO + step) < left.size() || (rightO + step) < right.size()) {
            int pointNum = (step + 2) / 2;
            for (int i = 0; i < pointNum; i++) {
                int leftX = i;
                int leftY = step - i;
                int leftCount = getCount(left, leftO + leftX, right, rightO + leftY);

                int rightX = step - i;
                int rightY = i;
                int rightCount = getCount(left, leftO + rightX, right, rightO + rightY);

                if (leftCount > 0 || rightCount > 0) {
                    if (leftCount >= rightCount) {
                        result[0] = leftO + leftX;
                        result[1] = rightO + leftY;
                        break outer;
                    } else {
                        result[0] = leftO + rightX;
                        result[1] = rightO + rightY;
                        break outer;
                    }
                }
            }
            step++;
        }

        return result;
    }

    private static int getCount(List<String> left, int leftO, List<String> right, int rightO) {
        int count = 0;
        while (((leftO + count) < left.size()) && ((rightO + count) < right.size())) {
            if (!left.get(leftO + count).equals(right.get(rightO + count))) {
                break;
            }
            count++;
        }
        return count;
    }
}
