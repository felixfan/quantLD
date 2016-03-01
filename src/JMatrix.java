package quantld;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Felix Yanhui Fan felixfanyh@gmail.com
 */
public class JMatrix extends CalLD {
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
        Matrix M = new Matrix(a);
        EigenvalueDecomposition E = new EigenvalueDecomposition(M);
        double[] d = E.getRealEigenvalues();
        double[] im = E.getImagEigenvalues();
        
        Matrix M2 = new Matrix(b);
        EigenvalueDecomposition E2 = new EigenvalueDecomposition(M2);
        double[] d2 = E2.getRealEigenvalues();       
        double[] im2 = E2.getImagEigenvalues();
        
        double ev = 0.0;
        int n = d.length;
        for(int i=0; i<n;i++){
            ev += Math.sqrt(Math.pow(d[i]-d2[i],2)+Math.pow(im[i]-im2[i],2));
        }
        
        return ev;
    }
    
    protected double canberraDistance(double[][] a, double[][] b){
        double cad = 0.0;
        int n = a.length;
        for(int i=0; i<n;i++){
            for(int j=0; j<n;j++){
                cad += Math.abs(a[i][j] - b[i][j])/(Math.abs(a[i][j]) + Math.abs(b[i][j]));
            }
        }
        return cad;
    }
    
    protected double minkowskiDistance(double[][] a, double[][] b, double k){
        double mkd = 0.0;
        int n = a.length;
        for(int i=0; i<n;i++){
            for(int j=0; j<n;j++){
                mkd += Math.pow(Math.abs(a[i][j] - b[i][j]),k);
            }
        }
        mkd = Math.pow(mkd, 1/k);
        return mkd;
    }
    
    protected double manhattanDistance(double[][] a, double[][] b){
        return minkowskiDistance(a,b,1);
    }
    
    protected double euclideanDistance(double[][] a, double[][] b){
        return minkowskiDistance(a,b,2);
    }
    
    protected double chebyshevDistance(double[][] a, double[][] b){
        double mkd = 0.0;
        int n = a.length;
        for(int i=0; i<n;i++){
            for(int j=0; j<n;j++){
                double tmp = Math.abs(a[i][j] - b[i][j]);
                if(mkd < tmp){
                    mkd = tmp;
                }
            }
        }
        return mkd;
    }
    
    protected double brayCurtisDistance(double[][] a, double[][] b){
        double bcd = 0.0;
        int n = a.length;
        for(int i=0; i<n;i++){
            for(int j=0; j<n;j++){
                bcd += Math.abs(a[i][j] - b[i][j])/(a[i][j] + b[i][j]);
            }
        }
        return bcd;
    }
}
