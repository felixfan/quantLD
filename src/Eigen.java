/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quantld;

import org.apache.commons.math3.linear.*;

/**
 *
 * @author Felix Yanhui Fan felixfanyh@gmail.com
 */

public class Eigen {
    /** 
     * Compute the eigenvectors of the matrix m.
     * @param m Array2DRowRealMatrix
     * @return The eigenvectors of the matrix m.
     */
    public Array2DRowRealMatrix getEigenvectors(Array2DRowRealMatrix m){
        int nbRow = m.getRowDimension();
        int nbCol = m.getColumnDimension();
        EigenDecomposition eigen = new EigenDecomposition(m);
        double[][] eigenvectorsValues = new double[nbRow][nbCol];
        for (int i=0; i < nbRow; i++) {
            eigenvectorsValues[i]=eigen.getEigenvector(i).toArray();
        }
        Array2DRowRealMatrix v = new Array2DRowRealMatrix(eigenvectorsValues);
        return v;
    }
    
    /**
     * Compute the eigenvalues of the matrix m.
     * @param m Array2DRowRealMatrix
     * @return Array of eigenvalues
     */
    public double[] getEigenvalues(Array2DRowRealMatrix m){
        EigenDecomposition eig = new EigenDecomposition(m);
        double[] ev;
        if(eig.hasComplexEigenvalues()){
            ev = eig.getImagEigenvalues();
        }else{
            ev = eig.getRealEigenvalues();
        }
        return ev;
    }
}
