/* 
 * File:   pairwisePops.h
 * Author: fan
 *
 * Created on September 22, 2014, 4:08 PM
 */

#ifndef PAIRWISEPOPS_H
#define	PAIRWISEPOPS_H

#include "assignMatrixValues.h"
#include "pairwiseMatrices.h"
#include "mcPerm.h"
#include "calLD.h"

using namespace std;

class pairwisePops {
public:
    pairwisePops();
    pairwisePops(const pairwisePops& orig);
    virtual ~pairwisePops(); 
    vector<double> pwTMTPEVD(const vector<vector<double> > &fr, const vector<vector<double> > &sr, int windowSize, string method);
    vector<vector<double> > pwSTJTALL(const vector<vector<double> > &fr, const vector<vector<double> > &sr, int windowSize, string method, int n1, int n2, bool fisher);  
    vector<vector<double> > pwTMTPEVDperm(const vector<vector<double> > &fr, const vector<vector<double> > &sr, const vector<vector<int> > &genoCode1, const vector<vector<int> > &genoCode2, int windowSize, string method, int perm, int n1, int n2, string ldMeasure, string ldMethod);
    vector<vector<double> > pwAllPerm(const vector<vector<double> > &fr, const vector<vector<double> > &sr, const vector<vector<int> > &genoCode1, const vector<vector<int> > &genoCode2, int windowSize, int perm, int n1, int n2, string ldMeasure, string ldMethod, bool fisher);
private:
    assignMatrixValues amv;
    pairwiseMatrices pwm;
    mcPerm mcpm;
    calLD myCalLD;
};

#endif	/* PAIRWISEPOPS_H */

