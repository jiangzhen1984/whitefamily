
#pragma warning (disable : 4996)
#include "Logger.h"

#include <direct.h>
#include <ctime>

static Logger * l;


Logger::Logger(string dir) : mDir(dir)
{
	
	int ret = mkdir(mDir.c_str());

	time_t rawtime;
	struct tm * timeinfo;
	char buffer[80];

	time(&rawtime);
	timeinfo = localtime(&rawtime);

	strftime(buffer, sizeof(buffer), "%Y_%m_%d", timeinfo);

	
	string logfile(dir + buffer+".txt");
	if (logFile == NULL)
	{
		logFile = fopen(logfile.c_str(), "a");
		if (logFile == NULL)
		{
			//FIXME write error file
		}
	}

	
}

Logger::~Logger()
{
	if (logFile != NULL)
	{
		fclose(logFile);
		logFile = NULL;
	}
}



Logger::Logger(Logger & logger) :mDir(logger.mDir)
{
	
}

Logger::Logger(Logger * logger)
{
	if (logger != NULL)
	{
		mDir = logger->mDir;
	}
}


void Logger::internalLog(string level, string data)
{
	if (logFile == NULL)
	{
		return;
	}

	time_t rawtime;
	struct tm * timeinfo;
	char buffer[80];

	time(&rawtime);
	timeinfo = localtime(&rawtime);

	strftime(buffer, sizeof(buffer), "%d-%m-%Y %I:%M:%S", timeinfo);
		
	fwrite(buffer, sizeof(char), strlen(buffer), logFile);
	fwrite(level.c_str(), sizeof(char), level.size(), logFile);
	fwrite(data.c_str(), sizeof(char),  data.size(), logFile);
	fwrite("\n", sizeof(char), 1, logFile);
}


void Logger::i(string data)
{
	internalLog(" [INFO] ", data);
}
void Logger::d(string data)
{
	internalLog(" [DEBUG] ", data);
}
void Logger::e(string data)
{
	internalLog(" [ERROR] ", data);
}



void Logger::I(string data)
{
	Logger::getInstance()->i(data);
}

void Logger::D(string data)
{
	Logger::getInstance()->d(data);
}


void Logger::E(string data)
{
	Logger::getInstance()->e(data);
}

Logger * Logger::getInstance()
{
	if (l == NULL)
	{
		l = new Logger("log/");
	}
	return l;
}
