@echo off
echo 正在修复 Gradle Wrapper...

REM 创建临时目录
mkdir temp_gradle 2>nul
cd temp_gradle

REM 下载 Gradle Wrapper JAR 文件
echo 正在下载 gradle-wrapper.jar...
curl -L -o gradle-wrapper.jar https://github.com/gradle/gradle/raw/v8.11.1/gradle/wrapper/gradle-wrapper.jar

REM 复制到正确位置
if exist gradle-wrapper.jar (
    copy gradle-wrapper.jar ..\gradle\wrapper\gradle-wrapper.jar
    echo Gradle Wrapper 修复完成！
) else (
    echo 下载失败，请检查网络连接
)

REM 清理临时文件
cd ..
rmdir /s /q temp_gradle

echo 现在可以运行 ./gradlew build 来构建项目
pause