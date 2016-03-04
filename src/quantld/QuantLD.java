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


/**
 *
 * @author Felix Yanhui Fan felixfanyh@gmail.com
 */

public class QuantLD {

    /**
     * @param args the command line arguments
     * @throws org.apache.commons.cli.ParseException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws ParseException, IOException {
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
        int nrow = 10000;
        
        if(params.containsKey("T")){
            tol = Double.parseDouble(params.get("T"));
        }
        if(params.containsKey("i")){
            maxItr = Integer.parseInt(params.get("i"));
        }
        if(params.containsKey("m")){
            ldMeasure = params.get("m");
        }
        if(params.containsKey("w")){
            winSize = Integer.parseInt(params.get("w"));
        }
        if(params.containsKey("t")){
            tDist = params.get("t");
        }
        if(params.containsKey("o")){
            output = params.get("o");
        }
        if(params.containsKey("n")){
            nrow = Integer.parseInt(params.get("n"));
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
        
        // printout program
        System.out.println( "@-------------------------------------------------------------@");
        System.out.println( "|       quantLD       |    v0.1.0304    |     4 Mar 2016      |");
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
        System.out.println("\t" + "--test " + tDist);
        System.out.println("\t" + "--max-iteration " + maxItr);
        System.out.println("\t" + "--tolerance " + tol);
        System.out.println("\t" + "--nrow " + nrow);
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
        System.out.println("output: " + output);
        System.out.println();
        
        WriteTxtFile wtf = new WriteTxtFile();
        wtf.runQuantLD(output, fileName1, fileName2, tDist, winSize, ldMeasure, tol, maxItr,nrow);
        
        long endTime = System.currentTimeMillis();
        long diffTime = endTime - startTime;
        System.out.print("Time used: ");
        wtf.miSecToHourMinSec(diffTime);
        Date cdate = new Date();
        System.out.println("Analysis finished: " + cdate);
    } 
}
