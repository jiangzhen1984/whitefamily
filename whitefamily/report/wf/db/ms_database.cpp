#include "ms_database.h"
#include <iostream>


using namespace db;
using namespace db::ms;


#define URI_CPY(des, src) \
         if ((src) != NULL && *(src) != '\0') { \
			int n = strlen((src)) + 1;          \
			(des) = (char *)malloc(n);          \
			memset((des), 0, n);                \
			strncpy_s((des), n, (src), n);      \
         }

MsDatabase::MsDatabase() : mIsOpen(false), mUri(NULL), Ref()
{
}
MsDatabase::MsDatabase(const char * uri, char * user, char * pwd, int port) : mIsOpen(false), Ref()
{
	
	URI_CPY(mUri, uri)
	URI_CPY(mUserName, user)
	URI_CPY(mPwd, pwd)
	mPort = port;
	
}


MsDatabase::~MsDatabase()
{
	if (mUri != NULL)
	{
		free(mUri);
	}

	if (mUserName != NULL)
	{
		free(mUserName);
	}


	if (mPwd != NULL)
	{
		free(mPwd);
	}


}

bool MsDatabase::open()
{
	if (mUri == NULL || *mUri =='\0')
	{
		return false;
	}
	return true;
}

bool MsDatabase::open(char * uri)
{
	if (mUri != NULL)
	{
		free(mUri);
	}
	

	URI_CPY(mUri, uri)

	return this->open();
	
}

bool MsDatabase::testConnection()
{
	return false;
}

Connection * MsDatabase::getConnection()
{
	return NULL;
}
Connection * MsDatabase::createConnection()
{
	Connection * pConn = new MsConnection();
	bool ret = pConn->open(mUri, mUserName, mPwd);
	if (!ret)
	{
		delete pConn;
		return NULL;
	}
	return pConn;
}
bool MsDatabase::close()
{
	return false;
}


SP<Database > MsDatabase::createDatabase(const char * uri)
{
	Database * ptr = new MsDatabase(uri, "", "");
	ptr->open();
	SP<Database > sp(ptr);
	return sp;
}



MsConnection::MsConnection() :mIsOpen(false)
{
	RETCODE rc;

	rc = SQLAllocEnv(&mHEnv);
	if (!SQL_SUCCEEDED(rc))
	{
		//FIXME can not alloc
	}
	rc = SQLAllocConnect(mHEnv, &mMSConn);
	if (!SQL_SUCCEEDED(rc))
	{
		//FIXME can not alloc
	}
}

MsConnection::~MsConnection()
{
	SQLFreeHandle(SQL_HANDLE_DBC, mMSConn);
	SQLFreeHandle(SQL_HANDLE_ENV, mHEnv);	
}



bool MsConnection::open(char * uri, char * user, char * pwd, int port)
{
	char    szConnStrOut[256];
	int     iConnStrLength2Ptr;

	RETCODE rc = SQLDriverConnect(mMSConn, NULL, (unsigned char*)uri,
		SQL_NTS, (unsigned char*)szConnStrOut,
		255, (SQLSMALLINT*)&iConnStrLength2Ptr, SQL_DRIVER_NOPROMPT);

	if (SQL_SUCCEEDED(rc))
	{
		mIsOpen = true;
	}
	else
	{
		 ::std::cout << "open db : " << mIsOpen << szConnStrOut << " RET:" << rc << "  " << iConnStrLength2Ptr << ::std::endl;
	}
		
	return mIsOpen;
}


bool MsConnection::test()
{
	return false;
}


