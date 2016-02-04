/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quantld;

import java.io.IOException;
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
        
        final double TOLERANCE = 0.001;
        final int MAXITERATE = 1000;
        String ldMeasure = "r2";
        double tol = TOLERANCE;
        int maxItr = MAXITERATE;
        if(params.containsKey("t")){
            tol = Double.parseDouble(params.get("t"));
        }
        if(params.containsKey("i")){
            maxItr = Integer.parseInt(params.get("i"));
        }
        if(params.containsKey("m")){
            ldMeasure = params.get("m");
        }
        
        System.out.println("first input genotype file: " + fileName1);
        System.out.println("second input genotype file: " + fileName2);
        System.out.println("LD measure: " + ldMeasure);
        System.out.println("maximum number of EM steps: " + maxItr);
        System.out.println("convergence tolerance: " + tol);
        
        
        Test mytest = new Test(tol, maxItr);
        //mytest.testRecoding(fileName1); // passed
        //mytest.testEM();                // passed
        //mytest.testCalLD(fileName1, ldMeasure); // passed
        mytest.testMatrix();
        
    } 
}
