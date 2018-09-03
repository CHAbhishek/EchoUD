package ch.abhishek.echo.utils

import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import ch.abhishek.echo.R
import ch.abhishek.echo.activities.MainActivity
import ch.abhishek.echo.fragments.SongPlayingFragment

class CaptureBroadcast: BroadcastReceiver(){
//    @SuppressLint("ServiceCast")
    override fun onReceive(p0: Context?, p1: Intent?) {
        if(p1?.action==Intent.ACTION_NEW_OUTGOING_CALL){
            try{
                MainActivity.Statified.notificationManager?.cancel(1998)
            }catch (e:Exception){
                e.printStackTrace()
            }
            try{
            if(SongPlayingFragment.Statified.mediaplayer?.isPlaying as Boolean){
                SongPlayingFragment.Statified.mediaplayer?.pause()
                SongPlayingFragment.Statified.playpauseImageButton?.setBackgroundResource(R.drawable.play_icon)
            }
        }catch(e:Exception){
                print(e.stackTrace)
            }
    }
        else{
            val tm: TelephonyManager = p0?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            when(tm?.callState){
                TelephonyManager.CALL_STATE_RINGING->{
                    try{
                        MainActivity.Statified.notificationManager?.cancel(1998)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                    try{
                        if(SongPlayingFragment.Statified.mediaplayer?.isPlaying as Boolean){
                            SongPlayingFragment.Statified.mediaplayer?.pause()
                            SongPlayingFragment.Statified.playpauseImageButton?.setBackgroundResource(R.drawable.play_icon)
                        }
                    }catch(e:Exception){
                        print(e.stackTrace)
                    }

                }


                else->{}

            }
        }
    }

}
