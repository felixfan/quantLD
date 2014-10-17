/* 
 * File:   pairwiseMatrices.cpp
 * Author: fan
 * 
 * Created on September 26, 2014, 2:54 PM
 */

#include <iostream>
#include <vector>
#include <cmath>
#include "Eigen/Dense"
#include "boost/math/distributions.hpp"
//#include "boost/math/distributions/chi_squared.hpp"
#include "pairwiseMatrices.h"

using namespace Eigen;
using namespace std;
using namespace boost::math;

pairwiseMatrices::pairwiseMatrices() {
}

pairwiseMatrices::pairwiseMatrices(const pairwiseMatrices& orig) {
}

pairwiseMatrices::~pairwiseMatrices() {
}

double pairwiseMatrices::Tmethod(const MatrixXd &mf, const MatrixXd &ms, int n){
    /*
    T method
    mf and ms are the LD matrix with n x n
    */
    double sum=0;
    for(int i=0; i<n;i++){
        for(int j=0;j<n;j++){
            if(j>=i){
                sum += abs(mf(i,j) - ms(i,j));
            }
        }
    }
    return sum;
}

double pairwiseMatrices::Tpercent(const MatrixXd &mf, const MatrixXd &ms, int n){
    /*
    T percent
    mf and ms are the LD matrix with n x n
    */
    double t=0;
    double sum=0;
    double res = 0;
    for(int i=0; i<n;i++){
        for(int j=0;j<n;j++){
            if(j>=i){
                sum = sum + mf(i,j) + ms(i,j);
                t += abs(mf(i,j) - ms(i,j));
            }
        }
    }
    res = 200 * t / sum;
    return res;
}

// R function cortest.normal in package psych with fisher=FALSE
vector<double> pairwiseMatrices::SteigerTest(const MatrixXd &mf, const MatrixXd &ms, int n, int n1, int n2, bool fisher){
    /*
    Steiger Test
    mf and ms are the LD matrix with n x n
    n1: sample size of mf
    n2: sample size of ms
    */
    MatrixXd mf1(n,n), ms1(n,n);
    if(fisher == true){
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                if(i != j){
                    mf1(i,j)=0.5*log((1+mf(i,j))/(1-mf(i,j)));
                    ms1(i,j)=0.5*log((1+ms(i,j))/(1-ms(i,j)));
                }else{
                    mf1(i,j)=0;
                    ms1(i,j)=0;
                }              
            }
        }
    }else{
        mf1=mf;
        ms1=ms;
    }
    
    MatrixXd m(n,n);
    double chisq=0;
    m = mf1 - ms1;
    m=m.cwiseProduct(m);
    
    for(int i=0; i<n;i++){
        for(int j=0;j<i;j++){
            chisq += m(i,j);
        }
    }
    
    double c = 1.0 * n1 * n2 / (n1+n2);
    chisq *= (c-3);
    double df=n*(n-1)/2.0;
    double p=1;
    if(std::isinf(chisq) || std::isnan(chisq)){
        p=NAN;
    }else if(chisq > 0){
        chi_squared mydist(df);
        p = 1 - cdf(mydist,chisq);
    }
    vector<double> result;
    result.push_back(p);
    result.push_back(chisq);
    result.push_back(df);
    return result;
}

// R function cortest.jennrich in package psych
vector<double> pairwiseMatrices::JennrichTest(const MatrixXd &mf, const MatrixXd &ms, int n, int n1, int n2){
    /*
    Jennrich Test
    mf and ms are the LD matrix with n x n
    n1: sample size of mf
    n2: sample size of ms
    */
    
    MatrixXd r(n,n);  
    MatrixXd s(n,n);
    MatrixXd z(n,n);
    
    double c = 1.0 * n1 * n2 / (n1 + n2);
    
    r = n1 * mf + n2 * ms;
    r /= (n1+n2);
    s = r.cwiseProduct(r);
    
    MatrixXd r_ind = r.inverse();
    MatrixXd s_ind = s.inverse();
    
    MatrixXd r_diff = mf - ms;
    
    z = sqrt(c) * r_ind * r_diff;
    
    auto temp = z * z.transpose();
    auto result = 0.5 * temp.trace();
    
    result = result - (z.diagonal()).transpose() * s_ind * z.diagonal();
    double chisq = result;
    double df=n*(n-1)*0.5;
    double p=1;
    if(std::isinf(chisq) || std::isnan(chisq)){
        p=NAN;
    }else if(chisq > 0){
        chi_squared mydist(df);
        p = 1 - cdf(mydist,chisq);
    }
    vector<double> res;
    res.push_back(p);
    res.push_back(chisq);
    res.push_back(df);
    return res;
}

double pairwiseMatrices::EigenValueDiff(const MatrixXd &mf, const MatrixXd &ms){
    /*
    SelfAdjointEigenSolver<MatrixXd> eigensolver1(mf);
    if (eigensolver1.info() != Success) abort();
    SelfAdjointEigenSolver<MatrixXd> eigensolver2(ms);
    if (eigensolver2.info() != Success) abort();
    */
    try{
        SelfAdjointEigenSolver<MatrixXd> eigensolver1(mf);
        SelfAdjointEigenSolver<MatrixXd> eigensolver2(ms);
        auto ev1 = eigensolver1.eigenvalues();
        auto ev2 = eigensolver2.eigenvalues();
        auto dm = ev1-ev2;
        auto dmabs = dm.array().abs();
        double result = dmabs.sum();
        return result;
    }catch(exception &e){
        cout << e.what() << endl;
    }
    
    
}
