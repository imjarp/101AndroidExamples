// ApiInterfaceV1.aidl
package com.example.jarp.aidlcallback;

// Declare any non-default types here with import statements
import com.example.jarp.aidlcallback.CustomData;
import com.example.jarp.aidlcallback.AidlCallback;

interface ApiInterfaceV1 {

    boolean isPrime ( long value );

       void getAllDataSince ( long timeStamp , out CustomData[] result );

       void storeData ( in CustomData data);

       void addCallback( in AidlCallback callback );
}
