package Test;

import Calculator.engine.Calculator;
import Calculator.util.CalcUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

    @Test
    public void SampleInputOne_Test() {
        int[] impressions = new int[]{2_000_000, 3_500_000, 2_300_000, 8_000_000, 10_000_000, 1_500_000, 1_000_000};
        int[] revenues = new int[]{200, 400, 210, 730, 1_000, 160, 100};
        int totalImpressions = 32_356_000;
        int n = revenues.length;
        int gcd = CalcUtil.findGcd(impressions, totalImpressions);

        Assertions.assertEquals(4000, gcd);

        //Decreases space usage by dividing values of impressions array
        CalcUtil.divideAllWithGcd(impressions, gcd);
        totalImpressions = totalImpressions / gcd;

        int[][] table = Calculator.getMaxProfitTable(impressions, revenues, totalImpressions);
        Assertions.assertNotNull(table);
        Assertions.assertEquals(3_620, getMaxProfit_recursive(totalImpressions, impressions, revenues, n));
        Assertions.assertEquals(3_620, table[revenues.length - 1][totalImpressions]);
    }

    @Test
    public void SampleInputTwo_Test() {
        int[] impressions = new int[]{1, 2, 3, 70_000, 49_000_000};
        int[] revenues = new int[]{0, 2, 2, 71_000, 50_000_000};
        int totalImpressions = 50_000_000;
        int n = revenues.length;
        int gcd = CalcUtil.findGcd(impressions, totalImpressions);

        Assertions.assertEquals(1, gcd);

        //Decreases space usage by dividing values of impressions array
        CalcUtil.divideAllWithGcd(impressions, gcd);
        totalImpressions = totalImpressions / gcd;

        int[][] table = Calculator.getMaxProfitTable(impressions, revenues, totalImpressions);
        Assertions.assertNotNull(table);
        //Assertions.assertEquals(51_014_000, getMaxProfit_recursive(totalImpressions, impressions, revenues, n));
        Assertions.assertEquals(51_014_000, table[revenues.length - 1][totalImpressions]);
    }

    @Test
    public void SampleInputThree_Test() {
        int[] impressions = new int[]{1_000_000, 2_000_000, 3_000_000};
        int[] revenues = new int[]{5_000, 9_000, 20_000};
        int totalImpressions = 2_000_000_000;
        int n = revenues.length;
        int gcd = CalcUtil.findGcd(impressions, totalImpressions);

        Assertions.assertEquals(1_000_000, gcd);

        //Decreases space usage by dividing values of impressions array
        CalcUtil.divideAllWithGcd(impressions, gcd);
        totalImpressions = totalImpressions / gcd;

        int[][] table = Calculator.getMaxProfitTable(impressions, revenues, totalImpressions);

        Assertions.assertNotNull(table);
        Assertions.assertEquals(13_330_000, getMaxProfit_recursive(totalImpressions, impressions, revenues, n));
        Assertions.assertEquals(13_330_000, table[revenues.length - 1][totalImpressions]);
    }

    // Time Complexity: O(2 ^ N)
    // Space Complexity: O(1)
    // Returns the maximum revenues for capacity maxImpression
    static int getMaxProfit_recursive(int maxImpression, int[] impressions, int[] revenues, int amountOfIntros) {
        if (amountOfIntros == 0 || maxImpression == 0)
            return 0;

        if (impressions[amountOfIntros - 1] > maxImpression) {
            return getMaxProfit_recursive(maxImpression, impressions, revenues, amountOfIntros - 1);

        } else {
            int maxVal = revenues[amountOfIntros - 1] + getMaxProfit_recursive(maxImpression - impressions[amountOfIntros - 1], impressions, revenues, amountOfIntros);
            int maxVal1 = getMaxProfit_recursive(maxImpression, impressions, revenues, amountOfIntros - 1);

            return Math.max(maxVal, maxVal1);
        }
    }
}