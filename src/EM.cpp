/* 
 * File:   em.cpp
 * Author: fan
 * 
 * Created on September 16, 2014, 5:01 PM
 */
#include <iostream>
#include <vector>
#include <cmath>
#include <cstdlib>

#include "EM.h"

using namespace std;

EM::EM() {
}

EM::EM(const EM& orig) {
}

EM::~EM() {
}

vector<double> EM::esem_step(vector<double> h, int x[3][3]){
    /*
    Excoffier-Slatkin EM algorithm for estimating haplotype frequencies.
    This code implements the special case of the algorithm for two biallelic loci. 
    With two biallelic loci, there are 4 types of gamete, which were represented as follows:
                             AB     Ab    aB     ab
                              0      1     2      3
    where 'A' and 'a' are the two alleles at the first locus, and 
    'B' and 'b' are the alleles at the second locus. 

    h is a vector of 4 haplotype frequencies, indexed as shown above.

    The genotype at the 1st and 2nd locus were represented as follows:
                              AA      Aa       aa
                              BB      Bb       bb
                               0       1        2

    x is a 3 X 3 matrix of genotype counts. 
    e.g. x[1][2] is the number of copies of the genotype Aa/bb.

    n is the sample size and should equal the sum of x.

    Output:
    hh, a revised estimate of h after a single EM step.
    */
    
    // g is a 4X4 matrix of probability of the genotype made up of haplotype i and j. 
    // e.g. g[0][3] is the probability of the genotype that combines gamete 0 (AB) with gamete 3 (ab).
    double g[4][4]={ {0,0,0,0}, {0,0,0,0}, {0,0,0,0}, {0,0,0,0}};
    
    for(int i=0; i<4; i++){
        g[i][i]=h[i]*h[i];
        for(int j=0; j<i; j++){
            g[i][j]=2*h[i]*h[j];
        }
    }
    
    // p is a 3X3 matrix of genotype frequencies, recoded as described for the input matrix x.  
    double p[3][3]={ {0,0,0}, {0,0,0}, {0,0,0} };
    
    p[0][0]=g[0][0];
    p[0][1]=g[1][0];
    p[0][2]=g[1][1];
    
    p[1][0]=g[2][0];
    p[1][1]=g[3][0] + g[2][1];
    p[1][2]=g[3][1];
    
    p[2][0]=g[2][2];
    p[2][1]=g[3][2];
    p[2][2]=g[3][3];
    
    vector<double> hh = {0,0,0,0};
    
    // number of each haplotype
    hh[0]=2*x[0][0] + x[0][1] + x[1][0] + x[1][1]*g[3][0]/p[1][1];
    hh[1]=x[0][1] + 2*x[0][2] + x[1][1]*g[2][1]/p[1][1] + x[1][2];
    hh[2]=x[1][0] + x[1][1]*g[2][1]/p[1][1] + 2*x[2][0] + x[2][1];
    hh[3]=x[1][1]*g[3][0]/p[1][1] + x[1][2] + x[2][1] + 2*x[2][2];
    
    // haploid sample size
    double n = hh[0] + hh[1] + hh[2] + hh[3];
    
    // frequencies of each haplotype
    for(int i=0; i<4; i++){
        hh[i] /=n ;
    }
    
    return hh;
}

vector<double> EM::esem(vector<int> gf, vector<int> gs){
    /*
    Excoffier-Slatkin EM algorithm for estimating haplotype frequencies.
    Input:
    gf is vector of genotype values at 1st locus, coded as 0, 1, and 2 to represent genotypes AA, Aa, and aa.
    gs is the corresponding vector for 2nd locus, coded as 0, 1, and 2 to represent genotypes BB, Bb, and bb.
    Output:
    h, a vector of 4 haplotype frequencies.
    */
    
    // x is a 3 X 3 matrix of genotype counts.
    int x[3][3] = { {0,0,0},{0,0,0},{0,0,0} };

    vector<int>::iterator i;
    vector<int>::iterator j;
    
    for(i=gf.begin(),j=gs.begin(); i < gf.end(); i++,j++){
        if(*i < 3 && *j<3){ // only use the non-missing data, missing genotype will be coded as 9.
            x[*i][*j]+=1;
        } 
    }
    
    vector<double> h=hf;
    vector<double> hh={0,0,0,0};
    double dh;
    
    // calculate the haplotype frequency based on the given genotype counts and haplotype frequency
    // if the difference between calculated haplotype frequency and the input haplotype frequency is small enough, stop. 
    for(int i=0; i<max_itr; i++){
        hh = esem_step(h,x);
        dh = 0;
        vector<double>::iterator a;
        vector<double>::iterator b;
        for(a=h.begin(),b=hh.begin(); a < h.end(); a++,b++){
            dh += abs(h[*a]-hh[*b]);
        }
        if(dh <= tol){
            break;
        }
        h=hh;
    }
    
    if(dh > tol){
        cout << "convergence error!" << endl;
    }
    
    return hh;
}

double EM::esem_Rsquared(vector<int> gf, vector<int> gs){
    /*
    Use Excoffier-Slatkin EM algorithm to estimate r squared.
    Input:
    gf is vector of genotype values at 1st locus, coded as 0, 1, and 2 to represent genotypes AA, Aa, and aa.
    gs is the corresponding vector for 2nd locus, coded as 0, 1, and 2 to represent genotypes BB, Bb, and bb.
    OutPut:
    r squared
    */
    
    // D = P(AB)P(ab)-P(Ab)P(aB)
    // r = D/sqrt(P(A)P(B)P(a)P(b))
    vector<double> h=esem(gf,gs);
    double pA = h[0]+h[1];
    double pB = h[0]+h[2];
    double qA = 1 - pA;
    double qB = 1- pB;
    double r = h[0]*h[3] - h[1]*h[2];
    r *= r;
    r /= (pA*pB*qA*qB);
    return r;
}

