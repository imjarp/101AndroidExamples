// ApiInterfaceV1.aidl
package com.example.jarp.aidlcustomdata;

// Declare any non-default types here with import statements
import com.example.jarp.aidlcustomdata.CustomData;

interface ApiInterfaceV1 {

    boolean isPrime ( long value );

    void getAllDataSince ( long timeStamp , out CustomData[] result );

    void storeData ( in CustomData data);

}
