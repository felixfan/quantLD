/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quantld;

import java.io.IOException;
import java.util.Arrays;
import org.apache.commons.math3.linear.*;

/**
 *
 * @author Felix Yanhui Fan felixfanyh@gmail.com
 */
public class Test {
    private double tol = 0.001;
    private int maxItr = 1000;
    
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
    
    void testMatrix(){
        double[][] a = {{1.44, 1.69, 2.25},{1.69, 6.25,1.21},{2.25,1.21,1.96}};
        double[][] b = {{1.44, 1.69, 2.25},{1.69, 6.25,1.21},{2.25,1.21,1.96}};
        Array2DRowRealMatrix ma = new Array2DRowRealMatrix(a);
        Array2DRowRealMatrix mb = new Array2DRowRealMatrix(b);
        System.out.println("matrix is:" + ma);
        System.out.println("Row Dimension is: " + ma.getRowDimension());    // 3
        System.out.println("Column Dimension is: " + mb.getColumnDimension()); // 3
        System.out.println("it is square matrix! : " + ma.isSquare());
        System.out.println("ma + mb = " + ma.add(mb));
        System.out.println("ma + 5 = " + ma.scalarAdd(5.0));
        // matrix sub
        System.out.println("ma - mb = " + ma.subtract(mb));
        // matrix norm
        System.out.println("the maximum absolute row sum norm is " + ma.getNorm());
        // matrix multiply
        System.out.println("ma * mb = " + ma.multiply(mb));
        System.out.println("ma * 5.0 = " + ma.scalarMultiply(5));
        System.out.println("mb * ma = " + ma.preMultiply(mb));
        // matrix trace
        System.out.println("the trace is " + ma.getTrace());
        // matrix transpose
        System.out.println("the transpose of mat1 is " + ma.transpose());
        // matrix to vector
        System.out.println("the first row vector is " + ma.getRowVector(0));
        // matrix get sub matrix of selected rows and columns
        System.out.println("sub matrix of ma is " + ma.getSubMatrix(new int[] { 0, 2 }, new int[] { 1, 2 }));
        // Eigenvalues/eigenvectors
        EigenDecomposition ed = new EigenDecomposition(ma);
        double[] re = ed.getRealEigenvalues();
        System.out.println("Eigen values are " + Arrays.toString(re));
        Eigen eig = new Eigen();
        Array2DRowRealMatrix v = eig.getEigenvectors(ma);
        System.out.println("Eigen vectors are " + v);
    }
}
