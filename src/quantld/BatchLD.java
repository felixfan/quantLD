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
public class BatchLD{
    
    public double[] calLDdiff(double[][][] r2a, double[][][] r2b, String method, String ldMeasure){
        JMatrix jmx = new JMatrix();
        int n = r2a.length;
        double[] ldDiff = new double[n];
        for(int i=0; i<n;i++){
            switch (method) {
                case "tst":
                    ldDiff[i] = jmx.calTstatistic(r2a[i], r2b[i]);
                    break;
                case "tpc":
                    ldDiff[i] = jmx.calTpercent(r2a[i], r2b[i]);
                    break;
                case "evd":
                    ldDiff[i] = jmx.calTraceDiffEigValue(r2a[i], r2b[i]);
                    break;
                case "cad":
                    ldDiff[i] = jmx.canberraDistance(r2a[i], r2b[i]);
                    break;
                case "mad":
                    ldDiff[i] = jmx.manhattanDistance(r2a[i], r2b[i]);
                    break;                    
                case "eud":
                    ldDiff[i] = jmx.euclideanDistance(r2a[i], r2b[i]);
                    break;
                case "chd":
                    ldDiff[i] = jmx.chebyshevDistance(r2a[i], r2b[i]);
                    break;
                case "bcd":
                    ldDiff[i] = jmx.brayCurtisDistance(r2a[i], r2b[i]);
                    break;
                default:
                    break;
            }
        }
        return ldDiff;
    }
    
    /**
     * calculate matrix distance
     * @param fileName1 file in PLINK tped format
     * @param fileName2 file in PLINK tped format
     * @param method method to measure matrix distance
     * @param winSize size of each window
     * @param ldMeasure LD measures, r2, dp, sr2
     * @param tol controls convergence. Algorithm stops when sum of absolute differences between new and old haplotype frequencies is less than tol.
     * @param maxItr maximum iterate
     * @return array of matrix distance
     * @throws IOException can not open input file
     */
    public double[] batchQuantLD(String fileName1, String fileName2, String method, int winSize, String ldMeasure, double tol, int maxItr) throws IOException{
        ReadTxtFile rtf = new ReadTxtFile();
        CalLD cld = new CalLD(tol, maxItr);
        int[][] gca = rtf.recodeGenotype(fileName1);
        int[][] gcb = rtf.recodeGenotype(fileName2);
        
        double[][][] r2a = cld.CalRDprime(gca, winSize, ldMeasure);
        double[][][] r2b = cld.CalRDprime(gcb, winSize, ldMeasure);
        
        double[] ldDiff = calLDdiff(r2a, r2b, method, ldMeasure);
        return ldDiff;
    }
    
    /**
     * calculate matrix distance
     * @param fileName1 file in PLINK tped format
     * @param fileName2 file in PLINK tped format
     * @param method method to measure matrix distance
     * @param winSize size of each window
     * @param ldMeasure LD measures, r2, dp, sr2
     * @param tol controls convergence. Algorithm stops when sum of absolute differences between new and old haplotype frequencies is less than tol.
     * @param maxItr maximum iterate
     * @param start row to start read
     * @param end row to end read
     * @return array of matrix distance
     * @throws IOException can not open input file
     */
    public double[] batchQuantLD(String fileName1, String fileName2, String method, int winSize, String ldMeasure, int start, int end, double tol, int maxItr) throws IOException{
        ReadTxtFile rtf = new ReadTxtFile();
        CalLD cld = new CalLD(tol, maxItr);
        int[][] gca = rtf.recodeGenotype(fileName1, start, end);
        int[][] gcb = rtf.recodeGenotype(fileName2, start, end);
        
        double[][][] r2a = cld.CalRDprime(gca, winSize, ldMeasure);
        double[][][] r2b = cld.CalRDprime(gcb, winSize, ldMeasure);
        
        double[] ldDiff = calLDdiff(r2a, r2b, method, ldMeasure);
        return ldDiff;
    }
}
