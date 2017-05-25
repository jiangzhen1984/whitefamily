

#pragma warning (disable : 4996)
#include "MainWindow.h"
#include "ui_mainwindow.h"

#include <QWidget>
#include <QMessageBox>
#include <QFileDialog>
#include <QDebug>

#include "ms_database.h"
#include "net.h"

MainWindow::MainWindow(QWidget *parent) : QMainWindow(parent),
ui(new Ui::MainWindow)
{
	ui->setupUi(this);
	this->mDataAccessTestBtn   = ui->data_access_test_btn;
	this->mConnectionTestBtn   = ui->connection_test_btn;
	this->mWsTestBtn           = ui->ws_test_btn;
	this->mFileBrowserbtn      = ui->file_browser_btn;
	this->mDataFileEdit        = ui->data_file_edit;
	this->mCellphoneNumberEdit = ui->cellphone_number_edit;
	connect(this->mDataAccessTestBtn, SIGNAL(clicked()), this, SLOT(handleDataTestBtnClicked()));
	connect(this->mConnectionTestBtn, SIGNAL(clicked()), this, SLOT(handleConnectionTestBtnClicked()));
	connect(this->mWsTestBtn, SIGNAL(clicked()), this, SLOT(handleWsTestBtnClicked()));
	connect(this->mFileBrowserbtn, SIGNAL(clicked()), this, SLOT(handleFileBrowserBtnClicked()));
	
}


MainWindow::~MainWindow()
{
	if (ui != NULL)
	{
		delete ui;
	}
}


void MainWindow::handleDataTestBtnClicked()
{
	if (this->mDataFileEdit->text().size() <= 0)
	{
		return;
	}
	QString str = "Driver={Microsoft Access Driver (*.mdb, *.accdb)};DSN='';DBQ=" + this->mDataFileEdit->text() + ";PWD=123456";
	
	
	bool ret = false;
	{
		::db::SP<::db::Database>  pter = ::db::ms::MsDatabase::createDatabase((char *)str.toStdString().c_str());
		::db::SP<::db::Connection>  pconn = pter->createConnection();
		if (pconn != NULL)
		{
			ret = true;
			pconn->close();			
		}
		else
		{
			ret = false;
		}				
	}

	mTestDB = ret;

	QMessageBox msgBox;	
	if (ret)
	{
		QFile  file("t.conf");
		if (!file.open(QIODevice::WriteOnly | QIODevice::Text))
		{
			msgBox.setInformativeText(QStringLiteral("无法保存配置，请手动保存"));
		}
		else
		{
			
			QDataStream out(&file);

			out << this->mDataFileEdit->text().toLatin1();
			file.flush();
			file.close();
			msgBox.setInformativeText(QStringLiteral("连接成功并保存"));
		}
		
	}
	else
	{
		msgBox.setInformativeText(QStringLiteral("连接失败"));
	}
	
	msgBox.setStandardButtons(QMessageBox::Ok);
	msgBox.setDefaultButton(QMessageBox::Ok);
	msgBox.exec();	
}


void MainWindow::handleConnectionTestBtnClicked()
{

	::net::NetAPI api("localhost", "localhost");
	bool ret = api.testURL("/");


	QMessageBox msgBox;
	if (ret)
	{
		msgBox.setInformativeText(QStringLiteral("服务器连接成功"));	
		mTestAPI = true;

		QFile  file("u.conf");
		if (!file.open(QIODevice::WriteOnly | QIODevice::Text))
		{
			msgBox.setInformativeText(QStringLiteral("无法保存配置，请手动保存"));
		}
		else
		{

			QDataStream out(&file);

			out << this->mCellphoneNumberEdit->text().toLatin1();
			file.flush();
			file.close();
			msgBox.setInformativeText(QStringLiteral("测试成功并保存"));
		}
	}
	else
	{		
		msgBox.setInformativeText(QStringLiteral("服务器连接失败"));
	}
	
	msgBox.setStandardButtons(QMessageBox::Ok);
	msgBox.exec();

}
void MainWindow::handleWsTestBtnClicked()
{
	

	if (!mTestDB)
	{
		QMessageBox msgBox;
		msgBox.setText(QStringLiteral("请先测试数据连接"));
		msgBox.setDefaultButton(QMessageBox::Ok);
		msgBox.exec();
	}
	else if (!mTestAPI)
	{
		QMessageBox msgBox;
		msgBox.setText(QStringLiteral("请先测试网络连接连接"));
		msgBox.setDefaultButton(QMessageBox::Ok);
		msgBox.exec();
	}
	else
	{
		system("ws.exe /install");
		QMessageBox msgBox;
		msgBox.setText(QStringLiteral("注册服务成功"));
		msgBox.setDefaultButton(QMessageBox::Ok);
		msgBox.exec();
	}
}

void MainWindow::handleFileBrowserBtnClicked()
{
	QString fileName = QFileDialog::getOpenFileName(this);
	this->mDataFileEdit->setText(fileName);
}
