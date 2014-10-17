/* 
 * File:   assignMatrixValues.h
 * Author: fan
 *
 * Created on October 3, 2014, 11:05 AM
 */

#ifndef ASSIGNMATRIXVALUES_H
#define	ASSIGNMATRIXVALUES_H

#include <iostream>
#include <vector>
#include "Eigen/Dense"

using namespace Eigen;
using namespace std;

class assignMatrixValues {
public:
    assignMatrixValues();
    assignMatrixValues(const assignMatrixValues& orig);
    virtual ~assignMatrixValues();
    MatrixXd vectorToMatrix(const vector<double> *dv, int row, bool byRow);
    MatrixXf vectorToMatrix(const vector<float> *dv, int row, bool byRow);
    MatrixXi vectorToMatrix(const vector<int> *dv, int row, bool byRow);
private:

};

#endif	/* ASSIGNMATRIXVALUES_H */

