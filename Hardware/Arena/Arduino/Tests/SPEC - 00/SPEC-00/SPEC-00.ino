/*  Soccar Project
 *  
 *  v 1.0
 *  
 *  SPEC - 00
 *  
 *  This test will demonstrate the Arduino Nano's ability
 *  to write information to the LCD Display. This script will
 *  test proper functionality and connection of the LCD display
 *  by writing the phrase "Hello World!"
 *  
 *  This script was written for the Arduino NANO
 */

 // Define the Liquid Crystal Pinouts
 #define RS 2
 #define EN 3
 #define D0 4
 #define D1 5
 #define D2 6
 #define D3 7

 // Include the LiquidCrystal library
 #include <LiquidCrystal.h>

 // Set up the lcd object to use
 LiquidCrystal lcd(RS, EN, D0, D1, D2, D3);

void setup() {
 // Begin the LCD Display and write "Hello World!" to the screen
  lcd.begin(16, 2);

  lcd.print("Hello World!");
}

void loop() {
  // Otherwise do nothing
}
