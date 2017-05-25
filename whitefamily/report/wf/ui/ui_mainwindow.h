/********************************************************************************
** Form generated from reading UI file 'mainwindow.ui'
**
** Created by: Qt User Interface Compiler version 5.6.2
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAINWINDOW_H
#define UI_MAINWINDOW_H

#include <QtCore/QVariant>
#include <QtWidgets/QAction>
#include <QtWidgets/QApplication>
#include <QtWidgets/QButtonGroup>
#include <QtWidgets/QGroupBox>
#include <QtWidgets/QHBoxLayout>
#include <QtWidgets/QHeaderView>
#include <QtWidgets/QLabel>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QToolBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QWidget *centralWidget;
    QGroupBox *groupBox;
    QLabel *label;
    QLineEdit *data_file_edit;
    QPushButton *file_browser_btn;
    QLabel *label_2;
    QLineEdit *cellphone_number_edit;
    QWidget *horizontalLayoutWidget_3;
    QHBoxLayout *horizontalLayout_4;
    QPushButton *data_access_test_btn;
    QPushButton *connection_test_btn;
    QPushButton *ws_test_btn;
    QMenuBar *menuBar;
    QToolBar *mainToolBar;
    QStatusBar *statusBar;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName(QStringLiteral("MainWindow"));
        MainWindow->setWindowModality(Qt::ApplicationModal);
        MainWindow->resize(435, 336);
        centralWidget = new QWidget(MainWindow);
        centralWidget->setObjectName(QStringLiteral("centralWidget"));
        centralWidget->setMaximumSize(QSize(435, 16777215));
        groupBox = new QGroupBox(centralWidget);
        groupBox->setObjectName(QStringLiteral("groupBox"));
        groupBox->setGeometry(QRect(20, 30, 391, 151));
        label = new QLabel(groupBox);
        label->setObjectName(QStringLiteral("label"));
        label->setEnabled(true);
        label->setGeometry(QRect(20, 30, 50, 20));
        QSizePolicy sizePolicy(QSizePolicy::Fixed, QSizePolicy::Preferred);
        sizePolicy.setHorizontalStretch(0);
        sizePolicy.setVerticalStretch(0);
        sizePolicy.setHeightForWidth(label->sizePolicy().hasHeightForWidth());
        label->setSizePolicy(sizePolicy);
        label->setMinimumSize(QSize(50, 0));
        label->setMaximumSize(QSize(50, 16777215));
        label->setAlignment(Qt::AlignRight|Qt::AlignTrailing|Qt::AlignVCenter);
        data_file_edit = new QLineEdit(groupBox);
        data_file_edit->setObjectName(QStringLiteral("data_file_edit"));
        data_file_edit->setGeometry(QRect(90, 26, 200, 30));
        QSizePolicy sizePolicy1(QSizePolicy::Fixed, QSizePolicy::Fixed);
        sizePolicy1.setHorizontalStretch(0);
        sizePolicy1.setVerticalStretch(0);
        sizePolicy1.setHeightForWidth(data_file_edit->sizePolicy().hasHeightForWidth());
        data_file_edit->setSizePolicy(sizePolicy1);
        data_file_edit->setMinimumSize(QSize(200, 0));
        data_file_edit->setMaximumSize(QSize(200, 16777215));
        file_browser_btn = new QPushButton(groupBox);
        file_browser_btn->setObjectName(QStringLiteral("file_browser_btn"));
        file_browser_btn->setGeometry(QRect(302, 30, 80, 23));
        file_browser_btn->setMinimumSize(QSize(80, 0));
        file_browser_btn->setMaximumSize(QSize(60, 16777215));
        label_2 = new QLabel(groupBox);
        label_2->setObjectName(QStringLiteral("label_2"));
        label_2->setGeometry(QRect(20, 80, 50, 20));
        label_2->setMinimumSize(QSize(50, 0));
        label_2->setLayoutDirection(Qt::LeftToRight);
        label_2->setAlignment(Qt::AlignRight|Qt::AlignTrailing|Qt::AlignVCenter);
        cellphone_number_edit = new QLineEdit(groupBox);
        cellphone_number_edit->setObjectName(QStringLiteral("cellphone_number_edit"));
        cellphone_number_edit->setGeometry(QRect(90, 75, 200, 30));
        sizePolicy1.setHeightForWidth(cellphone_number_edit->sizePolicy().hasHeightForWidth());
        cellphone_number_edit->setSizePolicy(sizePolicy1);
        cellphone_number_edit->setMinimumSize(QSize(200, 0));
        cellphone_number_edit->setMaximumSize(QSize(300, 16777215));
        horizontalLayoutWidget_3 = new QWidget(centralWidget);
        horizontalLayoutWidget_3->setObjectName(QStringLiteral("horizontalLayoutWidget_3"));
        horizontalLayoutWidget_3->setGeometry(QRect(20, 190, 391, 71));
        horizontalLayout_4 = new QHBoxLayout(horizontalLayoutWidget_3);
        horizontalLayout_4->setSpacing(6);
        horizontalLayout_4->setContentsMargins(11, 11, 11, 11);
        horizontalLayout_4->setObjectName(QStringLiteral("horizontalLayout_4"));
        horizontalLayout_4->setContentsMargins(0, 0, 0, 0);
        data_access_test_btn = new QPushButton(horizontalLayoutWidget_3);
        data_access_test_btn->setObjectName(QStringLiteral("data_access_test_btn"));

        horizontalLayout_4->addWidget(data_access_test_btn);

        connection_test_btn = new QPushButton(horizontalLayoutWidget_3);
        connection_test_btn->setObjectName(QStringLiteral("connection_test_btn"));

        horizontalLayout_4->addWidget(connection_test_btn);

        ws_test_btn = new QPushButton(horizontalLayoutWidget_3);
        ws_test_btn->setObjectName(QStringLiteral("ws_test_btn"));

        horizontalLayout_4->addWidget(ws_test_btn);

        MainWindow->setCentralWidget(centralWidget);
        menuBar = new QMenuBar(MainWindow);
        menuBar->setObjectName(QStringLiteral("menuBar"));
        menuBar->setGeometry(QRect(0, 0, 435, 21));
        MainWindow->setMenuBar(menuBar);
        mainToolBar = new QToolBar(MainWindow);
        mainToolBar->setObjectName(QStringLiteral("mainToolBar"));
        MainWindow->addToolBar(Qt::TopToolBarArea, mainToolBar);
        statusBar = new QStatusBar(MainWindow);
        statusBar->setObjectName(QStringLiteral("statusBar"));
        MainWindow->setStatusBar(statusBar);

        retranslateUi(MainWindow);
        QObject::connect(data_access_test_btn, SIGNAL(clicked()), MainWindow, SLOT(raise()));

        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QApplication::translate("MainWindow", "MainWindow", 0));
        groupBox->setTitle(QApplication::translate("MainWindow", "\350\256\276\347\275\256", 0));
        label->setText(QApplication::translate("MainWindow", "\346\225\260\346\215\256\346\226\207\344\273\266", 0));
        file_browser_btn->setText(QApplication::translate("MainWindow", "\351\200\211\346\213\251\346\225\260\346\215\256\346\226\207\344\273\266", 0));
        label_2->setText(QApplication::translate("MainWindow", "\350\264\246\345\217\267", 0));
        data_access_test_btn->setText(QApplication::translate("MainWindow", "\346\265\213\350\257\225\346\225\260\346\215\256\346\226\207\344\273\266\350\256\277\351\227\256", 0));
        connection_test_btn->setText(QApplication::translate("MainWindow", "\346\265\213\350\257\225\350\264\246\345\217\267\350\277\236\346\216\245", 0));
        ws_test_btn->setText(QApplication::translate("MainWindow", "\346\263\250\345\206\214\346\234\215\345\212\241", 0));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAINWINDOW_H
