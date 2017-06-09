#pragma once

#include <cstring>
#include <iostream>

using namespace std;

class Logger
{
public:
	~Logger();

	Logger & operator << (string str);
	Logger & operator << (const char *);
	Logger & operator << (char *);

	Logger & operator << (bool );
	Logger & operator << (int);
	Logger & operator << (float);
	Logger & operator << (double);
	Logger & operator << (char);


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