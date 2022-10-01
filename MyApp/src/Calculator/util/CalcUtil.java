package Calculator.util;

import Calculator.dto.ImpressionInput;
import Calculator.dto.SampleInput;

public class CalcUtil {

    public static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    public static int findGcd(int[] values, int startValue) {
        int val = startValue;
        for (int i = 0; i < values.length; i++) {
            val = gcd(val, values[i]);
        }
        return val;
    }

    public static void divideAllWithGcd(int[] values, int gcd) {
        if (gcd == 1) {
            return;
        }
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i] / gcd;
        }
    }


    public static void printSampleInput(SampleInput sampleInput) {
        System.out.println("Impressions available: " + sampleInput.TotalImpressions);

        for (ImpressionInput customer : sampleInput.ImpressionInput) {
            System.out.println("Name: " + customer.Name + " Impressions per campaign: " + customer.ImpressionPerCampaign + " Revenue per campaign: " + customer.RevenuePerCampaign);
        }
    }
}
