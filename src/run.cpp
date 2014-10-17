/* 
 * File:   run.cpp
 * Author: fan
 * 
 * Created on October 7, 2014, 2:35 PM
 */

#include "run.h"

run::run() {
}

run::run(const run& orig) {
}

run::~run() {
}

void run::runQuantLDStep(string fileName1, string fileName2, int fPM, int windowSize, string method, int perm, string ldMeasure, string ldMethod,bool fisher, string outName){
    ifstream myfile(fileName1.c_str());
    int lines = 0;  
    string temp;
    while(myfile){
        if (!getline(myfile,temp)) break;
        lines++;
    }
    myfile.close(); 
    int maxlines = maxWin * windowSize;
    int iter = lines / maxlines + 1;
    
    int start, end;
    for(int zzz=0; zzz < iter; zzz++){        
        if(zzz==0){
            start = 1;
            end = maxlines;
        }else{
            start = end - windowSize + 1;
            end = maxlines + maxlines * zzz;
        }
        // read and code data  
        vector<vector<string> > data1 = rf.readTable(fileName1, start, end);
        vector<vector<int> > genoCode1 = rf.codeGenotype(data1);   
        vector<vector<string> > data2 = rf.readTable(fileName2, start, end);
        vector<vector<int> > genoCode2 = rf.codeGenotype(data2);

        // Window information
        vector<vector<string> > chrRSpos;
        for(int i=0; i < data1.size()-windowSize+1; i++){
            vector<string> temp;
            temp.push_back(data1[i][0]);        
            temp.push_back(data1[i][1]);
            temp.push_back(data1[i][3]);
            temp.push_back(data1[i+windowSize-1][1]);       
            temp.push_back(data1[i+windowSize-1][3]);
            chrRSpos.push_back(temp);
        }

        // calculate sample size
        auto t98 = genoCode1.begin();
        int n1 = t98->size();   
        auto t99 = genoCode2.begin();
        int n2 = t99->size();

        // calculate LD measure
        vector<vector<double> > fr, sr; 
        fr = myCalLD.calRD2(genoCode1, windowSize, ldMeasure, ldMethod);
        sr = myCalLD.calRD2(genoCode2, windowSize, ldMeasure, ldMethod);        
        
        // log
        if(zzz==0){
            cout << "read data from " << fileName1 << endl;
            cout << "there are " << n1 << " individuals and " << lines << " SNPs" << endl;
            cout << "read data from " << fileName2 << endl;
            cout << "there are " << n2 << " individuals and " << lines << " SNPs" << endl;
            if(ldMeasure == "r2"){
                cout << "calculate r squared" << endl;
                cout << "compare r squared matrices" << endl << endl;
            }else{
                cout << "calculate D prime" << endl;
                cout << "compare D prime matrices" << endl << endl;
            }            
        }
        
        // run the selected method
        vector<double> rtmtpevdstjt;
        vector<vector<double> > rtmtpevdstjtperm;
        vector<string> header;
        
        if(method == "tm"){
            if(fPM==0){
                rtmtpevdstjt= pwp.pwTMTPEVD(fr, sr, windowSize, method);
                header={"CHR", "SNP1", "POS1", "SNP2", "POS2", "Tmethod"};
                            
            }else{
                rtmtpevdstjtperm= pwp.pwTMTPEVDperm(fr, sr, genoCode1, genoCode2, windowSize, method, perm, n1, n2, ldMeasure,ldMethod);
                header={"CHR", "SNP1", "POS1", "SNP2", "POS2", "Tmethod", "TM.P"};                                 
            }    
        }else if(method == "tp"){
            if(fPM==0){
                rtmtpevdstjt= pwp.pwTMTPEVD(fr, sr, windowSize, method);
                header={"CHR", "SNP1", "POS1", "SNP2", "POS2", "Tpercent"};               
            }else{
                rtmtpevdstjtperm= pwp.pwTMTPEVDperm(fr, sr, genoCode1, genoCode2, windowSize, method, perm, n1, n2, ldMeasure,ldMethod);
                header={"CHR", "SNP1", "POS1", "SNP2", "POS2", "Tpercent", "TP.P"};                  
            }      
        }else if(method == "jt"){
            rtmtpevdstjtperm= pwp.pwSTJTALL(fr, sr, windowSize, method, n1, n2,fisher);
            header={"CHR", "SNP1", "POS1", "SNP2", "POS2", "JT.P", "JT.Chisq", "JT.df"};              
        }else if(method == "st"){        
            rtmtpevdstjtperm= pwp.pwSTJTALL(fr, sr, windowSize, method, n1, n2, fisher);
            header={"CHR", "SNP1", "POS1", "SNP2", "POS2", "ST.P", "ST.Chisq", "ST.df"};            
        }else if(method == "ev"){
            if(fPM==0){
                rtmtpevdstjt= pwp.pwTMTPEVD(fr, sr, windowSize, method);
                header={"CHR", "SNP1", "POS1", "SNP2", "POS2", "EVD"};                
            }else{
                rtmtpevdstjtperm = pwp.pwTMTPEVDperm(fr, sr, genoCode1, genoCode2, windowSize, method, perm, n1, n2, ldMeasure,ldMethod);
                header={"CHR", "SNP1", "POS1", "SNP2", "POS2", "EVD", "EVD.P"};                  
            }     
        }else{
            if(fPM==0){
                rtmtpevdstjtperm= pwp.pwSTJTALL(fr, sr, windowSize,method, n1, n2,fisher);
                header={"CHR", "SNP1", "POS1", "SNP2", "POS2", "ST.P", "ST.Chisq", "ST.df", "JT.P", "JT.Chisq", "JT.df", "Tmethod", "Tpercent", "EVD"};                
            }else{
                rtmtpevdstjtperm= pwp.pwAllPerm(fr, sr, genoCode1, genoCode2, windowSize, perm, n1, n2, ldMeasure,ldMethod,fisher);
                header={"CHR", "SNP1", "POS1", "SNP2", "POS2", "ST.P", "ST.Chisq", "ST.df", "JT.P", "JT.Chisq", "JT.df", "Tmethod", "Tpercent", "EVD", "TM.P", "TP.P", "EVD.P"};
            }

        }
        
        if(zzz==0){
            if(fPM==0 && (method=="tm" || method=="tp" || method=="ev")){
                wf.writeVectorToTable(chrRSpos, rtmtpevdstjt, outName, header);
            }else{
                wf.write2dVectorToTable(chrRSpos, rtmtpevdstjtperm, outName, header);
            }            
        }else{
            if(fPM==0 && (method=="tm" || method=="tp" || method=="ev")){
                wf.writeVectorToTable(chrRSpos, rtmtpevdstjt, outName);
            }else{
                wf.write2dVectorToTable(chrRSpos, rtmtpevdstjtperm, outName);
            }  
        }
        
    }
}
