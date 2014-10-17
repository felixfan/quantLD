/* 
 * File:   pairwisePops.cpp
 * Author: fan
 * 
 * Created on September 22, 2014, 4:08 PM
 */
#include <iostream>
#include <vector>
#include <string>

#include "pairwisePops.h"
#include "Eigen/Dense"

using namespace std;
using namespace Eigen;

pairwisePops::pairwisePops() {
}

pairwisePops::pairwisePops(const pairwisePops& orig) {
}

pairwisePops::~pairwisePops() {
}

// Tmethod, Tpercent, EVD
vector<double> pairwisePops::pwTMTPEVD(const vector<vector<double> > &fr, const vector<vector<double> > &sr, int windowSize, string method){  
    vector<double> result;
    
    for(auto i=fr.begin(),j=sr.begin(); i<fr.end(); i++, j++){
        MatrixXd mat1(windowSize,windowSize);
        MatrixXd mat2(windowSize,windowSize);
        mat1 = amv.vectorToMatrix(&(*i), windowSize, true);
        mat2 = amv.vectorToMatrix(&(*j), windowSize, true);
        if(method == "tm"){
            result.push_back(pwm.Tmethod(mat1,mat2,windowSize));
        }else if(method == "tp"){
            result.push_back(pwm.Tpercent(mat1,mat2,windowSize));
        }else if(method == "ev"){
            result.push_back(pwm.EigenValueDiff(mat1,mat2));
        }
        
    }
    return result; 
}

// ALL, Jennrich Test & SteigerTest
vector<vector<double> > pairwisePops::pwSTJTALL(const vector<vector<double> > &fr, const vector<vector<double> > &sr, int windowSize, string method, int n1, int n2, bool fisher){
    vector<vector<double> > results;
    
    for(auto i=fr.begin(),j=sr.begin(); i<fr.end(); i++, j++){
        MatrixXd mat1(windowSize,windowSize);
        MatrixXd mat2(windowSize,windowSize);
        mat1 = amv.vectorToMatrix(&(*i), windowSize, true);
        mat2 = amv.vectorToMatrix(&(*j), windowSize, true);
                
        // result order (all): ST.P ST.Chisq ST.df JT.P JT.Chisq JT.df Tmethod Tpercent EVD 
        // result order (ST): ST.P ST.Chisq ST.df
        // result order (JT): JT.P JT.Chisq JT.df
        if(method == "all"){
            vector<double> result;
            vector<double> temp = pwm.SteigerTest(mat1,mat2,windowSize,n1,n2,fisher);
            for(auto i=temp.begin(); i< temp.end(); i++){
                auto x = *i;
                result.push_back(x);
            }

            temp = pwm.JennrichTest(mat1,mat2,windowSize,n1,n2);
            for(auto i=temp.begin(); i< temp.end(); i++){
                auto x = *i;
                result.push_back(x);
            }

            result.push_back(pwm.Tmethod(mat1,mat2,windowSize));
            result.push_back(pwm.Tpercent(mat1,mat2,windowSize));
            result.push_back(pwm.EigenValueDiff(mat1,mat2));
            
            results.push_back(result);
        }else if(method == "st"){
            results.push_back(pwm.SteigerTest(mat1,mat2,windowSize,n1,n2,fisher));
        }else if(method == "jt"){
            results.push_back(pwm.JennrichTest(mat1,mat2,windowSize,n1,n2));
        }  
    }
    return results;
}

vector<vector<double> > pairwisePops::pwTMTPEVDperm(const vector<vector<double> > &fr, const vector<vector<double> > &sr, const vector<vector<int> > &genoCode1, const vector<vector<int> > &genoCode2, int windowSize, string method, int perm, int n1, int n2, string ldMeasure, string ldMethod){
    vector<double> org;
    
    for(auto i=fr.begin(),j=sr.begin(); i<fr.end(); i++, j++){
        MatrixXd mat1(windowSize,windowSize);
        MatrixXd mat2(windowSize,windowSize);
        mat1 = amv.vectorToMatrix(&(*i), windowSize, true);
        mat2 = amv.vectorToMatrix(&(*j), windowSize, true);
        if(method == "tm"){
            org.push_back(pwm.Tmethod(mat1,mat2,windowSize));
        }else if(method == "tp"){
            org.push_back(pwm.Tpercent(mat1,mat2,windowSize));
        }else if(method == "ev"){
            org.push_back(pwm.EigenValueDiff(mat1,mat2));
        }        
    }
    
    // permutation 
    cout << "permutation ... " << endl;
    int totw = genoCode1.size()-windowSize+1; // total number of windows
    vector<double> mcp(totw); // permutation p value
    
    // initialize permutation p value
    for(int i=0; i< totw; i++){
        mcp[i]=1.0;
    }
    
    for(int i=0; i<perm; i++){
        cout << " " << i+1 << "/" << perm << " ... " <<endl;
        vector<vector<int> > simGC1 = mcpm.mcperm(genoCode1, genoCode2, n1);
        vector<vector<int> > simGC2 = mcpm.mcperm(genoCode1, genoCode2, n2);
        
        vector<vector<double> > mcfr, mcsr;
    
        mcfr = myCalLD.calRD2(simGC1, windowSize, ldMeasure, ldMethod);
        mcsr = myCalLD.calRD2(simGC2, windowSize, ldMeasure, ldMethod);
   
        vector<double> sim;
    
        for(auto i=mcfr.begin(),j=mcsr.begin(); i<mcfr.end(); i++, j++){
            MatrixXd mat1(windowSize,windowSize);
            MatrixXd mat2(windowSize,windowSize);
            mat1 = amv.vectorToMatrix(&(*i), windowSize, true);
            mat2 = amv.vectorToMatrix(&(*j), windowSize, true);
            if(method == "tm"){
                sim.push_back(pwm.Tmethod(mat1,mat2,windowSize));
            }else if(method == "tp"){
                sim.push_back(pwm.Tpercent(mat1,mat2,windowSize));
            }else if(method == "ev"){
                sim.push_back(pwm.EigenValueDiff(mat1,mat2));
            }    
        }
                
        for(int i=0; i<sim.size(); i++){
            if(sim[i] > org[i]){
                mcp[i]++;
            }
        }
    }
    
    cout << endl;
    // p value: (m+1)/(n+1)
    for(int i=0; i<mcp.size(); i++){   
        mcp[i]/=(perm+1.0);
    }
    
    vector<vector<double> > results;
    for(auto i=org.begin(), j=mcp.begin(); i<org.end();i++,j++){
        vector<double> result;
        result.push_back(*i);
        result.push_back(*j);
        results.push_back(result);
    }
    return results; 
}

