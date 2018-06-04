# piMote
Java servlet for receiving commands to send to LIRC or Denon receivers 

Very much a work in progress...

Using Alex to send voice commands to a rasbbery pi using: https://github.com/bwssytems/ha-bridge 
Ha-bridge then forwards commands onto piMote which parses the command and can either sends a command to LIRC to control any IR controlled device you have setup (requires led connected to pi) or sends a command to any Denon AVR that implements the DENON AVR control protocol.


# Prerequisites

- Raspberry pi
- https://github.com/bwssytems/ha-bridge installed on the pi and connected to your alexa
- Web Application Server (I use tomcat 8 on the pi)
- Java 8+ (soon to be 10)
- Maven 3.2+ (only for building)
- You must be connected to the same network as your AV receiver
- Your receiver must be either turned on or you have to enable the network standby feature


# API Usage example from ha-bridge

- Http Verb: POST
- Content Type: html/text (soon to be JSON)
- Target Item for LIRC: http://your.tomcat.deployment/LIRCServlet_war/servlets.LIRCServlet?remote=tv&command=power
- Target Item for Denon: http://your.tomcat.deployment/LIRCServlet_war/servlets.LIRCServlet?remote=avr&command=KEY_SOURCE_MEDIAPLAYER



