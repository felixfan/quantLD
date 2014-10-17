/* 
 * File:   writeFile.cpp
 * Author: fan
 * 
 * Created on September 23, 2014, 9:49 AM
 */

#include <iostream>
#include <vector>
#include <fstream>
#include <iomanip>

#include "writeFile.h"

using namespace std;

writeFile::writeFile() {
}

writeFile::writeFile(const writeFile& orig) {
}

writeFile::~writeFile() {
}

int writeFile::writeVectorToTable(const vector<vector<string> > &infor, const vector<double> &v, string fileName, vector<string> header){
    cout << "writing data to " << fileName << endl;
    
    ofstream myfile(fileName.c_str());        
    
    if(! header.empty()){
        for(auto i = header.begin(); i<header.end();i++){
            myfile << setiosflags(ios::right) << setw(12)<< *i;
        }
        myfile << endl;
    }
    
    if(! infor.empty()){
        auto i = infor.begin();
        auto k=v.begin();
        for(i = infor.begin(), k=v.begin(); i<infor.end();i++, k++){
            for(auto j=i->begin(); j<i->end();j++){
                myfile << setiosflags(ios::right) << setw(12)<< setprecision(4)<< *j;
            }
            myfile << setiosflags(ios::right) << setw(12)<< setprecision(4)<< *k << endl;           
        }
    }
    
    myfile.close();
    return 0;
}

int writeFile::write2dVectorToTable(const vector<vector<string> > &infor, const vector<vector<double> > &v, string fileName, vector<string> header){
    cout << "writing data to " << fileName << endl;
    
    ofstream myfile(fileName.c_str());
    
    if(! header.empty()){
        for(auto i = header.begin(); i<header.end();i++){
            myfile << setiosflags(ios::right) << setw(12)<< *i;
        }
        myfile << endl;
    }
    
    if(! infor.empty()){
        auto i = infor.begin();
        auto k=v.begin();
        for(i = infor.begin(), k=v.begin(); i<infor.end();i++, k++){
            for(auto j=i->begin(); j<i->end();j++){
                myfile << setiosflags(ios::right) << setw(12)<< setprecision(4)<< *j;
            }
            
            for(auto l = k->begin(); l < k->end(); l++){
                myfile << setiosflags(ios::right) << setw(12)<< setprecision(4)<< *l;
            }
            myfile << endl;           
        }
    }
    
    myfile.close();
    return 0;
}

////////////////////////////////////////////////////////
// append model
int writeFile::writeVectorToTable(const vector<vector<string> > &infor, const vector<double> &v, string fileName){
    ofstream myfile(fileName.c_str(), std::fstream::app);        
    
    if(! infor.empty()){
        auto i = infor.begin();
        auto k=v.begin();
        for(i = infor.begin(), k=v.begin(); i<infor.end();i++, k++){
            for(auto j=i->begin(); j<i->end();j++){
                myfile << setiosflags(ios::right) << setw(12)<< setprecision(4)<< *j;
            }
            myfile << setiosflags(ios::right) << setw(12)<< setprecision(4)<< *k << endl;           
        }
    }
    
    myfile.close();
    return 0;
}

int writeFile::write2dVectorToTable(const vector<vector<string> > &infor, const vector<vector<double> > &v, string fileName){
    ofstream myfile(fileName.c_str(),std::fstream::app);
    
    if(! infor.empty()){
        auto i = infor.begin();
        auto k=v.begin();
        for(i = infor.begin(), k=v.begin(); i<infor.end();i++, k++){
            for(auto j=i->begin(); j<i->end();j++){
                myfile << setiosflags(ios::right) << setw(12)<< setprecision(4)<< *j;
            }
            
            for(auto l = k->begin(); l < k->end(); l++){
                myfile << setiosflags(ios::right) << setw(12)<< setprecision(4)<< *l;
            }
            myfile << endl;           
        }
    }
    
    myfile.close();
    return 0;
}

