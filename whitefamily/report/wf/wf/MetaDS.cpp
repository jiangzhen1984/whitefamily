#include "MetaDS.h"
#include "ms_database.h"
#include "Logger.h"
#include "net.h"


SP<Connection>  Report::openDatabase(string file)
{
	SP<Connection> pconn = NULL;
	Logger * l = Logger::getInstance();
	string dburl("Driver={Microsoft Access Driver (*.mdb, *.accdb)};DSN='';");
	dburl.append("DBQ=").append(file).append(";PWD=123456");
	{
		SP<Database>  pter = ::db::ms::MsDatabase::createDatabase(dburl.c_str());
		pconn = pter->createConnection();
	}
	*l << string("Open database success") << dburl;
	return pconn;
}

int Report::readIncoming(SP<Connection> spConn, vector<Incoming *>& vec)
{
	int n = 0, i;
	SP<Row> spr;
	Incoming * ptrIn = NULL;
	

	SP<ResultSet> sp = spConn->execQuery(" SELECT * FROM a; ");
	n = sp->getRowCount();
	for (i = 0; i < n; i++)
	{
		spr = sp->getRow(i);		
		ptrIn = new Incoming();
		ptrIn->setValue(atoi((const char *)spr->getField(3)->getValue()));
		vec.push_back(ptrIn);
	}
	return 0;
};


void Report::reportIncomings(vector<Incoming *>& vec)
{
	Logger *l = Logger::getInstance();
	::net::NetAPI api("", "");
	bool ret  = api.testURL("");
	
	*l << " Test url :" << ret;
	ret = api.postHttpJson("", "", "");
	*l << " post result :" << ret ;

};
