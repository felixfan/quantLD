/* 
 * File:   run.h
 * Author: fan
 *
 * Created on October 7, 2014, 2:35 PM
 */

#ifndef RUN_H
#define	RUN_H

#include "pairwisePops.h"
#include "writeFile.h"
#include "readData.h"
#include "calLD.h"
#include <fstream>

using namespace std;

class run {
public:
    run();
    run(const run& orig);
    virtual ~run();
    void runQuantLDStep(string fileName1, string fileName2, int fPM, int windowSize, string method, int perm, string ldMeasure, string ldMethod,bool fisher, string outName);   
private:
    pairwisePops pwp;
    writeFile wf;
    readData rf;
    calLD myCalLD;
    int maxWin = 100; // maxLines = maxWin * windowSize
};

#endif	/* RUN_H */

