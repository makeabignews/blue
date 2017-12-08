void setup()
{
  Serial.begin(9600);
  pinMode(12, OUTPUT);
}
void loop()
{
  while(Serial.available())
   {
     char c=Serial.read();
      if(c=='1')
        {
//          Serial.println("Hello I am amarino");
          Serial.write("12");//返回到手机调试程序上
          digitalWrite(12, HIGH);
          delay(0.5);
          digitalWrite(12, LOW);
        }
   }
}
