/* 
 * File:   calLD.h
 * Author: fan
 *
 * Created on September 18, 2014, 3:46 PM
 */

#ifndef CALLD_H
#define	CALLD_H

using namespace std;

class calLD {
public:
    calLD();
    calLD(const calLD& orig);
    virtual ~calLD();
    vector<vector<double> > calRD(const vector<vector<int> > &genoCode, int windowSize, string ldMeasure, string ldMethod);
    vector<vector<double> > calRD2(const vector<vector<int> > &genoCode, int windowSize, string ldMeasure, string ldMethod);
private:

};

#endif	/* CALLD_H */

