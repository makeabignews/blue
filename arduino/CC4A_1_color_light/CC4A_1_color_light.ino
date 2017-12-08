void setup()
{
  Serial.begin(115200);
  pinMode(9, OUTPUT);
  pinMode(10, OUTPUT);
  pinMode(11, OUTPUT);
  pinMode(12, OUTPUT);
  digitalWrite(9, HIGH);
}
void loop()
{
  while(Serial.available())
   {
     char c=Serial.read();
      if(c=='1')
        {
          digitalWrite(10, HIGH);
        }
        if(c=='2')
        {
          digitalWrite(11, HIGH);
        }
        if(c=='3')
        {
          digitalWrite(12, HIGH);
        }
         if(c=='4')
        {
          digitalWrite(10, LOW);
        }
        if(c=='5')
        {
          digitalWrite(11, LOW);
        }
        if(c=='6')
        {
          
          digitalWrite(12, LOW);
        }
        Serial.write(c);
   }
}
