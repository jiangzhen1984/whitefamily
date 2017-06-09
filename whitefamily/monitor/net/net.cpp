#include "net.h"

#define VERSION "0.1"

#include <iostream>
#include <Windows.h>
#include <winhttp.h>

using namespace net;


static int internalSendData(LPCWSTR address, LPCWSTR uri, LPCWSTR method, LPCWSTR headers, DWORD header_lengh, LPVOID body, DWORD body_length, ::std::string & out);

NetAPI::NetAPI(const char * apiAddress, const char *  updateAddress)
{
	memset(mAddr, 0, sizeof(mAddr));
	memset(mUpdateAddr, 0, sizeof(mUpdateAddr));
	if (apiAddress != NULL &&  *apiAddress != '\0')
	{
		memcpy(mAddr, apiAddress, strlen(apiAddress));
	}

	if (updateAddress != NULL &&  *updateAddress != '\0')
	{
		memcpy(mUpdateAddr, updateAddress, strlen(updateAddress));
	}
}


bool NetAPI::testURL(const char * uri)
{
	WCHAR addrw[256];
	WCHAR uriw[256];
	::std::string out;
	memset(addrw, 0, 256);
	memset(uriw, 0, 256);

	MultiByteToWideChar(0, 0, mAddr, strlen(mAddr), addrw, strlen(mAddr));
	MultiByteToWideChar(0, 0, uri, strlen(uri), uriw, strlen(uri));

	int ret = internalSendData(addrw, uriw, L"GET", NULL, 0, NULL, 0, out);
	::std::cout << out.c_str() << ::std::endl;

	return ret == 0;	

}

bool NetAPI::postHttpJson(const char * uri, const char * header, const char * bodyData)
{
	WCHAR addrw[256];
	WCHAR uriw[256];
	::std::string out;
	memset(addrw, 0, 256);
	memset(uriw, 0, 256);
	MultiByteToWideChar(0, 0, mAddr, strlen(mAddr), addrw, strlen(mAddr));
	MultiByteToWideChar(0, 0, uri, strlen(uri), uriw, strlen(uri));
	
	int ret = internalSendData(addrw, uriw, L"GET", NULL, 0, NULL, 0, out);

	::std::cout << out.c_str() << ::std::endl;
	return true;

}

char * NetAPI::getVersion()
{
	return VERSION;
}


int internalSendData(LPCWSTR address, LPCWSTR uri, LPCWSTR method, LPCWSTR headers, DWORD header_lengh, LPVOID body, DWORD body_length, ::std::string & out)
{
	DWORD dwSize = 0;
	DWORD dwDownloaded = 0;
	LPSTR pszOutBuffer;
	BOOL  bResults = FALSE;
	HINTERNET  hSession = NULL,
		hConnect = NULL,
		hRequest = NULL;

	int ret = 0;


	// Use WinHttpOpen to obtain a session handle.
	hSession = WinHttpOpen(L"WinHTTP Example/1.0",
		WINHTTP_ACCESS_TYPE_DEFAULT_PROXY,
		WINHTTP_NO_PROXY_NAME,
		WINHTTP_NO_PROXY_BYPASS, 0);

	// Specify an HTTP server.
	if (hSession)
		hConnect = WinHttpConnect(hSession, address,
		INTERNET_DEFAULT_HTTP_PORT, 0);

	// Create an HTTP request handle.
	if (hConnect)
		hRequest = WinHttpOpenRequest(hConnect, method, uri,
		NULL, WINHTTP_NO_REFERER,
		WINHTTP_DEFAULT_ACCEPT_TYPES,
		0);

	// Send a request.
	if (hRequest)
		bResults = WinHttpSendRequest(hRequest,
		headers, 0,
		body, body_length,
		body_length, 0);


	// End the request.
	if (bResults)
		bResults = WinHttpReceiveResponse(hRequest, NULL);

	// Keep checking for data until there is nothing left.
	if (bResults)
	{
		do
		{
			// Check for available data.
			dwSize = 0;
			if (!WinHttpQueryDataAvailable(hRequest, &dwSize))
				printf("Error %u in WinHttpQueryDataAvailable.\n",
				GetLastError());

			// Allocate space for the buffer.
			pszOutBuffer = new char[dwSize + 1];
			if (!pszOutBuffer)
			{
				printf("Out of memory\n");
				dwSize = 0;
			}
			else
			{
				// Read the data.
				ZeroMemory(pszOutBuffer, dwSize + 1);

				if (!WinHttpReadData(hRequest, (LPVOID)pszOutBuffer,
					dwSize, &dwDownloaded))
				{
					ret =GetLastError();
				}
				else
				{
					out.append(pszOutBuffer);
				}					
				
				// Free the memory allocated to the buffer.
				delete[] pszOutBuffer;
			}
		} while (dwSize > 0);
	}


	// Report any errors.
	if (!bResults)
	{
		printf("Error %d has occurred.\n", GetLastError());
		ret = GetLastError();
	}
		

	// Close any open handles.
	if (hRequest) WinHttpCloseHandle(hRequest);
	if (hConnect) WinHttpCloseHandle(hConnect);
	if (hSession) WinHttpCloseHandle(hSession);

	return ret;
}

