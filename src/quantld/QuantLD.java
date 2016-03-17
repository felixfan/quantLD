/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quantld;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.commons.cli.ParseException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author Felix Yanhui Fan felixfanyh@gmail.com
 */

class globalSetting{
    public static final String packageVersion = "v2.0";
    public static final String lastReviseDate = "17 Mar 2016";
}

public class QuantLD {   
    /**
     * @param args the command line arguments
     * @throws org.apache.commons.cli.ParseException can not parse opts
     * @throws java.io.IOException can not open input file
     * @throws java.lang.InterruptedException
     * @throws java.util.concurrent.ExecutionException
     */
    public static void main(String[] args) throws ParseException, IOException, InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        JOptsParse jop = new JOptsParse();
        HashMap<String, String> params = jop.getOpt(args);
        
        String fileName1 = params.get("f");
        String fileName2 = params.get("s");
        
        // default
        String ldMeasure = "r2";
        double tol = 0.001;
        int maxItr = 1000;
        int winSize = 50;
        String tDist = "evd";
        String output = "output.txt";
        int nrow = 1000;
        int perm = 0;
        boolean seedb = false;
        long seedl = 0;
        int intThread=1;
        
        if(params.containsKey("tol")){
            tol = Double.parseDouble(params.get("tol"));
        }
        if(params.containsKey("iter")){
            maxItr = Integer.parseInt(params.get("iter"));
        }
        if(params.containsKey("m")){
            ldMeasure = params.get("m");
        }
        if(params.containsKey("w")){
            winSize = Integer.parseInt(params.get("w"));
        }
        if(params.containsKey("d")){
            tDist = params.get("d");
        }
        if(params.containsKey("o")){
            output = params.get("o");
        }
        if(params.containsKey("nr")){
            nrow = Integer.parseInt(params.get("nr"));
        }
        if(params.containsKey("p")){
            perm = Integer.parseInt(params.get("p"));
        }
        if(params.containsKey("seed")){
            seedl = Long.parseLong(params.get("seed"));
            seedb = true;
        }
        if(params.containsKey("nt")){
            intThread=Integer.parseInt(params.get("nt"));
        }
        
        // check parameters
        String[] ldMeasures = new String[]{"r2","dp","sr2"};
        if(!Arrays.asList(ldMeasures).contains(ldMeasure)){
            System.out.println("--ld-measure only support r2, dp or sr2");
            System.exit(1);
        }
        String[] tDists = new String[]{"tst","tpc","evd","cad","mad","eud","chd","bcd"};
        if(!Arrays.asList(tDists).contains(tDist)){
            System.out.println("--test only support tst, tpc, evd, cad, mad, eud, chd, bcd");
            System.exit(2);
        }
        if(tol > 1){
            System.out.println("Warning: high convergence tolerance will lead to low accuracy");
        }else if(tol < 0.0001){
            System.out.println("Warning: low convergence tolerance will take more time");
        }
        if(maxItr > 10000){
            System.out.println("Warning: more EM steps will take more time");
        }else if(maxItr < 10){
            System.out.println("Warning: less EM steps will lead to low accuracy");
        }
        if(perm < 0){
            System.out.println("Erro: number of permutation can not be negative");
            System.exit(1);
        }
        
        // printout program
        System.out.println( "@-------------------------------------------------------------@");
        System.out.print( "|       quantLD       |      " +  globalSetting.packageVersion);
        System.out.println( "      |     " + globalSetting.lastReviseDate  + "      |");
        System.out.println( "|-------------------------------------------------------------|");
        System.out.println( "|  (C) 2016 Felix Yanhui Fan, GNU General Public License, v2  |");
        System.out.println( "|-------------------------------------------------------------|");
        System.out.println( "|    For documentation, citation & bug-report instructions:   |");
        System.out.println( "|          http://felixfan.github.io/quantLD                  |");
        System.out.println( "@-------------------------------------------------------------@");
        System.out.println();
        System.out.println( "Options in effect:");
        
        // printout parameters
        System.out.println();
        System.out.println("\t" + "--first-tped " + fileName1);
        System.out.println("\t" + "--second-tped " + fileName2);
        System.out.println("\t" + "--win-size " + winSize);
        System.out.println("\t" + "--ld-measure " + ldMeasure);
        System.out.println("\t" + "--ld-diff-measure " + tDist);
        System.out.println("\t" + "--max-iteration " + maxItr);
        System.out.println("\t" + "--conv-tolerance " + tol);
        System.out.println("\t" + "--num-rows " + nrow);
        if(perm > 0){
            System.out.println("\t" + "--perm " + perm);
            if(seedb){
                System.out.println("\t" + "--rand-seed " + seedl);
            }
            if(intThread > 1){
                System.out.println("\t" + "--num-threads " + intThread);
            }
        }
        System.out.println("\t" + "--out " + output);
        
        System.out.println();
        System.out.println("first input genotype file: " + fileName1);
        System.out.println("second input genotype file: " + fileName2);
        System.out.println("window size: " + winSize);
        System.out.println("LD measure: " + ldMeasure);
        System.out.println("Measure of Distance: " + tDist);
        System.out.println("maximum number of EM steps: " + maxItr);
        System.out.println("convergence tolerance: " + tol);
        System.out.println("number of rows: " + nrow);
        if(perm > 0){
            System.out.println("number of permutation: " + perm);
            if(seedb){
                System.out.println("random seed: " + seedl);
            }
            if(intThread > 1){
                System.out.println("number of threads: " + intThread);
            }
        }
        System.out.println("output: " + output);
        System.out.println();
        
        WriteTxtFile wtf;
        if(perm > 0 && seedb){
            wtf = new WriteTxtFile(seedl);
        }else{
            wtf = new WriteTxtFile();
        }
        
        if(perm == 0){
            wtf.runQuantLD(output, fileName1, fileName2, tDist, winSize, ldMeasure, tol, maxItr,nrow);
        }else{
            wtf.runQuantLDPerm(output, fileName1, fileName2, tDist, winSize, ldMeasure, tol, maxItr, nrow, perm,intThread);
        }
        
        long endTime = System.currentTimeMillis();
        long diffTime = endTime - startTime;
        System.out.print("Time used: ");
        wtf.miSecToHourMinSec(diffTime);
        Date cdate = new Date();
        System.out.println("Analysis finished: " + cdate);
    } 
}
