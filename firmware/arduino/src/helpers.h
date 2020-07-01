//
// Created by Brian on 6/20/2020.
//

#ifndef UNTITLED_HELPERS_H
#define UNTITLED_HELPERS_H

#include "arduino_secrets.h"
#include <WiFiNINA.h>
#include <SPI.h>
#include "Arduino.h"

int minOf(int n1, int n2) {
    return n1 > n2 ? n2 : n1;
}

int maxOf(int n1, int n2) {
    return n1 < n2 ? n2 : n1;
}

double toRatio(int value, int min, int max) {
    return (((double) minOf(maxOf(value, min), max) - min) / (max - min));
}

void blinkLed(int port, int duration) {
    digitalWrite(port, HIGH);
    delay(duration);
    digitalWrite(port, LOW);
    delay(duration);
}

void getMacAddress(char macAddress[18]) {
    byte mac[6];
    WiFi.macAddress(mac);
    sprintf(macAddress, "%2x:%2x:%2x:%2x:%2x:%2x", mac[5], mac[4], mac[3], mac[2], mac[1], mac[0]);
}

void printWifiData() {
    // print your WiFi shield's IP address:
    IPAddress ip = WiFi.localIP();
    Serial.print("IP Address: ");
    Serial.println(ip);

    // print your MAC address:
    byte mac[6];
    WiFi.macAddress(mac);
    Serial.print("MAC address: ");
    Serial.print(mac[5], HEX);
    Serial.print(":");
    Serial.print(mac[4], HEX);
    Serial.print(":");
    Serial.print(mac[3], HEX);
    Serial.print(":");
    Serial.print(mac[2], HEX);
    Serial.print(":");
    Serial.print(mac[1], HEX);
    Serial.print(":");
    Serial.println(mac[0], HEX);

}

void printCurrentNet() {
    // print the SSID of the network you're attached to:
    Serial.print("SSID: ");
    Serial.println(WiFi.SSID());

    // print the MAC address of the router you're attached to:
    byte bssid[6];
    WiFi.BSSID(bssid);
    Serial.print("BSSID: ");
    Serial.print(bssid[5], HEX);
    Serial.print(":");
    Serial.print(bssid[4], HEX);
    Serial.print(":");
    Serial.print(bssid[3], HEX);
    Serial.print(":");
    Serial.print(bssid[2], HEX);
    Serial.print(":");
    Serial.print(bssid[1], HEX);
    Serial.print(":");
    Serial.println(bssid[0], HEX);

    // print the received signal strength:
    long rssi = WiFi.RSSI();
    Serial.print("signal strength (RSSI):");
    Serial.println(rssi);

    // print the encryption type:
    byte encryption = WiFi.encryptionType();
    Serial.print("Encryption Type:");
    Serial.println(encryption, HEX);
    Serial.println();
}

#endif //UNTITLED_HELPERS_H
