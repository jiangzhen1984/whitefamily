#pragma once
#include <QMainWindow>
#include <QWidget>
#include <QPushButton>
#include <QLineEdit>

namespace Ui {
	class MainWindow;
}


class MainWindow :public QMainWindow
{

	Q_OBJECT

public:
	explicit MainWindow(QWidget *parent = 0);
	~MainWindow();

private slots:
	void handleDataTestBtnClicked();
	void handleConnectionTestBtnClicked();
	void handleWsTestBtnClicked();
	void handleFileBrowserBtnClicked();

private:
	Ui::MainWindow *ui;
	QPushButton * mDataAccessTestBtn;
	QPushButton * mConnectionTestBtn;
	QPushButton * mWsTestBtn;	
	QPushButton * mFileBrowserbtn;
	QLineEdit   * mDataFileEdit;
	QLineEdit   * mCellphoneNumberEdit;
	bool   mTestDB;
	bool   mTestAPI;

};

