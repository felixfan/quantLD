package quantld;

import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Felix Yanhui Fan  felixfanyh@gmail.com
 */
public class BatchLD extends JMatrix {
    /**
     * 
     * @param fileName1
     * @param fileName2
     * @param method
     * @param winSize
     * @param ldMeasure
     * @param tol
     * @param maxItr
     * @return
     * @throws IOException 
     */
    public double[] batchQuantLD(String fileName1, String fileName2, String method, int winSize, String ldMeasure, double tol, int maxItr) throws IOException{
        ReadTxtFile rtf = new ReadTxtFile();
        int[][] gca = rtf.recodeGenotype(fileName1);
        int[][] gcb = rtf.recodeGenotype(fileName2);
        
        double[][][] r2a = CalRDprime(gca, winSize, ldMeasure);
        double[][][] r2b = CalRDprime(gcb, winSize, ldMeasure);
        
        int n = r2a.length;
        double[] ldDiff = new double[n];
        for(int i=0; i<n;i++){
            switch (method) {
                case "tst":
                    ldDiff[i] = calTstatistic(r2a[i], r2b[i]);
                    break;
                case "tpc":
                    ldDiff[i] = calTpercent(r2a[i], r2b[i]);
                    break;
                case "evd":
                    ldDiff[i] = calTraceDiffEigValue(r2a[i], r2b[i]);
                    break;
                case "cad":
                    ldDiff[i] = canberraDistance(r2a[i], r2b[i]);
                    break;
                case "mad":
                    ldDiff[i] = manhattanDistance(r2a[i], r2b[i]);
                    break;                    
                case "eud":
                    ldDiff[i] = euclideanDistance(r2a[i], r2b[i]);
                    break;
                case "chd":
                    ldDiff[i] = chebyshevDistance(r2a[i], r2b[i]);
                    break;
                case "bcd":
                    ldDiff[i] = brayCurtisDistance(r2a[i], r2b[i]);
                    break;
                default:
                    break;
            }
            
        }
        return ldDiff;
    }
    
    /**
     * 
     * @param fileName1
     * @param fileName2
     * @param method
     * @param winSize
     * @param ldMeasure
     * @param start
     * @param end
     * @param tol
     * @param maxItr
     * @return
     * @throws IOException 
     */
    public double[] batchQuantLD(String fileName1, String fileName2, String method, int winSize, String ldMeasure, int start, int end, double tol, int maxItr) throws IOException{
        ReadTxtFile rtf = new ReadTxtFile();
        int[][] gca = rtf.recodeGenotype(fileName1, start, end);
        int[][] gcb = rtf.recodeGenotype(fileName2, start, end);
        
        double[][][] r2a = CalRDprime(gca, winSize, ldMeasure);
        double[][][] r2b = CalRDprime(gcb, winSize, ldMeasure);
        
        int n = r2a.length;
        double[] ldDiff = new double[n];
        for(int i=0; i<n;i++){
            switch (method) {
                case "tst":
                    ldDiff[i] = calTstatistic(r2a[i], r2b[i]);
                    break;
                case "tpc":
                    ldDiff[i] = calTpercent(r2a[i], r2b[i]);
                    break;
                case "evd":
                    ldDiff[i] = calTraceDiffEigValue(r2a[i], r2b[i]);
                    break;
                case "cad":
                    ldDiff[i] = canberraDistance(r2a[i], r2b[i]);
                    break;
                case "mad":
                    ldDiff[i] = manhattanDistance(r2a[i], r2b[i]);
                    break;                    
                case "eud":
                    ldDiff[i] = euclideanDistance(r2a[i], r2b[i]);
                    break;
                case "chd":
                    ldDiff[i] = chebyshevDistance(r2a[i], r2b[i]);
                    break;
                case "bcd":
                    ldDiff[i] = brayCurtisDistance(r2a[i], r2b[i]);
                    break;
                default:
                    break;
            }
            
        }
        return ldDiff;
    }
}
