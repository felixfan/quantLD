/* 
 * File:   readData.h
 * Author: fan
 *
 * Created on September 23, 2014, 4:24 PM
 */

#ifndef READDATA_H
#define	READDATA_H

using namespace std;

class readData {
public:
    readData();
    readData(const readData& orig);
    virtual ~readData();
    vector<vector<string> > readTable(const string fileName); // read all lines 
    vector<vector<string> > readTable(const string fileName, int start, int end); // read lines from start to end
    string getMajorAllele(const vector<string> &snpLine);
    vector<vector<int> > codeGenotype(const vector<vector<string> > &snpLines);
    vector<vector<int> > rmMissingGenotype(const vector<int> &v1, const vector<int> &v2);
private:

};

#endif	/* READDATA_H */

