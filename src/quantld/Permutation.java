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
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.concurrent.Task;

/**
 *
 * @author Felix Yanhui Fan  felixfanyh@gmail.com
 */
public class Permutation{
    Random r;
    
    Permutation(){
        r = new Random();
    }
    
    Permutation(long seed){
        r = new Random(seed);
    }
    
    /**
     * shuffle index
     * @param n number of permutation
     * @param ss1 sample size of population 1
     * @param ss2 sample size of population 2
     * @return shuffled index
     */
    private List<List<Integer>> shuffIndex(int n, int ss1, int ss2){
        List<List<Integer>> idx = new ArrayList<>();
        int ss = ss1 + ss2;

        List<Integer> org = new ArrayList<>();
        for (int i = 0; i < ss; i++){
           org.add(i); 
        }
        
        for (int i = 0; i < n; i++) {
            ArrayList<Integer> perm = new ArrayList<>(org);
            Collections.shuffle(perm, r);
            idx.add(perm);
        }
        return idx;
    }
    
    /**
     * run quantLD with permutation
     * @param fileName1 file in PLINK tped format
     * @param fileName2 file in PLINK tped format
     * @param method method to measure matrix distance
     * @param winSize size of each window
     * @param ldMeasure LD measures, r2, dp, sr2
     * @param tol controls convergence. Algorithm stops when sum of absolute differences between new and old haplotype frequencies is less than tol.
     * @param maxItr maximum iterate
     * @param perm times of permutation
     * @param intThread threads used in permutation
     * @return array of matrix distance and permutation p-values
     * @throws IOException 
     * @throws java.lang.InterruptedException 
     * @throws java.util.concurrent.ExecutionException 
     */
    public double[][] permQuantLD(String fileName1, String fileName2, String method, int winSize, String ldMeasure, double tol, int maxItr, int perm, int intThread) throws IOException, InterruptedException, ExecutionException{
        // org step
        ReadTxtFile rtf = new ReadTxtFile();
        CalLD cld = new CalLD(tol, maxItr);
        BatchLD bld = new BatchLD();
        
        int[][][] gc = rtf.recodeGenotype(fileName1, fileName2);
        
        double[][][] r2a = cld.CalRDprime(gc[0], winSize, ldMeasure);
        double[][][] r2b = cld.CalRDprime(gc[1], winSize, ldMeasure);       
        double[] orgLDdiff = bld.calLDdiff(r2a, r2b, method);

        // permutation step
        int ns1 = gc[0].length;    // number of SNP in pop 1
        int ns2 = gc[1].length;    // number of SNP in pop 2
        int ss1 = gc[0][0].length; // number of individual in pop 1
        int ss2 = gc[1][0].length; // number of individual in pop 2
        if(ns1 != ns2){
            System.out.println("the number of SNPs is not equal in two populations!");
            System.exit(5);
        }
        
        List<List<Integer>> idx = shuffIndex(perm, ss1, ss2);
        
        double[] pmp = new double[orgLDdiff.length];
        Arrays.fill(pmp, 1);
      
        ExecutorService exec = Executors.newFixedThreadPool(intThread);
        final CompletionService<String> serv = new ExecutorCompletionService<>(exec);
        int runningThread = 0;
        for (int s = 0; s < idx.size(); s++) {
            serv.submit(new PermRun(s,gc,idx.get(s),orgLDdiff,cld,method,ldMeasure,pmp,winSize));
            runningThread++;
        }

        for (int s = 0; s < runningThread; s++) {
            Future<String> task = serv.take();
            String infor = task.get();
            //System.out.println(infor);
        }
        exec.shutdown();                 
        
        // cal p
        double[][] ans = new double[pmp.length][3];              
        for(int i=0; i< pmp.length; i++){
            ans[i][0] = orgLDdiff[i];
            ans[i][1] = pmp[i] / (perm + 1);
            ans[i][2] = (pmp[i] - 1) / perm;
        }

        return ans;
    }
    
