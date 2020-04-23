#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>

#define FIREBASE_HOST "https://iothome-4c3df.firebaseio.com/"
#define FIREBASE_AUTH "rfKE0IYa2v9pT1EKdDC2Qu5vvfZqvF4Z5TsnTLFR"
#define WIFI_SSID "bangsad"
#define WIFI_PASSWORD "william123"


#define ldr 5   //D1
#define led 2   //D4
#define led1 4 // D2
#define pir 13 // 

//2. Define FirebaseESP8266 data object for data sending and receiving
FirebaseData firebaseData;

String path = "/Node1";

//int oldAdcLdr;
//int newAdcLdr;

int stateMotion = LOW;             // default tidak ada gerakan
int valMotion = 0;
int valLdr = LOW;

void setup()
{

  Serial.begin(115200);

  pinMode(ldr,INPUT);
  pinMode(pir,INPUT);
  pinMode(led1,OUTPUT);
  pinMode(led,OUTPUT);

//  oldAdcLdr = analogRead(ldr);
  
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  //3. Set your Firebase info

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  //4. Enable auto reconnect the WiFi when connection lost
  Firebase.reconnectWiFi(true);

}

void loop()
{
  delay(500);
  
 valMotion = digitalRead(pir);   // read sensor value
  if (valMotion == HIGH) {           // check if the sensor is HIGH
    if (stateMotion == HIGH) {
      Firebase.setInt(firebaseData, path + "/pir", 1);
      Firebase.setInt(firebaseData, path + "/led1", 1);
      stateMotion = HIGH;  
      digitalWrite(led1, HIGH);// update variable state to HIGH
    }
  } 
  else {
    if (stateMotion == LOW){
      Firebase.setInt(firebaseData, path + "/pir", 0);
      Firebase.setInt(firebaseData, path + "/led1", 0);
      stateMotion = LOW;   
      digitalWrite(led1, LOW);// update variable state to LOW
    }
  }


  //AMBIL NILAI DARI LDR DAN KIRIM KE FIREBASE
  valLdr = digitalRead(ldr);  //read sensor LDR
  if(valLdr == HIGH){         //cek apakah sensor HIGH
      Firebase.setInt(firebaseData, path + "/ldr", 1);
      Firebase.setInt(firebaseData, path + "/led", 1);
      digitalWrite(led, HIGH);
//      Serial.println("kurang cahaya");
//      Serial.println("LED Hidup");
  }
  else{
      Firebase.setInt(firebaseData, path + "/ldr", 0);
      Firebase.setInt(firebaseData, path + "/led", 0);
      digitalWrite(led, LOW);
//      Serial.println("Cukup cahaya");
//      Serial.println("LED Mati");
  }
  
//  newAdcLdr = analogRead(ldr);
//  Serial.println(newAdcLdr);
//  double volLdr = (newAdcLdr*3.3)/4095.0;
//  Serial.println(volLdr);
//  if(newAdcLdr != oldAdcLdr){
//    Firebase.setDouble(firebaseData, path + "/ldr", volLdr);
//    oldAdcLdr = newAdcLdr;
//  }
}
