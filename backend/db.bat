@echo off
:: Provides a shortcut to access the mybatis migration executable
set dirPath=%cd%


:: Set temporary variables
for /f %%i in (.env) do (
    set "%%i"
)

:: Directory jump
call cd .\\migrate\\repository

:: Execute command
call ..\\bin\\migrate %1 %2

call cd %dirPath%
