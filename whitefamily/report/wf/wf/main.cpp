
#pragma warning (disable : 4996)
#include "Logger.h"
#include "net.h"
#include "ms_database.h"



using namespace db;
int main(int argc, char ** argv) {

	int n, n1, i, j;
	SP<Row> spr;
	SP<Database>  pter = ::db::ms::MsDatabase::createDatabase("Driver={Microsoft Access Driver (*.mdb, *.accdb)};DSN='';DBQ=C:\\Users\\28851274\\Documents\\test.accdb;PWD=123456");
	Connection * pconn = pter->createConnection();
	if (pconn == NULL)
	{
		Logger::E("Open connection failed");
		return 0;
	}

	SP<ResultSet> sp = pconn->execQuery(" SELECT * FROM a; ");
	n = sp->getRowCount();
	for (i = 0; i < n; i++)
	{
		spr = sp->getRow(i);
		n1 = spr->getFieldCount();
		string data;
		for (j = 0; j < n1; j++)
		{
			
			data.append((char *)spr->getField(j)->getValue()).append("  ");
		}
		Logger::I(data);
	}

	pconn->close();

	//TODO upload data to API
	
	return 0;
}

