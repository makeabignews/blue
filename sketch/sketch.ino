void setup()
{
   // 如果是HC-05，請改成38400
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
          Serial.write("Serial--12--high");//返回到手机调试程序上
          digitalWrite(12, HIGH);
        }
       if(c=='0')
       {
        Serial.write("Serial--12--low");
        digitalWrite(12, LOW);
       }
   }
}
