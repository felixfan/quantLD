/* 
 * File:   pairwiseMatrices.h
 * Author: fan
 *
 * Created on September 26, 2014, 2:53 PM
 */

#ifndef PAIRWISEMATRICES_H
#define	PAIRWISEMATRICES_H

using namespace std;
using namespace Eigen;

class pairwiseMatrices {
public:
    pairwiseMatrices();
    pairwiseMatrices(const pairwiseMatrices& orig);
    virtual ~pairwiseMatrices();
    double Tmethod(const MatrixXd &mf, const MatrixXd &ms, int n);
    double Tpercent(const MatrixXd &mf, const MatrixXd &ms, int n);
    vector<double> SteigerTest(const MatrixXd &mf, const MatrixXd &ms, int n, int n1, int n2, bool fisher);
    vector<double> JennrichTest(const MatrixXd &mf, const MatrixXd &ms, int n, int n1, int n2);
    double EigenValueDiff(const MatrixXd &mf, const MatrixXd &ms);
private:

};

#endif	/* PAIRWISEMATRICES_H */

