/* 
 * File:   mcPerm.cpp
 * Author: fan
 * 
 * Created on September 23, 2014, 4:20 PM
 */
#include <iostream>
#include <vector>
#include <ctime>

#include "mcPerm.h"

using namespace std;

mcPerm::mcPerm() {
}

mcPerm::mcPerm(const mcPerm& orig) {
}

mcPerm::~mcPerm() {
}

vector<vector<int> > mcPerm::mcperm(const vector<vector<int> > &genoCode1, const vector<vector<int> > &genoCode2, int n){
    /*
    merge genotype code from two populations with sample size of n1 and n2, then 
    resampling n (n1 or n2) samples from the combined data with replacement to yield 
    two random populations (need to run it twice) of the sample size identical as previous observed.
    */
    vector<vector<int> > simPops;
    
    for(auto i=genoCode1.begin(),j=genoCode2.begin();i<genoCode1.end();i++,j++){
        // merge two vector
        int tot = (*i).size() + (*j).size();
        vector<int> ij;
        ij.reserve(tot); // preallocate memory
        ij.insert(ij.end(), i->begin(), i->end());
        ij.insert(ij.end(), j->begin(), j->end());
        
        // resampling n samples
        vector<int> simPop;
        srand(time(NULL)); //  you should seed only once and get many random numbers
        for(int i=0; i<n; i++){
            int x = rand() % tot;
            simPop.push_back(ij[x]);
        }
        
        simPops.push_back(simPop);
    }
    
    return simPops;
}
