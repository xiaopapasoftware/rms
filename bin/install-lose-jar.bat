@echo off
echo.
echo [信息] 安装中央仓库缺失jar。
echo.
rem pause
rem echo.

call mvn install:install-file -Dfile=../lib/analyzer-2012_u6.jar -DgroupId=org.wltea -DartifactId=analyzer -Dversion=2012_u6 -Dpackaging=jar  -DlocalRepositoryPath=D:\m2\repository
call mvn install:install-file -Dfile=../lib/apache-ant-zip-2.3.jar -DgroupId=com.ckfinder -DartifactId=apache-ant-zip -Dversion=2.3 -Dpackaging=jar -DlocalRepositoryPath=D:\m2\repository
call mvn install:install-file -Dfile=../lib/ckfinder-2.3.jar -DgroupId=com.ckfinder -DartifactId=ckfinder -Dversion=2.3 -Dpackaging=jar -DlocalRepositoryPath=D:\m2\repository
call  mvn install:install-file -Dfile=../lib/ckfinderplugin-fileeditor-2.3.jar -DgroupId=com.ckfinder -DartifactId=ckfinderplugin-fileeditor -Dversion=2.3 -Dpackaging=jar -DlocalRepositoryPath=D:\m2\repository
call mvn install:install-file -Dfile=../lib/ckfinderplugin-imageresize-2.3.jar -DgroupId=com.ckfinder -DartifactId=ckfinderplugin-imageresize -Dversion=2.3 -Dpackaging=jar -DlocalRepositoryPath=D:\m2\repository
call mvn install:install-file -Dfile=../lib/UserAgentUtils-1.13.jar -DgroupId=bitwalker -DartifactId=UserAgentUtils -Dversion=1.13 -Dpackaging=jar -DlocalRepositoryPath=D:\m2\repository

pause