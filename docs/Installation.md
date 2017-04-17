
#Raspberry steps

Downloaded raspberry pi full raspbian image from
https://www.raspberrypi.org/downloads/raspbian/

Followed those instructions to write the image to SD card:
https://www.raspberrypi.org/documentation/installation/installing-images/mac.md
using Etcher https://etcher.io/


Once i had the image on SD card, i mounted the ext2 partition.


#####Linux configuration
Copied and adapted the network configuration files to **/etc/network** and **/etc/wpa_applicant**
from original project files found in docs/resources.
CDed to the boot partition of the card, and added an empty file, so that raspbian starts ssh server on boot.
touch ssh

Once booted, i did the normally mandatory apt updates, changed the password.

Created the boot script so that the app starts when machine does. Sudo-created a file in **/etc/systemd/system** called **HalBuddy.service**
```
[Unit]
Description=Button manager

[Service]
ExecStart=/usr/bin/java -jar HalBuddy-java.jar
WorkingDirectory=/home/pi/HalBuddy-java
Restart=always

[Install]
WantedBy=default.target
```
then
```
sudo systemctl enable HalBuddy
```


#####To install PI4j natives
(source http://pi4j.com/install.html)

Type in bash:
**curl -s get.pi4j.com | sudo bash**

#####To install maven
Maven is necessary to compile the project. It, if installed through APT will force a jdk7 install, which i did not want. I instead downloaded latest release from http://maven.apache.org and followed the simple instructions here. https://maven.apache.org/install.html

#####OSX tip for ext2
If not present on machine,
follow the instruction to install from sources here:
https://github.com/alperakcan/fuse-ext2

#Code
Accessing the GPIO is done using the PI4J library at http://pi4j.com.
The rest webserver is using the sparkjava library at sparkjava.com.
Application has a simple main()


###Building code


#Electronics
There is a single pcb board on the expansion port, that mainly uses 5V to power the LED through a N-Mosfet transistor.

#Mechanics
Freecad was used to prepare a housing. http://www.freecadweb.org
Housing consists of two seperate parts, one fixed to wall and also maintaining pi and its cable. Other part is screwed to the button. A cable and connector

##known problems:
- LED ought to be better with 12V. Next version ought to have a step-up circuitry.
