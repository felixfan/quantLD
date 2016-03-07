/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package quantld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Felix Yanhui Fan  felixfanyh@gmail.com
 */
public class Permutation{
    private final long seed;
    boolean useSeed;
    
    Permutation(){
        this.useSeed = false;
        this.seed = 0; // meaningless
    }
    
    Permutation(long seed){
        this.useSeed = true;
        this.seed = seed;
    }
    
    /**
     * shuffle index
     * @param n number of permutation
     * @param ss1 sample size of population 1
     * @param ss2 sample size of population 2
     * @return shuffled index
     */
    public List<List<Integer>> shuffIndex(int n, int ss1, int ss2){
        List<List<Integer>> idx = new ArrayList<>();
        int ss = ss1 + ss2;

        List<Integer> org = new ArrayList<>();
        for (int i = 0; i < ss; i++){
           org.add(i); 
        }
        
        Random r;
        if(this.useSeed == false){
            r = new Random();
        }else{
            r = new Random(this.seed);
        }
        
        for (int i = 0; i < n; i++) {
            ArrayList<Integer> perm = new ArrayList<>(org);
            Collections.shuffle(perm, r);
            idx.add(perm);
        }
        return idx;
    }
    
    
    public double[][] permQuantLD(String fileName1, String fileName2, String method, int winSize, String ldMeasure, double tol, int maxItr, int perm) throws IOException{
        // org step
        ReadTxtFile rtf = new ReadTxtFile();
        CalLD cld = new CalLD(tol, maxItr);
        BatchLD bld = new BatchLD();
        int[][] gca = rtf.recodeGenotype(fileName1);
        int[][] gcb = rtf.recodeGenotype(fileName2);
        
        double[][][] r2a = cld.CalRDprime(gca, winSize, ldMeasure);
        double[][][] r2b = cld.CalRDprime(gcb, winSize, ldMeasure);
        double[] orgLDdiff = bld.calLDdiff(r2a, r2b, method, ldMeasure);

        // permutation step
        int ns1 = gca.length;    // number of SNP in pop 1
        int ns2 = gcb.length;    // number of SNP in pop 2
        int ss1 = gca[0].length; // number of individual in pop 1
        int ss2 = gcb[0].length; // number of individual in pop 2
        if(ns1 != ns2){
            System.out.println("the number of SNPs is not equal in two populations!");
            System.exit(5);
        }
        
        List<List<Integer>> idx = shuffIndex(perm, ss1, ss2);
        
        double[] pmp = new double[orgLDdiff.length];
        Arrays.fill(pmp, 1);
        
        for(List<Integer> idx1 : idx){ // total number of permutation   
            int[][] gcap = new int[ns1][ss1];
            int[][] gcbp = new int[ns2][ss2];
            // pop1
            for(int i=0; i<ns1;i++){// total number of SNP
                for(int j =0; j<ss1; j++){ // total number of individual
                    int tidx = idx1.get(j);
                    if( tidx < ss1){
                        gcap[i][j] = gca[i][tidx];
                    }else{
                        gcap[i][j] = gcb[i][tidx - ss1];
                    }
                }
            }
            // pop2
            for(int i=0; i<ns2;i++){// total number of SNP
                for(int j =0; j<ss2; j++){ // total number of individual
                    int tidx = idx1.get(j+ss1);
                    if( tidx < ss1){
                        gcbp[i][j] = gca[i][tidx];
                    }else{
                        gcbp[i][j] = gcb[i][tidx - ss1];
                    }
                }
            }
            // cal static
            double[][][] r2ap = cld.CalRDprime(gcap, winSize, ldMeasure);
            double[][][] r2bp = cld.CalRDprime(gcbp, winSize, ldMeasure);
            double[] pmLDdiff = bld.calLDdiff(r2ap, r2bp, method, ldMeasure);
            for(int i=0; i<pmLDdiff.length; i++){
                if(pmLDdiff[i] >= orgLDdiff[i]){
                    pmp[i] += 1;
                }
            }
        }
        
        double[][] ans = new double[pmp.length][3];
        
        // cal p
        for(int i=0; i< pmp.length; i++){
            ans[i][0] = orgLDdiff[i];
            ans[i][1] = pmp[i] / (perm + 1);
            ans[i][2] = (pmp[i] - 1) / perm;
        }

        return ans;
    }
}