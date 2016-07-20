package com.softdesign.devintensive.utils;

/**
 * Created by Иван on 11.07.2016.
 */
public interface AppConfig {

    String BASE_URL = "http://devintensive.softdesign-apps.ru/api/";
    long MAX_CONNECT_TIMEOUT = 1000 * 30;
    long MAX_READ_TIMEOUT = 1000 * 30;
    long START_DELAY = 1500;
}