vector<vector<double> > pairwisePops::pwAllPerm(const vector<vector<double> > &fr, const vector<vector<double> > &sr, const vector<vector<int> > &genoCode1, const vector<vector<int> > &genoCode2, int windowSize, int perm, int n1, int n2, string ldMeasure, string ldMethod, bool fisher){    
    vector<vector<double> > results;
    vector<double> tms;
    vector<double> tps;
    vector<double> evs;
    
    for(auto i=fr.begin(),j=sr.begin(); i<fr.end(); i++, j++){
        MatrixXd mat1(windowSize,windowSize);
        MatrixXd mat2(windowSize,windowSize);
        mat1 = amv.vectorToMatrix(&(*i), windowSize, true);
        mat2 = amv.vectorToMatrix(&(*j), windowSize, true);
        
        vector<double> result;
        result.reserve(12);
        // result order: ST.P ST.Chisq ST.df JT.P JT.Chisq JT.df Tmethod Tpercent EVD TM.P TP.P EVD.P
        tms.push_back(pwm.Tmethod(mat1,mat2,windowSize));
        tps.push_back(pwm.Tpercent(mat1,mat2,windowSize));
        evs.push_back(pwm.EigenValueDiff(mat1,mat2));
        
        vector<double> temp = pwm.SteigerTest(mat1,mat2,windowSize,n1,n2, fisher);
        for(auto i=temp.begin(); i< temp.end(); i++){
            auto x = *i;
            result.push_back(x);
        }
        
        temp = pwm.JennrichTest(mat1,mat2,windowSize,n1,n2);
        for(auto i=temp.begin(); i< temp.end(); i++){
            auto x = *i;
            result.push_back(x);
        }
        
        results.push_back(result);
    }
    
    //permutation
    cout << "permutation ... " << endl;
    int totw = genoCode1.size()-windowSize+1; // total number of windows
    vector<double> mcptm(totw); // permutation p value
    vector<double> mcptp(totw); // permutation p value
    vector<double> mcpev(totw); // permutation p value
    
    // initialize permutation p value
    for(int i=0; i< totw; i++){
        mcptm[i]=1.0;
        mcptp[i]=1.0;
        mcpev[i]=1.0;
    }
    
    for(int i=0; i<perm; i++){
        cout << " " << i+1 << "/" << perm << " ... " <<endl;
        vector<vector<int> > simGC1 = mcpm.mcperm(genoCode1,genoCode2,n1);
        vector<vector<int> > simGC2 = mcpm.mcperm(genoCode1,genoCode2,n2);
        
        vector<vector<double> > mcfr, mcsr;
    
        mcfr = myCalLD.calRD2(simGC1, windowSize, ldMeasure, ldMethod);
        mcsr = myCalLD.calRD2(simGC2, windowSize, ldMeasure, ldMethod);
    
        vector<double> simtms, simtps, simevs;
    
        for(auto i=mcfr.begin(),j=mcsr.begin(); i<mcfr.end(); i++, j++){
            MatrixXd mat1(windowSize,windowSize);
            MatrixXd mat2(windowSize,windowSize);
            mat1 = amv.vectorToMatrix(&(*i), windowSize, true);
            mat2 = amv.vectorToMatrix(&(*j), windowSize, true);
            simevs.push_back(pwm.EigenValueDiff(mat1,mat2));
            simtms.push_back(pwm.Tmethod(mat1,mat2, windowSize));
            simtps.push_back(pwm.Tpercent(mat1,mat2, windowSize));
        }
        
        for(int i=0; i<simevs.size(); i++){
            if(simevs[i] > evs[i]){
                mcpev[i]++;
            }
            if(simtms[i] > tms[i]){
                mcptm[i]++;
            }
            if(simtps[i] > tps[i]){
                mcptp[i]++;
            }
        }
    }
    cout << endl;
    
    // p value: (m+1)/(n+1)
    for(int i=0; i<mcpev.size(); i++){   
        mcpev[i]/=(perm+1.0);
        mcptm[i]/=(perm+1.0);
        mcptp[i]/=(perm+1.0);
    }
    
    // result order: ST.P ST.Chisq ST.df JT.P JT.Chisq JT.df Tmethod Tpercent EVD TM.P TP.P EVD.P
    auto i=results.begin();
    for(auto j=tms.begin(), k=mcptm.begin(), l=tps.begin(), m=mcptp.begin(), n= evs.begin(), o=mcpev.begin(); i<results.end();i++,j++,k++,l++,m++,n++,o++){
        i -> push_back(*j);
        i -> push_back(*l);
        i -> push_back(*n);
        i -> push_back(*k);       
        i -> push_back(*m);        
        i -> push_back(*o);
    }
    
    return results;
}