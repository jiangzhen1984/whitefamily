
#pragma warning (disable : 4996)
#include "Logger.h"
#include "db.h"
#include "MetaDS.h"




using namespace db;
int main(int argc, char ** argv) {
	
	char dbfileStr[1024];
	char nameFileStr[1024];
	//TODO read from conf file
	FILE * dbfile = NULL;
	FILE * nameFile = NULL;
	char buf[1024];
	int ret;
	::std::string strdb;
	::std::string strname;
	Connection * connptr = NULL;
	::std::vector<Incoming *> vec;

	memset(dbfileStr, 0, sizeof(dbfileStr));
	memset(nameFileStr, 0, sizeof(nameFileStr));
	memset(buf, 0, sizeof(buf));

	dbfile = fopen("t.conf", "r");
	if (dbfile == NULL)
	{
		Logger::E("Can't open db conf");
		goto cleanup;
	}
	ret = fread(buf, sizeof(char), sizeof(buf), dbfile);
	if (ret <= 0)
	{		
		Logger::E("Can't open db conf");
		goto cleanup;
	}
	strdb.append(&buf[4]);
	memset(buf, 0, sizeof(buf));


	nameFile = fopen("u.conf", "r");
	if (nameFile == NULL)
	{
		Logger::E("Can't open user conf");
		goto cleanup;
	}
	ret = fread(buf, sizeof(char), sizeof(buf), nameFile);
	if (ret <= 0)
	{
		Logger::E("Can't open db conf");
		goto cleanup;
	}
	strname.append(&buf[4]);
	 
	Logger::I(" DB : " + strdb + "  NAME:"+ strname);

	Report re;
	connptr =  re.openDatabase(strdb);
	ret = re.readIncoming(connptr, vec);
	for (std::vector<Incoming *>::iterator it = vec.begin(); it != vec.end(); ++it)
	{
		
		string data("Read Data:");
		data.append(std::to_string((*it)->getValue()));		
		Logger::I(data);
	}
	

	re.reportIncomings(vec);
	
cleanup:
	if (dbfile != NULL)
	{
		fclose(dbfile);
		dbfile = NULL;
	}
	if (nameFile != NULL)
	{
		fclose(nameFile);
		nameFile = NULL;
	}
	if (connptr != NULL)
	{
		connptr->close();
	}

	for (std::vector<Incoming *>::iterator it = vec.begin(); it != vec.end(); ++it)
	{
		delete *it;
	}

	return 0;
}

