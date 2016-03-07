/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quantld;


/**
 *
 * @author Felix Yanhui Fan felixfanyh@gmail.com
 */
public class CalLD extends EM {   
    public CalLD(){
        super.tol = 0.001;
        super.maxItr = 1000;
    }
    
    public CalLD(double tol,int maxItr){
        super.tol = tol;
        super.maxItr = maxItr;
    }
    
    /**
     * calculate LD matrix
     * @param genoCode is 2D array of genotype codes
     * @param windowSize size of window
     * @param ldMeasure LD measures, r2, dp, sr2
     * @return matrix of LD measure for all windows
     */
    public double[][][] CalRDprime(int[][] genoCode, int windowSize, String ldMeasure){   
        if(genoCode.length < windowSize){
            System.out.println("\nError: window size is too large for your dataset");
            System.exit(99);
        }
        // total number of windows
        int totWindows = genoCode.length - windowSize + 1;
        
        // output, r2 or D prime, each window has a matrix
        double[][][] rSquares = new double[totWindows][windowSize][windowSize];
        
        // calculate r2 or D prime for each window
        // first window, calculate all matrix 
        for(int i=0; i<windowSize;i++){ // first snp
            for(int j=0; j<windowSize;j++){ // second snp
                if(i == j){ // diagonal, LD with itself
                    rSquares[0][i][j] = 1.0;
                }else if(i<j){ // upright matrix
                    switch (ldMeasure) {
                        case "r2":
                            rSquares[0][i][j] = esemR2(genoCode[i],genoCode[j]);
                            break;
                        case "dp":
                            rSquares[0][i][j] = esemDprime(genoCode[i],genoCode[j]);
                            break;
                        case "sr2":
                            rSquares[0][i][j] = esemSignedR2(genoCode[i],genoCode[j]);
                            break;
                        default:
                            System.out.println("--ld-measure only support r2, dp or sr2");
                            System.exit(1);
                    } 
                }else{ // bottom left matrix 
                    rSquares[0][i][j] = rSquares[0][j][i];
                }
            }
        }
        // second to last windows, only calculate the last row and last column of the matrix
        // other values were already calculated in previous window
        for(int n=1; n<totWindows; n++){
            for(int i=0; i<windowSize;i++){ // first snp
                for(int j=0; j<windowSize;j++){ // second snp
                    if(i < windowSize-1 && j < windowSize-1){// were already calculated in previous matrix 
                        rSquares[n][i][j] = rSquares[n-1][i+1][j+1];
                    }else{
                        if(i<j){// upright matrix, last column
                            switch (ldMeasure) {
                                case "r2":
                                    rSquares[n][i][j] = esemR2(genoCode[n+i],genoCode[n+j]);
                                    break;
                                case "dp":
                                    rSquares[n][i][j] = esemDprime(genoCode[n+i],genoCode[n+j]);
                                    break;
                                case "sr2":
                                    rSquares[n][i][j] = esemSignedR2(genoCode[n+i],genoCode[n+j]);
                                    break;
                                default:
                                    System.out.println("--ld-measure only support r2, dp or sr2");
                                    System.exit(1);
                            }
                        }else if(i==j){
                            rSquares[n][i][j] = 1;
                        }else{// bottom left matrix, last row
                            rSquares[n][i][j] = rSquares[n][j][i];
                        }
                    }
                }
            }
            
        }
        return rSquares;
    }
}
