#pragma once

#include "db.h"
#include <vector>
#include <windows.h>
#include <sqlext.h>


template class __declspec(dllexport) ::std::vector<::db::Row*>;
template class __declspec(dllexport) ::std::vector<::db::Field*>;

namespace db {
	namespace ms {

		class MsField;
		class MsRow;
		class MsResultSet;
		class MsConnection;
		class MsDatabase;

		class __declspec(dllexport) MsDatabase : public Database {
		public:
			explicit MsDatabase();
			explicit MsDatabase(const char * uri, char * user, char * pwd, int port = 0);
			virtual ~MsDatabase();

			virtual bool open();
			virtual bool open(char * uri);

			virtual bool testConnection();

			virtual SP<Connection> getConnection();
			virtual SP<Connection> createConnection();
			virtual bool close();
			static SP<Database > createDatabase(const char * uri);
		private:
			bool mIsOpen;
			char * mUri;
			char * mUserName;
			char * mPwd;
			int mPort;
			friend class MsConnection;
		};
	



		class __declspec(dllexport) MsConnection : public Connection {
		public:
			explicit MsConnection();
			virtual ~MsConnection();
			virtual bool open(char * uri, char * user, char * pwd, int port = 0);
			virtual bool test();
			virtual SP<ResultSet> execQuery(const char * sql);
			virtual int execUpdate(const char * sql);
			virtual bool close();
		private:
			bool    mIsOpen;
			SP<MsDatabase>  pdb;
			HENV    mHEnv;
			HDBC    mMSConn;
			friend class MsDatabase;
		};




		class __declspec(dllexport) MsResultSet : public ResultSet {
		public:
			virtual SP<Row> getRow(int idx);
			virtual int getRowCount();
			void addRow(::db::ms::MsRow & r);
		private:
			::std::vector<::db::Row*> mRows;
		};


		class __declspec(dllexport) MsRow : public Row {
		public:

			MsRow & operator = (MsRow * msr);

			virtual SP<Field> getField(int idx);
			virtual int getFieldCount();
			void addField(MsField & f);

		private:
			::std::vector<::db::Field*> mFields;
		};


		class __declspec(dllexport) MsField : public Field {
		public:
			MsField();
			MsField(char * name, void * value);
			~MsField();

			MsField & operator = (MsField & msf);
			MsField & operator = (MsField * msf);
			MsField & operator = (MsField msf);

			virtual void * getValue();
			virtual char * getName();
			void setValue(void * data);
			void setName(char * name);
		private:
			void *  mDataPtr;
			char   mName[1024];
			SQLCHAR mbind[4096];
			friend class MsConnection;
		};

	};

};