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
        System.out.println( "|       quantLD       |    v0.1.0302    |     2 Mar 2016      |");
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
        System.out.println("first input genotype file: " + fileName1);
        System.out.println("second input genotype file: " + fileName2);
        System.out.println("window size: " + winSize);
        System.out.println("LD measure: " + ldMeasure);
        System.out.println("Measure of Distance: " + tDist);
        System.out.println("maximum number of EM steps: " + maxItr);
        System.out.println("convergence tolerance: " + tol);
        System.out.println("output: " + output);
        System.out.println();
        
        //Test mytest = new Test(tol, maxItr);
        //mytest.testRecoding(fileName1); // passed
        //mytest.testEM();                // passed
        //mytest.testCalLD(fileName1, ldMeasure); // passed
        //mytest.testMatrix();                   // passed
        
        //mytest.testMatrixT(mytest.pa, mytest.pb);    // passed
        //mytest.testMatrixTP(mytest.pa, mytest.pb);  // passed
        // mytest.testMatrixEig(mytest.pa, mytest.pb);
        //mytest.testMatrixCAD(mytest.pa, mytest.pb);   // passed
        //mytest.testMatrixMAD(mytest.pa, mytest.pb);   // passed
        //mytest.testMatrixEUD(mytest.pa, mytest.pb);   // passed
        //mytest.testMatrixCSD(mytest.pa, mytest.pb);    // passed
        //mytest.testMatrixBCD(mytest.pa, mytest.pb);  // passed
        
        /*
        BatchLD bld = new BatchLD();
        ReadTxtFile rtf = new ReadTxtFile();
        double[] ans = bld.batchQuantLD(fileName1, fileName2, "evd", winSize, ldMeasure, tol, maxItr);
        double[] pos = rtf.readPos(fileName1, winSize);
        int n = ans.length;
        for(int i=0; i<n;i++){
            System.out.print(pos[i]);
            System.out.print("\t");
            System.out.println(ans[i]);
        }
                */
        
        WriteTxtFile wtf = new WriteTxtFile();
        //wtf.outputTxt(output, fileName1, fileName2, tDist, winSize, ldMeasure, tol, maxItr);
        //wtf.outputTxt(output, fileName1, fileName2, tDist, winSize, ldMeasure, 1, 10, tol, maxItr);
        //wtf.outputTxt(output, fileName1, fileName2, tDist, winSize, ldMeasure, 1, 7, tol, maxItr);
        //wtf.outputTxt(output, fileName1, fileName2, tDist, winSize, ldMeasure, 4, 10, tol, maxItr);
        wtf.runQuantLD(output, fileName1, fileName2, tDist, winSize, ldMeasure, tol, maxItr,nrow);
    } 
}
