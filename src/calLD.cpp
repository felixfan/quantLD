/* 
 * File:   calLD.cpp
 * Author: fan
 * 
 * Created on September 18, 2014, 3:46 PM
 */
#include <iostream>
#include <vector>

#include "calLD.h"
#include "EM.h"
#include "readData.h"

using namespace std;

calLD::calLD() {
}

calLD::calLD(const calLD& orig) {
}

calLD::~calLD() {
}

vector<vector<double> > calLD::calRD(const vector<vector<int> > &genoCode, int windowSize, string ldMeasure, string ldMethod){
    readData rf;
    EM em;

    vector<vector<double> > rSquares; // each element is a window
    
    // cout << "calculate pair-wise r squared...";
    for(int i=0; i < genoCode.size()-windowSize+1; i++){ // total windows
        // cout << "\twindow " << i+1 << "/" << genoCode.size()-windowSize+1 << "...";
        vector<double> t; // contains all LD measures of one window
        vector<double>  tt; // temp vector to reduce calculation to only half matrix
        tt.resize(genoCode.size() * genoCode.size());
        for(int j=i; j< i+windowSize; j++){ // each window
            for(int k=i; k< i+windowSize; k++){ // pair wise
                if(k==j){
                    t.push_back(1.0); // LD with itself
                }else if(k>j){ // upright matrix
                    vector<vector<int> > genoTwoLoci = rf.rmMissingGenotype(genoCode[j],genoCode[k]);
                    double rSquare;
                    if(ldMeasure=="r2"){
                        if(ldMethod=="rh"){
                            rSquare = em.rh_Rsquared(genoTwoLoci[0],genoTwoLoci[1]);
                        }else if(ldMethod=="esem"){
                            rSquare = em.esem_Rsquared(genoTwoLoci[0],genoTwoLoci[1]);
                        }                       
                    }else{
                        if(ldMethod=="rh"){
                            rSquare = em.rh_Dprime(genoTwoLoci[0],genoTwoLoci[1]);
                        }else if(ldMethod=="esem"){
                            rSquare = em.esem_Dprime(genoTwoLoci[0],genoTwoLoci[1]);
                        }                         
                    }                    
                    t.push_back(rSquare);  
                    tt[genoCode.size() * j + k] =rSquare;
                }else{ // bottom left matrix
                    t.push_back(tt[genoCode.size() * k + j]);
                }
            }
        }
        rSquares.push_back(t);
        // cout << "done" << endl;
    }
    // cout << "calculate pair-wise r squared ... done" << endl;
    // cout << "done" << endl;
    return rSquares;
}

vector<vector<double> > calLD::calRD2(const vector<vector<int> > &genoCode, int windowSize, string ldMeasure, string ldMethod){
    readData rf;
    EM em;

    vector<vector<double> > rSquares; // each element is a window
    
    // temp vector to reduce calculation to only half matrix
    // matrix for window 0 or previous window for second to last windows
    vector<double>  tt; 
    tt.resize(windowSize * windowSize);
        
    // cout << "calculate pair-wise r squared...";
    for(int i=0; i < genoCode.size()-windowSize+1; i++){ // total windows
        vector<double> t; // contains all LD measures of one window 
        
        // temp vector to reduce calculation to only half matrix            
        vector<double>  tt0; 
        tt0.resize(windowSize * windowSize);
        
        // each window
        if(i==0){ // first window
            for(int j=i; j< i+windowSize; j++){ // each row
                for(int k=i; k< i+windowSize; k++){ // each column
                    if(k==j){ // diagonal
                        t.push_back(1.0); // LD with itself
                        tt[windowSize * (j-i) + k-i] = 1.0;
                    }else if(k>j){ // upright matrix
                        vector<vector<int> > genoTwoLoci = rf.rmMissingGenotype(genoCode[j],genoCode[k]);
                        double rSquare;
                        if(ldMeasure=="r2"){
                            if(ldMethod=="rh"){
                                rSquare = em.rh_Rsquared(genoTwoLoci[0],genoTwoLoci[1]);
                            }else if(ldMethod=="esem"){
                                rSquare = em.esem_Rsquared(genoTwoLoci[0],genoTwoLoci[1]);
                            }                       
                        }else{
                            if(ldMethod=="rh"){
                                rSquare = em.rh_Dprime(genoTwoLoci[0],genoTwoLoci[1]);
                            }else if(ldMethod=="esem"){
                                rSquare = em.esem_Dprime(genoTwoLoci[0],genoTwoLoci[1]);
                            }                         
                        }                    
                        t.push_back(rSquare);  
                        tt[windowSize * (j-i) + k-i] = rSquare;
                    }else{ // bottom left matrix
                        tt[windowSize * (j-i) + k-i] = tt[windowSize * (k-i) + j-i];
                        t.push_back(tt[windowSize * (j-i) + k-i]);
                    }
                }
            }
        }else{ // second to last window           
            for(int j=i; j< i+windowSize; j++){ // each row
                for(int k=i; k< i+windowSize; k++){ // each column
                    if(k==j){ // diagonal
                        t.push_back(1.0); // LD with itself
                        tt0[windowSize * (j-i) + k-i] = 1.0;
                    }else if(k>j){ // upright matrix
                        if(k == i + windowSize -1){ // only calculate last column
                            vector<vector<int> > genoTwoLoci = rf.rmMissingGenotype(genoCode[j],genoCode[k]);
                            double rSquare;
                            if(ldMeasure=="r2"){
                                if(ldMethod=="rh"){
                                    rSquare = em.rh_Rsquared(genoTwoLoci[0],genoTwoLoci[1]);
                                }else if(ldMethod=="esem"){
                                    rSquare = em.esem_Rsquared(genoTwoLoci[0],genoTwoLoci[1]);
                                }                       
                            }else{
                                if(ldMethod=="rh"){
                                    rSquare = em.rh_Dprime(genoTwoLoci[0],genoTwoLoci[1]);
                                }else if(ldMethod=="esem"){
                                    rSquare = em.esem_Dprime(genoTwoLoci[0],genoTwoLoci[1]);
                                }                         
                            }
                            t.push_back(rSquare);
                            tt0[windowSize * (j-i) + k-i] = rSquare;
                        }else{ // copy from previous matrix for other columns                            
                            tt0[windowSize * (j-i) + k-i] = tt[windowSize * (j-i+1) + k-i+1]; // tt0[i][j] = tt[i+1][j+1]
                            t.push_back(tt0[windowSize * (j-i) + k-i]);
                        }                        
                    }else{ // bottom left matrix
                        tt0[windowSize * (j-i) + k-i] = tt0[windowSize * (k-i) + j-i];
                        t.push_back(tt0[windowSize * (j-i) + k-i]);
                    }
                }
            }
            tt.clear();
            tt = tt0;
            tt0.clear();
        }        
        
        rSquares.push_back(t);
        t.clear();
    }

    return rSquares;
}
