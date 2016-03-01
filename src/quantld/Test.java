/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quantld;

import java.io.IOException;

/**
 *
 * @author Felix Yanhui Fan felixfanyh@gmail.com
 */
public class Test {
    private double tol = 0.001;
    private int maxItr = 1000;
    
    final double pa[][] = {
            {0.80, 0.25, 0.28, 0.17, 0.52},
            {0.10, 0.13, 0.64, 0.03, 0.01},
            {0.18, 0.28, 0.90, 0.52, 0.07},
            {0.01, 0.37, 0.57, 0.36, 0.94},
            {0.97, 0.13, 0.28, 0.64, 0.29},
        };
    final double pb[][] = {
            {0.06, 0.29, 0.12, 0.24, 0.57},
            {0.44, 0.95, 0.37, 0.03, 0.44},
            {0.28, 0.64, 0.22, 0.54, 0.11},
            {1.00, 0.16, 0.07, 0.44, 0.42},
            {0.98, 0.38, 0.02, 0.99, 0.25}
        };
    
    public Test(){
    }
        
    public Test(double tol, int maxItr){
        this.tol = tol;
        this.maxItr = maxItr;
    }
    
    private final ReadTxtFile rd = new ReadTxtFile();
    private final CalLD cld = new CalLD(tol, maxItr);
    private final EM em = new EM(tol, maxItr);
    
    void testRecoding(String fileName){    
        try{
            int gtp[][] = rd.recodeGenotype(fileName);
            for(int gs[] : gtp){
                for(int g : gs){
                    System.out.print(g);
                    System.out.print(" ");
                }
                System.out.print("\n");
            }
        }catch(IOException e){
            e.printStackTrace(System.out);
        }  
        System.out.println();
    }
    
    void testEM(){
        double ans;
        int[] gf = {0,1,2,2,1,1,1,2,0}; 
        int[] gs = {0,1,2,2,1,1,1,2,0}; 
        ans = em.esemR2(gf, gs);
        System.out.println("r2 = " + ans);
        
        ans = em.esemDprime(gf, gs);
        System.out.println("Dprime = " + ans);
        
        ans = em.esemSignedR2(gf, gs);
        System.out.println("SignedR2 = " + ans);
        
        //int[] gf1 = {1, 1, 0, 2, 2, 1, 1, 0, 2, 2};
        //int[] gs1 = {2, 2, 2, 0, 1, 2, 2, 2, 0, 1};
        int[] gf1 = {2, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 2, 1, 2, 2, 1,
         2, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 2, 1, 1, 1, 1, 1,
         0, 0, 0, 1, 0, 2, 0, 1, 1, 0, 1, 1, 0, 0};
        int[] gs1 = {2, 1, 2, 2, 2, 2, 1, 2, 2, 2, 0, 2, 1, 2, 2, 2, 2, 1,
         2, 1, 2, 1, 2, 2, 1, 1, 1, 2, 2, 1, 2, 1, 1, 2, 2, 2,
         2, 2, 1, 1, 2, 2, 1, 2, 1, 2, 2, 2, 1, 1};
        ans = em.esemR2(gf1, gs1);
        System.out.format("expected value is %.3f\n", 0.374999919073*0.374999919073);
        System.out.format("r2 = %.3f\n", ans);
        
        System.out.println();
    }
    
    void testCalLD(String fileName, String ldMeasure) throws IOException{   
        int gtp[][] = rd.recodeGenotype(fileName);
        //System.out.println(Arrays.deepToString(gtp));
        double[][][] r2 =  cld.CalRDprime(gtp, 3, ldMeasure);
        for(double[][] tmp : r2){
            for(double[] tmp2 : tmp){
                for(double tmp3 : tmp2){
                    System.out.print("\t");
                    System.out.format("%.3f", tmp3); 
                }
                System.out.print("\n");
            }
            System.out.print("\n");
        }
        System.out.println();
    }
    
    void testMatrixT(double[][] a, double[][] b){
        JMatrix jm = new JMatrix();
        System.out.println("T statistic: " + jm.calTstatistic(a, b));
    }
    
    void testMatrixTP(double[][] a, double[][] b){
        JMatrix jm = new JMatrix();
        System.out.println("T percent: " + jm.calTpercent(a, b));
    }
    
    void testMatrixEig(double[][] a, double[][] b){
        JMatrix jm = new JMatrix();
        System.out.println("varLD between a and b is: " + jm.calTraceDiffEigValue(a, b));
    }   
    
    void testMatrixCAD(double[][] a, double[][] b){
        JMatrix jm = new JMatrix();
        System.out.println("Canberra distance: " + jm.canberraDistance(a, b));
    }
    
    void testMatrixMAD(double[][] a, double[][] b){
        JMatrix jm = new JMatrix();
        System.out.println("Manhattan distance: " + jm.manhattanDistance(a, b));
    }
    
    void testMatrixEUD(double[][] a, double[][] b){
        JMatrix jm = new JMatrix();
        System.out.println("Euclidean distance: " + jm.euclideanDistance(a, b));
    }
    
    void testMatrixCSD(double[][] a, double[][] b){
        JMatrix jm = new JMatrix();
        System.out.println("Chebyshev distance: " + jm.chebyshevDistance(a, b));
    }
    
    void testMatrixBCD(double[][] a, double[][] b){
        JMatrix jm = new JMatrix();
        System.out.println("Bray-Curtis dissimilarity: " + jm.brayCurtisDistance(a, b));
    }
    
}
