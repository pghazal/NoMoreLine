%To add in Java code
%import com.mathworks.toolbox.javabuilder.*;

function Camera_Detection()

clear all;
close all;

CameraUsed = webcam; % initialise the current webcam
set (CameraUsed, 'Resolution', '320x240'); % Set the resolution of the camera

ImageReference = snapshot(CameraUsed); % take a picture of the image of reference

infinityloop=true;
while infinityloop
LiveImage=snapshot(CameraUsed); % take a picture of the scene
ImageDifference=abs(cast(LiveImage,'int16')-cast(ImageReference,'int16')); % computation of the difference between the 2 images
ImageDifference=ImageDifference>30; % set a threshold (55) of the pixel that change. Return a matrix of binary number
PixelChange=sum(sum(sum(ImageDifference))); % sum this matrix of binary numbers
PixelChange=((PixelChange*100)/230400);
if (PixelChange>60)
    Motion=1;
    h = msgbox('motion!!');
else
    Motion=0;
end
pause(1);
end;
