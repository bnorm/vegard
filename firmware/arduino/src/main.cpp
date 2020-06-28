
#include "Arduino.h"
#include <WiFiNINA.h>
#include <ArduinoHttpClient.h>
#include <dht.h>

#include "arduino_secrets.h"
#include "helpers.h"

#define DELAY 5000
#define MOISTURE1_PIN PIN_A1
#define MOISTURE1_MIN 460
#define MOISTURE1_MAX 890

char ssid[] = SECRET_SSID; // your network SSID (name)
char pass[] = SECRET_PASS; // your network password (use for WPA, or use as key for WEP)

char host[] = SERVER_HOST;
int port = 8080;

WiFiClient wifi;
HttpClient client = HttpClient(wifi, host, port);
int status = WL_IDLE_STATUS;

dht tempSensor;

#define DHT22_PIN 5

void setup() {
    Serial.begin(9600);
    while (status != WL_CONNECTED) {
        Serial.print("Attempting to connect to Network named: ");
        Serial.println(ssid);  // print the network name (SSID);

        // Connect to WPA/WPA2 network:
        status = WiFi.begin(ssid, pass);
    }

    // print the SSID of the network you're attached to:
    Serial.print("SSID: ");
    Serial.println(WiFi.SSID());

    // print your WiFi shield's IP address:
    IPAddress ip = WiFi.localIP();
    Serial.print("IP Address: ");
    Serial.println(ip);
}

void loop() {
    if (DHTLIB_OK != tempSensor.read2302(DHT22_PIN)) {
        delay(DELAY);
        return;
    }

    int moisture1Raw = analogRead(MOISTURE1_PIN);

    Serial.println("making POST request");

    String postData = String("{")
                      + String("\"humidity\":") + String(tempSensor.humidity) + String(",")
                      + String("\"temperature\":") + String(tempSensor.temperature) + String(",")
                      + String("\"moisture\":") + String(moisture1Raw)
                      + String("}");
    client.post("/api/v1/dht", "application/json", postData);

    // read the status code and body of the response
    int statusCode = client.responseStatusCode();
    String response = client.responseBody();
    client.clearWriteError()

    Serial.print("Status code: ");
    Serial.println(statusCode);
    Serial.print("Response: ");
    Serial.println(response);

    delay(DELAY);
}

//#define ANALOG_PIN PIN_A1
//
//void setup() {
//    Serial.begin(9600);
//}
//
//void loop() {
//    int read = analogRead(ANALOG_PIN);
//    Serial.println(read);
//}
