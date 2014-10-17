/* 
 * File:   readData.cpp
 * Author: fan
 * 
 * Created on September 23, 2014, 4:24 PM
 */

#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <sstream>
#include <map>

#include "readData.h"

using namespace std;

readData::readData() {
}

readData::readData(const readData& orig) {
}

readData::~readData() {
}

vector<vector<string> > readData::readTable(const string fileName){
    /*
    read tped (PLINK format) data into a two dimensions vector
    each line of the tped file is one SNP
    each individual has two columns (A, C, G, T, 0, or -9)
    missing allele: 0 or -9
    */
    cout << "reading data from " << fileName << " ... ";
    ifstream myfile(fileName.c_str());
    vector<vector<string> > data;
    
    string temp;
    while(myfile){
        if (!getline(myfile,temp)) break;
        
        istringstream iss(temp);       
        vector <string> alleles;
        
        while(iss){
            string s;
            if (!getline(iss, s, ' ')) break;
            alleles.push_back(s);
        }
        data.push_back(alleles);
    }
    myfile.close();
    cout << "done" << endl;
    return data;
}

vector<vector<string> > readData::readTable(const string fileName, int start, int end){
    /*
    read tped (PLINK format) data into a two dimensions vector
    each line of the tped file is one SNP
    each individual has two columns (A, C, G, T, 0, or -9)
    missing allele: 0 or -9
    
    read from line 'start' to line 'end', include both start and end line
    start >= 1
    */
    
    ifstream myfile(fileName.c_str());
    vector<vector<string> > data;
    
    string temp;
    int count = 0;
    while(myfile){
        if (!getline(myfile,temp)) break;
        
        count++;
        if(count >= start && count <= end){
            istringstream iss(temp);       
            vector <string> alleles;
        
            while(iss){
                string s;
                if (!getline(iss, s, ' ')) break;
                alleles.push_back(s);
            }
        data.push_back(alleles);
        }else if(count > end){
            break;
        }        
    }
    myfile.close();
    return data;
}

string readData::getMajorAllele(const vector<string> &snpLine){
    /*
    find the major allele for each SNP
    */
    map<string, int> alleleCount={{"A",0},{"C",0},{"G",0},{"T",0}};  

    for(int i=0; i<snpLine.size();i++){
        if(snpLine[i] == "A" || snpLine[i] == "C" || snpLine[i] == "G" || snpLine[i] == "T"){
            alleleCount[snpLine[i]]++;
        }
    }
    
    string majorAllele = "";
    int temp;
    for(auto &kv : alleleCount){
        if(kv.second > 0){
            if(majorAllele == ""){
                majorAllele = kv.first;
                temp = kv.second;
            }else{
                if(kv.second > temp){
                    majorAllele = kv.first;
                }
            }
        }
    }
    
    return majorAllele;
}

vector<vector<int> > readData::codeGenotype(const vector<vector<string> > &snpLines){
    /*
    code the genotypes for each SNP
    homozygous of major allele was coded as 2
    heterozygous was coded as 1
    homozygous of minor allele was coded as 0
    missing data was coded as 9
    */
    vector<vector<int> > genoCode;
    for(int i=0; i<snpLines.size(); i++){
        string majorAllele = getMajorAllele(snpLines[i]);
        vector<int> line;
        for(int j=4; j< snpLines[i].size()-1; j+=2){
            int k = j + 1;
            if((snpLines[i][j]==majorAllele) && (snpLines[i][k]==majorAllele)){
                line.push_back(2);
            }else if((snpLines[i][j]==majorAllele) || (snpLines[i][k]==majorAllele)){
                line.push_back(1);
            }else if(((snpLines[i][j] != "0") && (snpLines[i][k]!= "0")) || ((snpLines[i][j]!="-9") && (snpLines[i][k]!="-9"))){
                line.push_back(0);
            }else{
                line.push_back(9);
            }
        }
        genoCode.push_back(line);
    }
    return genoCode;
}


vector<vector<int> > readData::rmMissingGenotype(const vector<int> &v1, const vector<int> &v2){
    /*
    remove individuals have missing genotype data in each window 
    */
    vector<vector<int> > genoTwoLoci;
    vector<int> t1;
    vector<int> t2;
    for(int i=0; i<v1.size(); i++){
        if(v1[i] != 9 && v2[i]!=9){
            t1.push_back(v1[i]);
            t2.push_back(v2[i]);
        }
    }
    genoTwoLoci.push_back(t1);
    genoTwoLoci.push_back(t2);
    return genoTwoLoci;
}