    /**
     * run quantLD with permutation
     * @param fileName1 file in PLINK tped format
     * @param fileName2 file in PLINK tped format
     * @param method method to measure matrix distance
     * @param winSize size of each window
     * @param ldMeasure LD measures, r2, dp, sr2
     * @param start row to start read
     * @param end row to end read
     * @param tol controls convergence. Algorithm stops when sum of absolute differences between new and old haplotype frequencies is less than tol.
     * @param maxItr maximum iterate
     * @param perm times of permutation
     * @param intThread threads used in permutation
     * @return array of matrix distance and permutation p-values
     * @throws IOException 
     * @throws java.lang.InterruptedException 
     * @throws java.util.concurrent.ExecutionException 
     */
    public double[][] permQuantLD(String fileName1, String fileName2, String method, int winSize, String ldMeasure, int start, int end, double tol, int maxItr, int perm, int intThread) throws IOException, InterruptedException, ExecutionException{
        // org step
        ReadTxtFile rtf = new ReadTxtFile();
        CalLD cld = new CalLD(tol, maxItr);
        BatchLD bld = new BatchLD();
        
        int[][][] gc = rtf.recodeGenotype(fileName1,fileName2, start, end);//Time-consuming. 
        
        double[][][] r2a = cld.CalRDprime(gc[0], winSize, ldMeasure);
        double[][][] r2b = cld.CalRDprime(gc[1], winSize, ldMeasure);
        double[] orgLDdiff = bld.calLDdiff(r2a, r2b, method);

        // permutation step
        int ns1 = gc[0].length;    // number of SNP in pop 1
        int ns2 = gc[1].length;    // number of SNP in pop 2
        int ss1 = gc[0][0].length; // number of individual in pop 1
        int ss2 = gc[1][0].length; // number of individual in pop 2
        if(ns1 != ns2){
            System.out.println("the number of SNPs is not equal in two populations!");
            System.exit(5);
        }
        
        List<List<Integer>> idx = shuffIndex(perm, ss1, ss2);
        
        double[] pmp = new double[orgLDdiff.length];
        Arrays.fill(pmp, 1);

        ExecutorService exec = Executors.newFixedThreadPool(intThread);
        final CompletionService<String> serv = new ExecutorCompletionService<>(exec);
        int runningThread = 0;
        for (int s = 0; s < idx.size(); s++) {
            serv.submit(new PermRun(s,gc,idx.get(s),orgLDdiff,cld,method,ldMeasure,pmp,winSize));
            runningThread++;
        }

        for (int s = 0; s < runningThread; s++) {
            Future<String> task = serv.take();
            String infor = task.get();
            //System.out.println(infor);
        }
        exec.shutdown();                
        
        // cal p
        double[][] ans = new double[pmp.length][3];
        for(int i=0; i< pmp.length; i++){
            ans[i][0] = orgLDdiff[i];
            ans[i][1] = pmp[i] / (perm + 1);
            ans[i][2] = (pmp[i] - 1) / perm;
        }

        return ans;
    }

    public class PermRun extends Task implements Callable<String>{
        int intID;
        int winSize;
        String ldMeasure;
        double[] orgLDdiff;
        CalLD cld;
        String method;
        int[][] gcap;
        int[][] gcbp;
        double[] pmp;
        
        public PermRun(int intID, int[][][] gc, List<Integer> idx1, double[] orgLDdiff, CalLD cld, String method, String ldMeasure, double[] pmp, int winSize) {
            this.intID=intID;
            this.orgLDdiff=orgLDdiff;
            this.cld=cld;
            this.method=method;
            this.ldMeasure=ldMeasure;
            this.pmp=pmp;
            this.winSize=winSize;
            
            int ns1 = gc[0].length;    // number of SNP in pop 1
            int ns2 = gc[1].length;    // number of SNP in pop 2
            int ss1 = gc[0][0].length; // number of individual in pop 1
            int ss2 = gc[1][0].length; // number of individual in pop 2
            if(ns1 != ns2){
                System.out.println("the number of SNPs is not equal in two populations!");
                System.exit(5);
            }
       
            gcap = new int[ns1][ss1];
            gcbp = new int[ns2][ss2];
            
            // pop1
            for(int i=0; i<ns1;i++){// total number of SNP
                for(int j =0; j<ss1; j++){ // total number of individual
                    int tidx = idx1.get(j);
                    if( tidx < ss1){
                        gcap[i][j] = gc[0][i][tidx];
                    }else{
                        gcap[i][j] = gc[1][i][tidx - ss1];
                    }
                }
            }
            
            // pop2
            for(int i=0; i<ns2;i++){// total number of SNP
                for(int j =0; j<ss2; j++){ // total number of individual
                    int tidx = idx1.get(j+ss1);
                    if( tidx < ss1){
                        gcbp[i][j] = gc[0][i][tidx];
                    }else{
                        gcbp[i][j] = gc[1][i][tidx - ss1];
                    }
                }
            }            
            
        }
         
        @Override
        public String call() throws Exception {
            double[][][] r2ap = cld.CalRDprime(gcap, winSize, ldMeasure);
            double[][][] r2bp = cld.CalRDprime(gcbp, winSize, ldMeasure);
            BatchLD bld = new BatchLD();
            double[] pmLDdiff = bld.calLDdiff(r2ap, r2bp, method);
            
            synchronized(this){
                for(int i=0; i<pmLDdiff.length; i++){
                    if(pmLDdiff[i] >= orgLDdiff[i]){
                        pmp[i] += 1;
                    }
                }
            }
            return "Permutation "+intID+" run successfully!";
        }
        
    }
    
}
