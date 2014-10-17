/* 
 * File:   em.h
 * Author: fan
 *
 * Created on September 16, 2014, 5:01 PM
 */

#ifndef EM_H
#define	EM_H

using namespace std;

class EM {
public:
    EM();
    EM(const EM& orig);
    virtual ~EM();   
    double esem_D(vector<int> gf, vector<int> gs);
    double esem_Dprime(vector<int> gf, vector<int> gs);
    double esem_Rsquared(vector<int> gf, vector<int> gs);
    double rh_D(vector<int> gf, vector<int> gs);
    double rh_Dprime(vector<int> gf, vector<int> gs);
    double rh_Rsquared(vector<int> gf, vector<int> gs);
    double hill_D(vector<int> gf, vector<int> gs);
    double hill_Dprime(vector<int> gf, vector<int> gs);
    double hill_Rsquared(vector<int> gf, vector<int> gs);
protected:
    const vector<double> hf={0.25,0.25,0.25,0.25}; // the initial vector of haplotype frequencies
    const int max_itr=1000;                        // max iterations controls convergence
    const double tol = 1e-3;                       // tolerance controls convergence.
    vector<double> esem_step(vector<double> h, int x[3][3]);
    vector<double> esem(vector<int> gf, vector<int> gs);
    vector<double> bivmom(vector<int> v1, vector<int> v2);
    vector<double> hill_Method(vector<int> gf, vector<int> gs);
};

#endif	/* EM_H */

