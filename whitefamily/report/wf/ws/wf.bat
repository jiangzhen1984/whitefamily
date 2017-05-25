@echo off
set FI=%~dp0

sc create wf start= auto binPath= %FI%ws.exe