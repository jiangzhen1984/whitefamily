#pragma once
#include "ref.h"



namespace db{
	class ResultSet;
	class Connection;
	class Row;
	class Field;
	
	
	class __declspec(dllexport) Database : virtual public Ref {

	public:
		virtual bool open() = 0;
		virtual bool open(char * uri) = 0;

		virtual bool testConnection() = 0;

		virtual SP<Connection> getConnection() = 0;
		virtual SP<Connection> createConnection() = 0;
		virtual bool close() = 0;
		virtual ~Database(){};
	};


	class __declspec(dllexport) Connection : virtual public Ref {
	public:
		virtual bool   open(char * uri, char * user, char * pwd, int port = 0) = 0;
		virtual bool   test() = 0;
		virtual SP<::db::ResultSet> execQuery(const char * sql) = 0;
		virtual int    execUpdate(const char * sql) = 0;
		virtual bool   close() = 0;
		virtual ~Connection() {};
	};



	class __declspec(dllexport) ResultSet : virtual public Ref {
	public:
		virtual SP<Row> getRow(int idx) = 0;
		virtual int getRowCount() = 0;
		virtual ~ResultSet(){};
	};


	class __declspec(dllexport) Row : virtual public Ref {
	public:
		virtual SP<Field> getField(int idx) = 0;
		virtual int getFieldCount() = 0;
		virtual ~Row(){};
	};




	class __declspec(dllexport) Field : virtual public Ref {
	public:
		virtual void * getValue() = 0;
		virtual char * getName() = 0;
		virtual ~Field(){};
	};
}


