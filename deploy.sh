#!/bin/sh
build_mode=$1
ssl_sha=$2
server_url=$3
api_path=$4

gradle clean
gradle assemble${build_mode} -PServerUrl=${server_url} -PApiPath=${api_path} -PSslSHA=${ssl_sha}

adb uninstall com.example.weatherforecast
adb install app/build/outputs/apk/${build_mode}/app-${build_mode}.apk
adb shell am start -n "com.example.weatherforecast/com.example.weatherforecast.view.WeatherMainActivity" -a android.intent.action.MAIN -c android.intent.category.LAUNCHER