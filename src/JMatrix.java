package quantld;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Felix Yanhui Fan felixfanyh@gmail.com
 */
public class JMatrix {
    /**
     * calculate T statistic
     * @param a
     * @param b
     * @return 
     */
    protected double calTstatistic(double[][] a, double[][] b){
        double t = 0.0;
        int n = a.length;
        for(int i=0; i<n;i++){
            for(int j=i; j<n;j++){
                t += Math.abs(a[i][j] - b[i][j]);
            }
        }
        return t;
    }
    
    /**
     * calculate T% statistic
     * @param a
     * @param b
     * @return 
     */
    protected double calTpercent(double[][] a, double[][] b){
        double m = 0.0;
        int n = a.length;
        for(int i=0; i<n;i++){
            for(int j=i; j<n;j++){
                m = m + a[i][j] - b[i][j];
            }
        }
        
        double t = calTstatistic(a,b);
        double tp = 200 * t / m;
        return tp;
    }
    
    /**
     * varLD statistic
     * @param a
     * @param b
     * @return 
     */
    protected double calTraceDiffEigValue(double[][] a, double[][] b){
       Array2DRowRealMatrix ma = new Array2DRowRealMatrix(a);
       Array2DRowRealMatrix mb = new Array2DRowRealMatrix(b);
       EigenDecomposition eda = new EigenDecomposition(ma);
       EigenDecomposition edb = new EigenDecomposition(mb);
       double[] eva = eda.getRealEigenvalues();
       double[] evb = edb.getRealEigenvalues();
       double ev = 0.0;
       int n = eva.length;
       for(int i=0; i<n;i++){
           ev += Math.abs(eva[i] - evb[i]);
       }
       return ev;
    }
    
    
}
