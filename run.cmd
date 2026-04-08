@ECHO OFF
SETLOCAL ENABLEDELAYEDEXPANSION

:: Verifica se h  parƒmetros, se nÆo houver, mostra o help
IF "%~1"=="" GOTO help
IF /I "%~1"=="/?" GOTO help
IF /I "%~1"=="--help" GOTO help

:: Encaminhamento de comandos
IF /I "%~1"=="clean" GOTO clean
IF /I "%~1"=="app" GOTO app
IF /I "%~1"=="test" GOTO test
IF /I "%~1"=="mutation" GOTO mutation

GOTO help

:help
ECHO.
ECHO Utilitario de execucao para Java/Spring Boot
ECHO.
ECHO Uso:
ECHO %~0 [clean] [test] [app] [mutation]
ECHO.
ECHO Lista de Parametros:
ECHO     clean      Remove diretorios target e limpa o repositorio local m2.
ECHO     test       Executa os testes unitarios do projeto.
ECHO     app        Executa a aplicacao Spring Boot (perfil test).
ECHO     mutation   Executa testes de mutacao usando Pitest. 
GOTO end

:clean
ECHO.
ECHO Removendo arquivos binarios e temporarios... 
:: Remove pastas target recursivamente
FOR /f %%i IN ('DIR target /s /b') DO RD /s /q %%i
:: Limpa dependencias especificas do walletservice no m2
FOR /f %%i in ('DIR %HOME%\.m2\repository\io\github\jtsato\walletservice* /b') do RD /s /q %%i
CLS
ECHO Limpeza concluida.

:: Permite encadeamento (ex: run clean test)
IF /I "%~2"=="test" GOTO test
IF /I "%~2"=="app" GOTO app
IF /I "%~2"=="mutation" GOTO mutation
GOTO end

:test
ECHO.
ECHO Executando testes unitarios...
:: Compila e instala pulando testes inicialmente para garantir o ambiente
CALL mvn -e clean compile
CALL mvn -e clean install -Dmaven.test.skip=true
:: Executa os testes com o perfil ativo
CALL mvn -e clean test -Dspring.profiles.active=test
GOTO end

:app
ECHO.
ECHO Iniciando a aplicacao...
:: Limpa o m2 e instala dependencias antes de rodar
FOR /f %%i in ('DIR %HOME%\.m2\repository\io\github\jtsato\walletservice* /b') do RD /s /q %%i
CALL mvn -e clean install -Dmaven.test.skip=true
:: Executa o spring-boot:run apontando para o diretorio de configuracao
CALL mvn spring-boot:run -Dspring-boot.run.jvmArguments='-Dspring.profiles.active=test' -f ./configuration
GOTO end

:mutation
ECHO.
ECHO Iniciando testes de mutacao (Pitest)...
:: Prepara o ambiente
FOR /f %%i in ('DIR %HOME%\.m2\repository\io\github\jtsato\walletservice* /b') do RD /s /q %%i
CALL mvn -e clean compile
CALL mvn -e clean install -Dmaven.test.skip=true
:: Executa a cobertura de mutacao
CALL mvn -e clean install org.pitest:pitest-maven:mutationCoverage
GOTO end

:end
SETLOCAL DISABLEDELAYEDEXPANSION
