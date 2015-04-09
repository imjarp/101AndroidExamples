// AidlCallback.aidl
package com.example.jarp.aidlcallback;

// Declare any non-default types here with import statements
import com.example.jarp.aidlcallback.CustomData;

oneway interface AidlCallback {

void onDataUpdated( in CustomData [] data );

}