double EM::esem_D(vector<int> gf, vector<int> gs){
    /*
    Use Excoffier-Slatkin EM algorithm to estimate r squared.
    Input:
    gf is vector of genotype values at 1st locus, coded as 0, 1, and 2 to represent genotypes AA, Aa, and aa.
    gs is the corresponding vector for 2nd locus, coded as 0, 1, and 2 to represent genotypes BB, Bb, and bb.
    OutPut:
    D
    */
    
    // D = P(AB)P(ab)-P(Ab)P(aB)
    vector<double> h=esem(gf,gs);
    double d = h[0]*h[3] - h[1]*h[2];
    return d;
}

double EM::esem_Dprime(vector<int> gf, vector<int> gs){
    /*
    Use Excoffier-Slatkin EM algorithm to estimate r squared.
    Input:
    gf is vector of genotype values at 1st locus, coded as 0, 1, and 2 to represent genotypes AA, Aa, and aa.
    gs is the corresponding vector for 2nd locus, coded as 0, 1, and 2 to represent genotypes BB, Bb, and bb.
    OutPut:
    D prime
    Note: D prime ranges between 0 and 1
    */
    
    // D = P(AB)P(ab)-P(Ab)P(aB)
    // Dprime = D/Dmax   if D>0
    // Dprime = D/Dmin   if D<0
    // Dmax = min(P(A)P(b), P(B)P(a))
    // Dmin = max(-P(A)P(B), -P(a)P(b))
    vector<double> h=esem(gf,gs);
    double d = h[0]*h[3] - h[1]*h[2];
    double pA = h[0]+h[1];
    double pB = h[0]+h[2];
    double qA = 1 - pA;
    double qB = 1- pB;
    double mm = 1.0;
    if(d < 0){
        mm = max(-pA*pB, -qA*qB);
    }else{
        mm = min(pA*qB, qA*pB);
    }
    return d/mm;
}

//////////////////////////////////////////////////////////////////////////////
//////////////////   Uses the method of Rogers and Huff 2008 /////////////////
//////////////////////////////////////////////////////////////////////////////

vector<double> EM::bivmom(vector<int> vec0, vector<int> vec1){
    /*
    Calculate means, variances, and covariance from two data vectors.
    On entry, vec0 and vec1 should be vectors of numeric values 
    and should have the same length. 
    
    Function returns m0, v0, m1, v1, and cov, 
    where m0 and m1 are the means of vec0 and vec1, 
    v0 and v1 are the variances, 
    and cov is the covariance.
    */
    
    // var(x) = E(x^2) - [E(x)]^2
    // cov(x,y) = E(x*y) - E(x) *E(y)
    
    if(vec0.size() != vec1.size()){
        cout << "two vectors should have the same length" << endl;
        exit(9);
    }
    
    double m0, m1, v0, v1, cov;
    m0 = m1 = v0 = v1 = cov = 0;
    for(auto i=vec0.begin(), j=vec1.begin(); i<vec0.end(); i++,j++){
        m0 += *i;
        m1 += *j;
        v0 += (*i) * (*i);
        v1 += (*j) * (*j);
        cov += (*i) * (*j);
    }
    
    m0 /= vec0.size();
    m1 /= vec0.size();
    v0 /= vec0.size();
    v1 /= vec0.size();
    cov /= vec0.size();
    
    v0 -= m0 * m0;
    v1 -= m1 * m1;
    cov -= m0 * m1;
    
    vector<double> result;
    result.push_back(m0);
    result.push_back(m1);
    result.push_back(v0);
    result.push_back(v1);
    result.push_back(cov);
    
    return result;
}

double EM::rh_D(vector<int> gf, vector<int> gs){
    vector<double> para = bivmom(gf, gs);
    double pA, pB, v0, v1, cov;
    pA = para[0];
    pB = para[1];
    v0 = para[2];
    v1 = para[3];
    cov = para[4];

    pA = 0.5 * pA;
    pB = 0.5 * pB;
    double qA = 1 - pA;
    double qB = 1 - pB;
    double two_1pf = sqrt((v0 * v1) / (pA * qA * pB * qB)); //estimates 2(1+f)
    double D = cov/two_1pf;
    return D;
}

double EM::rh_Dprime(vector<int> gf, vector<int> gs){
    vector<double> para = bivmom(gf, gs);
    double pA, pB, v0, v1, cov;
    pA = para[0];
    pB = para[1];
    v0 = para[2];
    v1 = para[3];
    cov = para[4];

    pA = 0.5 * pA;
    pB = 0.5 * pB;
    double qA = 1 - pA;
    double qB = 1 - pB;
    double two_1pf = sqrt((v0 * v1) / (pA * qA * pB * qB)); //estimates 2(1+f)
    double D = cov/two_1pf;
    
    double mm = 1.0;
    if(D < 0){
        mm = max(-pA*pB, -qA*qB);
    }else{
        mm = min(pA*qB, qA*pB);
    }
    
    return D/mm;
}
   
double EM::rh_Rsquared(vector<int> gf, vector<int> gs){
    vector<double> para = bivmom(gf, gs);
    double v0, v1, cov;
    v0 = para[2];
    v1 = para[3];
    cov = para[4];
    double r2 = cov * cov / v0 / v1;
    return r2;
}

