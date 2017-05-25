#pragma once

#include <cstring>
#include <iostream>

using namespace std;

class Logger
{
public:
	~Logger();

	static void I(string data);
	static void D(string data);
	static void E(string data);

	void i(string data);
	void d(string data);
	void e(string data);

	static Logger * getInstance();
	
private:
	void internalLog(string level, string data);
private:
	explicit Logger(string dir);
	explicit Logger(Logger &);
	explicit Logger(Logger *);
	string mDir;
	FILE * logFile;
	Logger * mLog;
};