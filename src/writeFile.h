/* 
 * File:   writeFile.h
 * Author: fan
 *
 * Created on September 23, 2014, 9:49 AM
 */

#ifndef WRITEFILE_H
#define	WRITEFILE_H

using namespace std;

class writeFile {
public:
    writeFile();
    writeFile(const writeFile& orig);
    virtual ~writeFile();
    int writeVectorToTable(const vector<vector<string> > &infor, const vector<double> &v, string fileName, vector<string> header);
    int write2dVectorToTable(const vector<vector<string> > &infor, const vector<vector<double> > &v, string fileName, vector<string> header);
    int writeVectorToTable(const vector<vector<string> > &infor, const vector<double> &v, string fileName); // append model
    int write2dVectorToTable(const vector<vector<string> > &infor, const vector<vector<double> > &v, string fileName); // append model
private:

};

#endif	/* WRITEFILE_H */

