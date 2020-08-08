
#include "Arduino.h"
#include <WiFiNINA.h>
#include <ArduinoHttpClient.h>
#include <dht.h>

#include "arduino_secrets.h"
#include "helpers.h"

#define DELAY 5000
#define MOISTURE1_PIN PIN_A1
#define DHT22_PIN 5

char ssid[] = SECRET_SSID; // your network SSID (name)
char pass[] = SECRET_PASS; // your network password (use for WPA, or use as key for WEP)

char host[] = SERVER_HOST;
char macAddress[18];
int port = 8080;

WiFiClient wifi;
HttpClient client = HttpClient(wifi, host, port);
String token = "";

dht tempSensor;

int connect(String &token) {
    char *data = (char *) malloc(1024 * sizeof(char));
    sprintf(data, R"({"macAddress":"%s","serialNumber":"N/A"})", macAddress);
    Serial.print("Request: ");
    Serial.println(data);

    client.post("/api/v1/connect", "application/json", data);

    int statusCode = client.responseStatusCode();
    Serial.print("Status code: ");
    Serial.println(statusCode);

    token = client.responseBody();
    Serial.print("Response: ");
    Serial.println(token);

    free(data);
    return statusCode;
}

int record(HttpClient &httpClient, const String &token, int moisture1Raw, float temperature, float humidity) {
    // TODO: convert to String?
    char *data = (char *) malloc(1024 * sizeof(char));

    sprintf(
            data,
            R"({"ambientTemperature":%f,"ambientHumidity":%f,"soilMoisture":%d})",
            temperature,
            humidity,
            moisture1Raw
    );
    Serial.print("Request: ");
    Serial.println(data);

    httpClient.beginRequest();
    httpClient.post("/api/v1/controllers/record");
    httpClient.sendHeader(HTTP_HEADER_CONTENT_TYPE, "application/json");
    httpClient.sendHeader(HTTP_HEADER_CONTENT_LENGTH, strlen(data));
    httpClient.sendHeader("Authorization", "Bearer " + token);
    httpClient.endRequest();
    httpClient.print(data);

    int statusCode = httpClient.responseStatusCode();
    Serial.print("Status code: ");
    Serial.println(statusCode);

    String response = httpClient.responseBody();
    Serial.print("Response: ");
    Serial.println(response);

    free(data);
    return statusCode;
}

void setup() {
    Serial.begin(9600);

    int status = WL_IDLE_STATUS;
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

    getMacAddress(macAddress);
}

void loop() {
    Serial.println("LOOP");
    if (DHTLIB_OK != tempSensor.read2302(DHT22_PIN)) {
        delay(DELAY);
        return;
    }

    int moisture1Raw = analogRead(MOISTURE1_PIN);

    Serial.println("making POST request");
    int statusCode;

    statusCode = record(client, token, moisture1Raw, tempSensor.temperature, tempSensor.humidity);
    while (statusCode != 200) {
        connect(token);
        statusCode = record(client, token, moisture1Raw, tempSensor.temperature, tempSensor.humidity);
    }


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