SP<ResultSet> MsConnection::execQuery(const char * sql)
{
	HSTMT       hStmt;
	RETCODE     rc;
	SQLINTEGER      rowCount = 0;
	SQLSMALLINT     fieldCount = 0, currentField = 0;
	::std::vector<Field*> vecf;
	MsResultSet * pSets= NULL;

	Field* pt = NULL;
	int n = 0;
	if (!this->mIsOpen)
	{
		return NULL;
	}

	rc = SQLAllocStmt(mMSConn, &hStmt);
	if (!SQL_SUCCEEDED(rc))
	{
		return NULL;
	}
	rc = SQLPrepare(hStmt, (unsigned char*) sql, SQL_NTS);
	if (!SQL_SUCCEEDED(rc))
	{
		return NULL;
	}

	int             ret1;
	int             ret2;

	

	rc = SQLExecute(hStmt);
	if (SQL_SUCCEEDED(rc))
	{
		
		SQLNumResultCols(hStmt, &fieldCount);
		
		if (fieldCount > 0)
		{
			for (currentField = 1; currentField <= fieldCount; currentField++)
			{
				
				
				MsField * fptr = new MsField();	
				SQLDescribeCol(hStmt, currentField,
					(SQLCHAR *)fptr->mName, sizeof(fptr->mName), 0, 0, 0, 0, 0);				
				vecf.push_back(fptr);
			}
			::std::cout <<  ::std::endl;
		}
		else
		{
			::std::cout << " file is 0" <<::std::endl;
		}

		int counts = vecf.size();
		n = 1;
		for (std::vector<Field*>::iterator it = vecf.begin(); it != vecf.end(); ++it)
		{
			rc = SQLBindCol(hStmt, n++, SQL_C_CHAR, (reinterpret_cast<MsField *>(*it))->mbind, 4096, (SQLINTEGER*)&ret1);
			
		}

		rc = SQLFetch(hStmt);
		pSets = new MsResultSet();
		while (SQL_SUCCEEDED(rc))
		{
			MsRow ms;
			for (std::vector<Field*>::iterator it = vecf.begin(); it != vecf.end(); ++it)
			{
				MsField * msptr(reinterpret_cast<MsField *>(*it));	
				int n = strlen((char *)msptr->mbind) + 1;
				char * pvoid = (char *)malloc(n);
				strncpy_s(pvoid, n,(char *)msptr->mbind, n);
				msptr->setValue(pvoid);
				ms.addField(*msptr);
			}

			
			pSets->addRow(ms);

			rc = SQLFetch(hStmt);
			rowCount++;
		};

		
		rc = SQLFreeStmt(hStmt, SQL_DROP);

		for (std::vector<Field*>::iterator it = vecf.begin(); it != vecf.end(); ++it)
		{
			delete *it;
		}

		vecf.clear();
	
	}

	if (pSets != NULL)
	{
		SP<ResultSet> sp(pSets);
		return sp;
	}
	
	return NULL;
}


int MsConnection::execUpdate(const char * sql)
{
	return 0;
}

bool MsConnection::close()
{
	if (mIsOpen)
	{
		SQLDisconnect(mMSConn);
	}
	//FIXME release connection
	
	return true;
}



SP<Row> MsResultSet::getRow(int idx)
{
	int n = mRows.size();
	if (n > idx && idx >= 0)
	{
		return mRows.at(idx);
	}
	else
	{
		return NULL;
	}
}


int MsResultSet::getRowCount()
{
	return mRows.size();
}

void MsResultSet::addRow(MsRow & f)
{
	MsRow * pr = new MsRow(f);
	mRows.push_back(pr);
}

MsRow & MsRow::operator= (MsRow * msr)
{
	mFields = msr->mFields;
	return *this;
}

SP<Field> MsRow::getField(int idx)
{
	
	int n = mFields.size();
	if (n > idx && idx >= 0)
	{
		return mFields.at(idx);
	}
	else
	{
		return NULL;
	}

}
int MsRow::getFieldCount()
{
	return mFields.size();
}


void MsRow::addField(MsField & f)
{
	Field * pf = new MsField(f);
	mFields.push_back(pf);
}


MsField::MsField()
{
	memset(mName, 0, sizeof(mName));
}
MsField::MsField(char * name, void * value) : mDataPtr(value)
{
	memset(mName, 0, sizeof(mName));
	memcpy(mName, name, strlen(name));
}



MsField::~MsField()
{
	if (mDataPtr != NULL)
	{
		delete mDataPtr;
	}
}

MsField & MsField::operator= (MsField & msf)
{
	memcpy(mbind, msf.mbind, strlen((const char *)msf.mbind));	
	this->mDataPtr = msf.mDataPtr;
	memcpy(mName, msf.mName, strlen(msf.mName));
	return *this;
}

MsField & MsField::operator = (MsField * msf)
{
	memcpy(mbind, msf->mbind, strlen((const char *)msf->mbind));
	this->mDataPtr = msf->mDataPtr;
	memcpy(mName, msf->mName, strlen(msf->mName));
	return *this;
}
MsField & MsField::operator = (MsField msf)
{
	memcpy(mbind, msf.mbind, strlen((const char *)msf.mbind));
	this->mDataPtr = msf.mDataPtr;
	memcpy(mName, msf.mName, strlen(msf.mName));
	return *this;
}


void * MsField::getValue()
{
	return this->mDataPtr;
}

char * MsField::getName()
{
	return this->mName;
}
void MsField::setValue(void * data)
{
	this->mDataPtr = data;
}
void MsField::setName(char * name)
{
	if (name != NULL)
	{
		memcpy(mName, name, strlen(name));
	}
}