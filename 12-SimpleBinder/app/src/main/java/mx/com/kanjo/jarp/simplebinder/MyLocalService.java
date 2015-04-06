package mx.com.kanjo.jarp.simplebinder;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class MyLocalService extends Service {

    private static final int NOTIFICATION_ID= 1001;

    private LocalBinder mLocalBinder = new LocalBinder();

    private Callback mCallBack;

    public MyLocalService() {
    }

    public void setCallBack(Callback callBack ){
        mCallBack = callBack;
    }

    public void doLongRunningOperation( MyComplexDataObject dataObject){
        new MyAsyncTask().execute(dataObject);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }

    public class LocalBinder extends Binder{

        public MyLocalService getService(){
            return  MyLocalService.this;
        }
    }

    public interface Callback {
        void onOperationProgress ( int progress );
        void onOperationCompleted (  MyComplexResult complexResult);
    }



    private NotificationCompat.Builder buildNotification(){


        return  new NotificationCompat.Builder(this)
                                        .setContentText("Notification from Service")
                                        .setContentText("Content Text Service")
                                        .setSmallIcon(R.mipmap.ic_launcher);


    }

    private final class MyAsyncTask extends AsyncTask<MyComplexDataObject,Integer,MyComplexResult>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startForeground(NOTIFICATION_ID, buildNotification().build());
        }

        @Override
        protected MyComplexResult doInBackground(MyComplexDataObject... params) {
            //Just for testing
            MyComplexResult result = new MyComplexResult();

            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            if(mCallBack != null && values.length >0){
                for ( Integer integer : values  ){
                    mCallBack.onOperationProgress(integer);
                }
            }

        }

        @Override
        protected void onCancelled(MyComplexResult complexResult) {
            super.onCancelled(complexResult);
            stopForeground(true);
        }


        @Override
        protected void onPostExecute(MyComplexResult complexResult) {

            if(mCallBack!=null){

                mCallBack.onOperationCompleted(complexResult);
            }
            stopForeground(true);
        }
    }

}

