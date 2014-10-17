/* 
 * File:   assignMatrixValues.cpp
 * Author: fan
 * 
 * Created on October 3, 2014, 11:06 AM
 */

#include "assignMatrixValues.h"

assignMatrixValues::assignMatrixValues() {
}

assignMatrixValues::assignMatrixValues(const assignMatrixValues& orig) {
}

assignMatrixValues::~assignMatrixValues() {
}

MatrixXd assignMatrixValues::vectorToMatrix(const vector<double> *dv, int row, bool byRow){
    int len = (*dv).size();
    int col = len / row;
    
    MatrixXd mat(row,col);
    
    if(col * row != len){
        cout << "only the first " << (col * row) << "elements in the vector will be used." << endl;
    }
    
    int k=0;
    
    if(byRow==true){
        for(int i=0; i<row;i++){
            for(int j=0;j<col;j++){
                mat(i,j)= (*dv)[k]; 
                k++;
            }
        }
    }else{
        for(int i=0; i<col;i++){
            for(int j=0;j<row;j++){
                mat(j,i)= (*dv)[k]; 
                k++;
            }
        }
    }
    
    return mat;  
}

MatrixXf assignMatrixValues::vectorToMatrix(const vector<float> *fv, int row, bool byRow){
    int len = (*fv).size();
    int col = len / row;   
    MatrixXf mat(row,col);

    if(col * row != len){
        cout << "only the first " << (col * row) << "elements in the vector will be used." << endl;
    }
    
    int k=0;
    
    if(byRow==true){
        for(int i=0; i<row;i++){
            for(int j=0;j<col;j++){
                mat(i,j)= (*fv)[k]; 
                k++;
            }
        }
    }else{
        for(int i=0; i<col;i++){
            for(int j=0;j<row;j++){
                mat(j,i)= (*fv)[k]; 
                k++;
            }
        }
    }
    
    return mat;  
}

MatrixXi assignMatrixValues::vectorToMatrix(const vector<int> *iv, int row, bool byRow){
    int len = (*iv).size();
    int col = len / row;
    
    MatrixXi mat(row,col);
    
    if(col * row != len){
        cout << "only the first " << (col * row) << "elements in the vector will be used." << endl;
    }
    
    int k=0;
    
    if(byRow==true){
        for(int i=0; i<row;i++){
            for(int j=0;j<col;j++){
                mat(i,j)= (*iv)[k]; 
                k++;
            }
        }
    }else{
        for(int i=0; i<col;i++){
            for(int j=0;j<row;j++){
                mat(j,i)= (*iv)[k]; 
                k++;
            }
        }
    }
    
    return mat;  
}
