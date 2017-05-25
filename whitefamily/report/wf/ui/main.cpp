
#include "MainWindow.h"
#include <QApplication>
#include <QTextCodec>

#ifdef _DEBUG
int main(int argc, char ** argv)
#else
int WinMain(HINSTANCE hInstance, HINSTANCE hPrevInstance, char*, int nShowCmd)
#endif
{
#ifdef _DEBUG
	
#else
	int argc = 0;
#endif
	QTextCodec::setCodecForLocale(QTextCodec::codecForName("UTF-8"));

	QApplication a(argc, 0);
	MainWindow w;
	w.show();

	return a.exec();
}
