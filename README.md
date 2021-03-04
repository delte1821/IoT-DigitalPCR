# IoT-DigitalPCR

IoT-Digital application developed by Ji Wook Choi.

Android application (.apk) is available.

========================================================================

Step 1) Pairing android smartphone with Digital PCR device.

Step 2) Send parameters from smartphone to Digital PCR device.

Step 3) Digital PCR activation via "Start" button

  i) Thermal cycling
  
    * Parameters *
    
    Temp 1 = Denaturation temperature (*C)
    
    Time 1 = Denaturation time (sec)
    
    Temp 2 = Annealing temperature (*C)
    
    Time 2 = Annealing time (sec)
    
    Temp 3 = Extension temperature (*C)
    
    Time 3 = Extension time (sec)
    
    Ncyc = Number of thermal cycle
    
  ii) Fluorescence imaging
  
    * Parameters *
    
    ISO
    
    Exposure time (sec)
    
    Shutter speed (ms)
    
    Live time (sec)
    
    Fluorescence (RGB)
    
  iii) Digital PCR analysis
  
    * Parameters * 

    Detection parameter 1 / 2 = Houghcircle parameter

    Minimum / Maximum radius (px)

    Minimum distance (px)
  
Step 4) Receive data (.db file) from Digital PCR device

Step 5) Send data to server
