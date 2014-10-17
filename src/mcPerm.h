/* 
 * File:   mcPerm.h
 * Author: fan
 *
 * Created on September 23, 2014, 4:20 PM
 */

#ifndef MCPERM_H
#define	MCPERM_H

using namespace std;

class mcPerm {
public:
    mcPerm();
    mcPerm(const mcPerm& orig);
    virtual ~mcPerm();
    vector<vector<int> > mcperm(const vector<vector<int> > &genoCode1, const vector<vector<int> > &genoCode2, int n);
private:

};

#endif	/* MCPERM_H */

