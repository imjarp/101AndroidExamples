# Android example #2 

The intention of this example is simply read the output from the logcat 

The steps you need to check is

  - Install the app 
  - Run in the console the script .
  
### Script

```sh
$ cd ~(yourpath)/sdk/platform-tools/
$adb logcat MY_TAG:* *:s 
$adb logcat MY_TAG:D *:s 
$adb logcat MY_TAG:E *:s 
$adb logcat MY_TAG:I *:s
$adb logcat MY_TAG:V *:s  
```
Then you can check in the log all the messages that are display in the terminal

--------- beginning of /dev/log/system
--------- beginning of /dev/log/main
D/MY_TAG  (23745): Hello from MY_TAG debug
V/MY_TAG  (23745): Hello from MY_TAG verbose
E/MY_TAG  (23745): Hello from MY_TAG error
I/MY_TAG  (23745): Hello from MY_TAG info
