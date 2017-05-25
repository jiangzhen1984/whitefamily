#include <iostream>


#include "ms_database.h"
#include "net.h"

using namespace std;
using namespace db;
using namespace db::ms;
using namespace net;



void testDb()
{
	SP<Database>  pter = MsDatabase::createDatabase("Driver={Microsoft Access Driver (*.mdb, *.accdb)};DSN='';DBQ=C:\\Users\\28851274\\Documents\\test.accdb;PWD=123456");
	bool ret = pter->testConnection();

	::std::cout << " test connection : " << ret << endl;

	SP<Connection> pc = pter->createConnection();
	if (pc == NULL)
	{
		::std::cout << "open connection failed" << ::std::endl;
		return;
	}



	SP<ResultSet> sp = pc->execQuery(" SELECT * FROM a; ");

	{
		SP<ResultSet> sp1 = sp;


		int n = sp1->getRowCount();
		for (int i = 0; i < n; i++)
		{
			::std::cout << " Row " << i;
			SP<Row> sr = sp1->getRow(i);
			int fn = sr->getFieldCount();
			for (int j = 0; j < fn; j++)
			{

				::std::cout << "  " << (char *)sr->getField(j)->getValue();
			}
			::std::cout << endl;
		}

	}
	int n = sp->getRowCount();
	for (int i = 0; i < n; i++)
	{
		::std::cout << " Row " << i;
		SP<Row> sr = sp->getRow(i);
		int fn = sr->getFieldCount();
		for (int j = 0; j < fn; j++)
		{

			::std::cout << "  " << (char *)sr->getField(j)->getValue();
		}
		::std::cout << endl;
	}
	pc->close();
}


void testConn()
{
	NetAPI na("localhost", "");
	char * str = na.getVersion();
	bool ret = na.postHttpJson("/", "", "aaa");
	::std::cout << ret << ::std::endl;
}

int main(int argc, char ** argv)
{
	testDb();
	
	return 0;
}