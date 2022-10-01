package Calculator.engine;

import Calculator.dto.ImpressionInput;
import Calculator.dto.ImpressionOutput;
import Calculator.dto.SampleInput;
import Calculator.dto.SampleOutput;
import Calculator.util.CalcUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter name of the file: ");
            String fileName = scanner.nextLine();

            if(fileName.isEmpty()){
                System.out.print("No input from user. Closing!");
                break;
            }

            SampleInput input;
            try {
                input = readSaleInputFromFile(fileName);
            } catch (Exception e) {
                System.out.print("Could not read from file!");
                e.printStackTrace();
                break;
            }

            SampleOutput output = calculate(input);
            if(output != null) {
                for (ImpressionOutput impressionOutput : output.ImpressionOutput) {
                    System.out.println(impressionOutput.Name
                            + ", " + impressionOutput.AmountOfCampaigns
                            + ", " + impressionOutput.TotalImpressions
                            + ", " + impressionOutput.TotalRevenue);
                }
                System.out.println(output.TotalImpressions + ", " + output.TotalRevenue);
            }
        }
    }

    private static SampleOutput calculate(SampleInput input){
        int[] impressions = new int[input.ImpressionInput.length];
        int[] revenues = new int[input.ImpressionInput.length];
        String[] customerNames = new String[input.ImpressionInput.length];

        for (int i = 0; i < input.ImpressionInput.length; i++) {
            customerNames[i] = input.ImpressionInput[i].Name;
            impressions[i] = input.ImpressionInput[i].ImpressionPerCampaign;
            revenues[i] = input.ImpressionInput[i].RevenuePerCampaign;
        }

        //Divides all impressions with GCD to save space
        int gcd = CalcUtil.findGcd(impressions, input.TotalImpressions);
        CalcUtil.divideAllWithGcd(impressions, gcd);
        int totalImpressions = input.TotalImpressions / gcd;

        int[][] table = getMaxProfitTable(impressions, revenues, totalImpressions);
        if(table != null){
            int maxRevenue = table[impressions.length - 1][totalImpressions];
            String[] saleList = getSampleCombination(table, impressions, revenues, customerNames, totalImpressions);

            return calculateSampleOutput(input, saleList, maxRevenue);
        }
        return null;
    }

    private static ImpressionInput getCustomerInfoInput(String customerName, ImpressionInput[] impressionInputs){
        for(ImpressionInput impressionInput : impressionInputs){
            if(impressionInput.Name != null && impressionInput.Name.equals(customerName)){
                return impressionInput;
            }
        }
        return null;
    }

    private static SampleOutput calculateSampleOutput(SampleInput input, String[] customerNames, int maxRevenue){
        SampleOutput output = new SampleOutput();

        output.ImpressionOutput = new ImpressionOutput[input.ImpressionInput.length];
        for(int i = 0; i<input.ImpressionInput.length; i++) {
            output.ImpressionOutput[i] = new ImpressionOutput();
            output.ImpressionOutput[i].Name = input.ImpressionInput[i].Name;
        }

        output.TotalImpressions = 0;
        for(String customerName : customerNames){
            ImpressionInput impressionInput = getCustomerInfoInput(customerName, input.ImpressionInput);

            if(impressionInput != null){
                updateOutputInformation(impressionInput, output.ImpressionOutput);
                output.TotalImpressions += impressionInput.ImpressionPerCampaign;
            }
        }
        output.TotalRevenue = maxRevenue;
        return output;
    }

    private static void updateOutputInformation(ImpressionInput impressionInput, ImpressionOutput[] outputs){
        for(ImpressionOutput impressionOutput : outputs){
            if(impressionOutput.Name != null && impressionOutput.Name.equals(impressionInput.Name)){
                impressionOutput.TotalImpressions += impressionInput.ImpressionPerCampaign;
                impressionOutput.TotalRevenue += impressionInput.RevenuePerCampaign;
                impressionOutput.AmountOfCampaigns += 1;
            }
        }
    }

    private static SampleInput readSaleInputFromFile(String fileName) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("resources\\" + fileName + ".txt"));
        String line = bufferedReader.readLine();

        SampleInput sampleInput = new SampleInput();
        sampleInput.TotalImpressions = Integer.parseInt(line);
        sampleInput.ImpressionInput = readImpressionsFromFile(bufferedReader);

        bufferedReader.close();
        return sampleInput;
    }

    private static ImpressionInput[] readImpressionsFromFile(BufferedReader bufferedReader) throws IOException {
        ArrayList<ImpressionInput> customers = new ArrayList<>();
        String line = bufferedReader.readLine();

        while (line != null) {
            String[] info = line.split(",");
            customers.add(new ImpressionInput(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2])));

            line = bufferedReader.readLine();
        }
        return customers.toArray(new ImpressionInput[customers.size()]);
    }

    //Time Complexity: O(amountOfIntros * totalImpressions)
    //Space Complexity: O(amountOfIntros * totalImpressions)
    public static int[][] getMaxProfitTable(int[] impressions, int[] revenues, int maxImpression) {
        if (impressions.length == 0 || revenues.length == 0 || maxImpression <= 0) {
            return null;
        }
        int amountOfIntros = revenues.length;

        int[][] table = new int[amountOfIntros][maxImpression + 1];
        for (int i = 0; i < amountOfIntros; i++) {
            for (int j = 1; j <= maxImpression; j++) {
                int includedCurrentRevenue = 0;
                int excludedCurrentRevenue = 0;

                if(impressions[i] <= j){
                    includedCurrentRevenue = revenues[i] + table[i][j - impressions[i]];
                }
                if(i > 0){
                    excludedCurrentRevenue = table[i - 1][j];
                }
                table[i][j] = Math.max(includedCurrentRevenue, excludedCurrentRevenue);
            }
        }
        return table;
    }


    public static String[] getSampleCombination(int[][] table, int[] impressions, int[] revenues, String[] names, int maxImpression){
        ArrayList<String> ret = new ArrayList<>();

        int row = impressions.length - 1;
        int column = maxImpression;

        int maxProfit = table[row][column];

        while (row - 1 >= 0 && table[row][column] >= 0 ){
            if (table[row][column] != table[row - 1][column]) {
                ret.add(names[row]);
                column = column - impressions[row];
                maxProfit = maxProfit - revenues[row];
            }else {
                row = row - 1;
            }
        }

        while (maxProfit > 0) {
            ret.add(names[0]);
            maxProfit = maxProfit - revenues[0];
        }

        return ret.toArray(new String[ret.size()]);
    }
}