
#pragma  once

namespace net
{
	
	
	class __declspec(dllexport) NetAPI 
	{
		

	public:
		NetAPI(const char * apiAddress, const char *  updateAddress);

		virtual bool testURL(const char * uri);

		virtual bool postHttpJson(const char * uri,  const char * header, const char * bodyData);

		virtual char * getVersion();


	private:
		char mAddr[1024];
		char mUpdateAddr[1024];
	};
};
