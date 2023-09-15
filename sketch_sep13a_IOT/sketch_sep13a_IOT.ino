#include <Adafruit_Sensor.h>
#include <DHT.h>
#include <Servo.h>
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

Servo s1;
#define rainAnalog A0
#define rainDigital D2
#define relayPin D4

bool rainkey;
bool tempkey;

#define FIREBASE_HOST "iot1-7c15f-default-rtdb.firebaseio.com"
#define FIREBASE_AUTH "aRv9sDHiCIvOhL0f78DJNAm1e6DRkSEQKolGiXNe"
#define WIFI_SSID "DINAL"
#define WIFI_PASSWORD "1234567890"

#define DHTPIN D5
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

void setup() {
  s1.attach(D3);
  Serial.begin(9600);
  dht.begin();
  pinMode(rainDigital, INPUT);
  pinMode(relayPin, OUTPUT);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.println("Connected.");
  Serial.println(WiFi.localIP());
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
}

void loop() {
  int rainAnalogVal = analogRead(rainAnalog);
  int rainDigitalVal = digitalRead(rainDigital);

  float temp = dht.readTemperature();
  // if (isnan(temp)) {
  //   Serial.println(F("Failed to read from DHT sensor!"));
  //   return;
  // }

  // // Read rainkey and tempkey values from Firebase
  // Firebase.getBool("/switches/rainkey", rainkey);
  // Firebase.getBool("/switches/tempkey", tempkey);
  // Read rainkey and tempkey values from Firebase
  rainkey = Firebase.getBool("/switches/rainkey");
  tempkey = Firebase.getBool("/switches/tempkey");
  if (Firebase.failed()) {
    Serial.println(F("Firebase error"));
    return;
  }
  //set values in firebase
  Firebase.setFloat("/values/Temp", temp);
  Firebase.setFloat("/values/rainDigitalVal", rainDigitalVal);
  Firebase.setFloat("/values/rainAnalogVal", rainAnalogVal);
  // Print sensor values to Serial Monitor
  Serial.println(F("Temperature: "));
  Serial.print(temp);
  Serial.println(" °C");
  Serial.print("\n");
  Serial.println(F("Rain Detection Analog Value: "));
  Serial.print(rainAnalogVal);
  Serial.print("\n");
  Serial.println(F("Rain Detection Digital Value: "));
  Serial.println(rainDigitalVal);

  // Relay Control
  if (rainkey || temp > 35) {
    digitalWrite(relayPin, HIGH);
    Serial.println("Relay turned ON");
  } else {
    digitalWrite(relayPin, LOW);
    Serial.println("Relay turned OFF");
  }

  // Servo Control
  if (tempkey || rainDigitalVal == 0) {
    Serial.println("Door turned ON");
    s1.write(0);
  } else {
    Serial.println("Door turned OFF");
    s1.write(180);
  }

  delay(1000);
}
