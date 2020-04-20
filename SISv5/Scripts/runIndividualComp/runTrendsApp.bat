@echo off
title TrendsApp

javac -sourcepath ../../Component/TrendsApp -cp ../../Components/* ../../Components/TrendsApp/*.java
pause

start "TrendsApp" /D"../../Components/TrendsApp" java -cp .;../* CreateTrendsApp