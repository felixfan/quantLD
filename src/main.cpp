#include <cstdlib>
#include <getopt.h>
#include <stdio.h>
#include <string>
#include <sstream>
#include <iostream>
#include <vector>
#include <ctime>
#include <fstream>

#include "run.h"

using namespace std;

void usage(){
    printf("  usage:\n \
     -f [--file1]:           first input file name \n \
     -s [--file2]:           second input file name \n \
     -w [--window-size]:     number of SNPs included in each window \n \
     -l [--ld-measure]:      measure of linkage disequlibrium (r2, dp) \n \
     -c [--ld-method]:       method to calculate r2 or dp (esem, rh)\n \
     -m [--method]:          method will be used (tm, tp, st, jt, ev, all) \n \
     -p [--perm]:            number of permutation \n \
     -o [--out]:             prefix of output \n \
     -a [--fisher]:          Fisher z transform \n \
     -h [--help]:            help, usage\n");
}

int main(int argc, char *argv[]) {
    clock_t start = clock();
    printf("\n \
@-------------------------------------------------------------@\n \
|        quantLD        |      v 1.2      |    Oct 2014       |\n \
|-------------------------------------------------------------|\n \
|  (C) 2014 Felix Yanhui Fan, GNU General Public License, v2  |\n \
|-------------------------------------------------------------|\n \
|    For documentation, citation & bug-report instructions:   |\n \
|        http://felixfan.github.io/quantLD                    |\n \
@-------------------------------------------------------------@\n \
\n\n");
    
    // no parameter, print usage
    if(argc==1){
        usage();
            return -1;
    }
    
    const struct option long_options[] ={
        {"help",               no_argument,        0, 'h'},
        {"file1",              required_argument,  0, 'f'},
        {"file2",              required_argument,  0, 's'},
        {"window-size",        required_argument,  0, 'w'},
        {"ld-measure",         required_argument,  0, 'l'},
        {"ld-method",          required_argument,  0, 'c'},
        {"method",             required_argument,  0, 'm'},
        {"perm",               required_argument,  0, 'p'},
        {"out",                required_argument,  0, 'o'},
        {"fisher",             required_argument,  0, 'a'},
        {0,0,0,0}
        };
    //turn off getopt error message
    opterr=0;

    string fileName1, fileName2, windowSize, ldMeasure, ldMethod, method, perm, outName, fisher, logName;
    int fFN1, fFN2, fWS, fLDMS, fLDMT, fMD, fPM, fON, fFisher;
    fFN1=fFN2=fWS=fLDMS=fLDMT=fMD=fPM=fON=fFisher=0;
    
    int opts;
    while((opts=getopt_long(argc, argv, ":hf:s:w:l:c:m:p:o:a:", long_options, NULL))!=-1){
        switch(opts){        
        case 'h':
            usage();
            return -1;
        case 'f':
            fileName1 = optarg;
            fFN1=1;
            if(fileName1.find_first_of('-') ==0){
                    cout << "argument is missing: -f(--file1)" << endl;
                    usage();
                    return -1;
            }
            break;
        case 's':
            fileName2 = optarg;
            fFN2=1;
            if(fileName2.find_first_of('-') ==0){
                    cout << "argument is missing: -s(--file2)" << endl;
                    usage();
                    return -1;
            }
            break;
        case 'w':
            windowSize=optarg;
            fWS=1;
            if(windowSize.find_first_of('-') ==0){
                    cout << "argument is missing: -w(--window-size)" << endl;
                    usage();
                    return -1;
            }else if (windowSize.find_first_not_of("0123456789") != string::npos){
                cout << "argument of -w(--window-size) should be numeric" << endl;
                return -1;
            }
            break;
        case 'l':
            ldMeasure = optarg;
            fLDMS=1;
            if(ldMeasure.find_first_of('-') ==0){
                    cout << "argument is missing: -l(--ld-measure)" << endl;
                    usage();
                    return -1;
            }
            break;
        case 'c':
            ldMethod = optarg;
            fLDMT=1;
            if(ldMethod.find_first_of('-') ==0){
                    cout << "argument is missing: -l(--ld-method)" << endl;
                    usage();
                    return -1;
            }
            break;
        case 'm':
            method = optarg;
            fMD=1;
            if(method.find_first_of('-') ==0){
                    cout << "argument is missing: -m(--method)" << endl;
                    usage();
                    return -1;
            }
            break;
        case 'p':
            perm = optarg;
            fPM=1;
            if(perm.find_first_of('-') ==0){
                    cout << "argument is missing: -p(--perm)" << endl;
                    usage();
                    return -1;
            }else if (perm.find_first_not_of("0123456789") != string::npos){
                cout << "argument of -p(--perm) should be numeric" << endl;
                return -1;
            }
            break;
        case 'o':
            outName = optarg;
            fON=1;
            if(outName.find_first_of('-') ==0){
                cout << "argument is missing: -o(--out)" << endl;
                usage();
                return -1;
            }
            break;
        case 'a':
            fisher = optarg;
            fFisher=1;
            if(fisher.find_first_of('-') ==0){
                cout << "argument is missing: -a(--fisher)" << endl;
                usage();
                return -1;
            }
            break;
        case '?':
            cout << "unknown option: " << argv[--optind] << endl;
            usage();
            return -1;
        case ':':
            cout << "The option takes an argument which is missing" << endl;
            usage();
            return -1;
        }
    }

   
    // check input file
    if(fFN1==0){
            cout << "option -f(--file1) is missing" << endl;
            usage();
            return -1;
    }
    if(fFN2==0){
            cout << "option -s(--file2) is missing" << endl;
            usage();
            return -1;
    }

    // check window-size
    int myWindowSize;
    if(fWS==0){
        myWindowSize=50;  // default  
    }else{
        istringstream converter(windowSize);
        converter >> myWindowSize; 
    }
    
    // check method
    // tm: T method; tp: T percent; st: Steiger Test, jt: Jennrich Test
    // ev: eigen value; all: perform all test at the same time
    if(fMD==0){
        method = "tm"; // default
    }else if(method != "tm" && method != "tp" && method != "st" && method != "jt" && method != "ev" && method != "all"){
        cout << "method must be one of: tm, tp, st, jt, ev, all" << endl;
        usage();
        return -1;
    }
    
    // check LD measure
    if(fLDMS==0){
        ldMeasure = "r2"; // default
    }else if(ldMeasure != "r2" && ldMeasure !="dp"){
        cout << "ld measure must be one of: r2, dp" << endl;
        usage();
        return -1;
    }
    
    // check LD method
    if(fLDMT==0){
        ldMethod = "esem"; // default
    }else if(ldMethod != "rh" && ldMethod != "esem"){
        cout << "ld method must be one of: rh, esem" << endl;
        usage();
        return -1;
    }
    
    // check perm
    int myPerm;
    if(fPM==1) {
        istringstream converter(perm);
        converter >> myPerm; 
    }
     
    // check prefix of output
    if(fON==0){
        outName="quantLD.txt";
        logName="quantLD.log";
    }else{
        logName = outName + ".log";
        outName += ".txt";   
    }
    
    // check fisher
    bool myFisher;
    if(fFisher==1){
        if(fisher.at(0)== 'T' || fisher.at(0)== 't'){
            myFisher = true;
        }else if(fisher.at(0)== 'F' || fisher.at(0)== 'f'){
            myFisher = false;
        }else{
            cout << "fisher must be true or false!" << endl;
            usage();
            return -1;
        }
            
    }else{
        myFisher = true; // default
        fisher = "true";
    }
    cout << "  Options in effect:" << endl;
    cout << "    --file1 " << fileName1 << endl;
    cout << "    --file2 " << fileName2 << endl;
    cout << "    --window-size " << myWindowSize << endl;
    cout << "    --ld-measure " << ldMeasure << endl;
    cout << "    --ld-method " << ldMethod << endl;
    cout << "    --method " << method << endl;
    cout << "    --fisher " << fisher << endl;
    if(fPM==1){
        cout << "    --perm " << myPerm << endl;
    }
    cout << "    --out " << outName << endl << endl;
    
    ////////////////////////////////////////////////////////////////////////////
    // log
    ofstream logfile(logName);
    logfile << "@-------------------------------------------------------------@" << endl;
    logfile << "|        quantLD        |      v 1.2      |    Oct 2014       |" << endl;
    logfile << "|-------------------------------------------------------------|" << endl;
    logfile << "|  (C) 2014 Felix Yanhui Fan, GNU General Public License, v2  |" << endl;
    logfile << "|-------------------------------------------------------------|" << endl;
    logfile << "|    For documentation, citation & bug-report instructions:   |" << endl;
    logfile << "|        http://felixfan.github.io/quantLD                    |" << endl;
    logfile << "@-------------------------------------------------------------@" << endl;
    logfile << endl;
    logfile << "  Options in effect:" << endl;
    logfile << "    --file1 " << fileName1 << endl;
    logfile << "    --file2 " << fileName2 << endl;
    logfile << "    --window-size " << myWindowSize << endl;
    logfile << "    --ld-measure " << ldMeasure << endl;
    logfile << "    --ld-method " << ldMethod << endl;
    logfile << "    --method " << method << endl;
    logfile << "    --fisher " << fisher << endl;
    if(fPM==1){
        logfile << "    --perm " << myPerm << endl;
    }
    logfile << "    --out " << outName << endl << endl;
    
    // run the selected method
    // write the results to the output file 
    run myrun;
    myrun.runQuantLDStep(fileName1, fileName2, fPM, myWindowSize, method, myPerm, ldMeasure, ldMethod, myFisher, outName);
    
    // run time
    clock_t end = clock();
    clock_t diff =  (end - start) / CLOCKS_PER_SEC;
    int sec = (int) diff;
    int hour = sec/3600;
    sec %= 3600;
    int minute = sec/60;
    sec %= 60;
    cout << endl << "CPU time used: " << hour << " hours ";
    cout << minute << " minutes " << sec << " seconds" << endl;
    
    time_t now = time(0); // current date/time based on current system   
    string dt = ctime(&now); // convert now to string form
    cout << dt << endl;
    
    //log
    logfile << "write results to " << outName << endl;
    logfile << endl << "CPU time used: " << hour << " hours ";
    logfile << minute << " minutes " << sec << " seconds" << endl;
    logfile << dt << endl;
    logfile.close();
    
    return 0;
}

