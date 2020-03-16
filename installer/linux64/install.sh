#!/usr/bin/env bash
echo "StreamPi Client Installer"
echo "Alpha 0.0.5"
echo -n "Enter Screen Width (No. Of Pixels) : "
read screen_width
echo -n "Enter Screen Height (No. Of Pixels) : "
read screen_height
echo "Installing Fonts ..."
sudo cp Roboto-Regular.ttf /usr/share/fonts/truetype/
fc-cache -v -f
echo "... Done!"
echo "Copying Files ..."
mkdir ~/StreamPi/
sudo cp -r actions ~/StreamPi/
sudo cp -r animatefx ~/StreamPi/
sudo cp -r assets ~/StreamPi/
sudo cp -r com ~/StreamPi/
sudo cp -r css ~/StreamPi/
sudo cp -r jdk ~/StreamPi/
sudo cp -r net ~/StreamPi/
sudo cp -r StreamPiClient ~/StreamPi/
sudo cp start_streampi ~/StreamPi/
cd ~/StreamPi/
sudo chmod +x jdk/bin/java
echo "${screen_width}::${screen_height}::black::192.168.0.102::69::test1::1::0::100::10:::" >> config
sudo chmod +x start_streampi
echo "... Done!"
echo "Installation done. run '~/StreamPi/start_streampi' to start StreamPi"
read -n 1 -s -r
