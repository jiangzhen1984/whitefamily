#pragma once
#include <string>
#include <vector>
#include "db.h"

using namespace std;
using namespace db;

class Incoming
{
public:
	Incoming(){};
	Incoming(string id, int type, double value) : mId(id), mType(type), mValue(value){};


	string getId(){ return mId; };
	void   setId(string id) { mId = id; };
	int getType(){ return mType; };
	void   setType(int type) { mType = type; };
	double getValue(){ return mValue; };
	void   setValue(double value) { mValue = value; };

private:
	string mId;
	int    mType;
	double mValue;	
};



class Report
{
public:	
	SP<Connection>  openDatabase(string file);
	int readIncoming(SP<Connection> spConn, vector<Incoming *>&);
	void reportIncomings(vector<Incoming *>& vec);
};