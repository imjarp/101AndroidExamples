# Android example #1 

The intention of this example is to call a Service from the adb.

The steps you need to check is

  - Install the app 
  - Run in the console the script .
  
### Script

```sh
$ cd ~(yourpath)/sdk/platform-tools/
$adb com.mx.jarp.AndroidServiceADB/.service.ServiceTest
```
Then you can check in the log (DEBUG) the next message

'01-15 20:24:57.899  25656-25656/com.mx.jarp.AndroidServiceADB D/ServiceTest﹕ Hello from service'
