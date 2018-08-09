@file:Suppress("OverridingDeprecatedMember")

package ch.abhishek.echo.fragments


import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import ch.abhishek.echo.CurrentSongHelper
import ch.abhishek.echo.R
import ch.abhishek.echo.Songs
import java.util.*
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
/**
 * A simple [Fragment] subclass.
 *
 */
class SongPlayingFragment : Fragment() {
    var myActivity:Activity?=null
    var mediaplayer:MediaPlayer? = null
    var startTimeText : TextView?=null
    var endTimeText:TextView?=null
    var playpauseImageButton:ImageButton?=null
    var previousImageButton:ImageButton?=null
    var nextImageButton:ImageButton?=null
    var loopImageButton:ImageButton?=null
    var seekbar: SeekBar?=null
    var songArtistView:TextView?=null
    var songTitleView:TextView?=null
    var shuffleImageButton:ImageButton?=null
    var currentSongHelper: CurrentSongHelper?=null
    var currentPosition:Int=0
    var fetchSongs:ArrayList<Songs>?=null

    var updateSongTime = object:Runnable{
        override fun run() {
            val getcurrent =mediaplayer?.currentPosition
            startTimeText?.setText(String.format("%d:%d",
           TimeUnit.MILLISECONDS.toMinutes(getcurrent?.toLong() as Long),
           TimeUnit.MILLISECONDS.toSeconds(getcurrent?.toLong() as Long)-
           TimeUnit.MILLISECONDS.toSeconds( TimeUnit.MILLISECONDS.toMinutes(getcurrent?.toLong() as Long))))
            Handler().postDelayed(this, 1000)

        }


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_song_playing,container,false)
        seekbar=view?.findViewById(R.id.seekBar)
        startTimeText=view?.findViewById(R.id.startTime)
        endTimeText=view?.findViewById(R.id.endTime)
        playpauseImageButton=view?.findViewById(R.id.playPauseButton)
        nextImageButton=view?.findViewById(R.id.nextButton)
        previousImageButton=view?.findViewById(R.id.previousButton)
        loopImageButton=view?.findViewById(R.id.loopButton)
        songArtistView=view?.findViewById(R.id.songArtist)
        songTitleView=view?.findViewById(R.id.songTitle)
        shuffleImageButton=view?.findViewById(R.id.shuffleButton)
        return view


    }

     override fun onAttach(context: Context?) {
        super.onAttach(context)
         myActivity=context as Activity
    }

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        myActivity=context as Activity
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        currentSongHelper= CurrentSongHelper()
        currentSongHelper?.isPlaying=true
        currentSongHelper?.isShuffle=false
        currentSongHelper?.isLoop=false

        var path: String?=null
        var _songTitle: String?=null
        var _songArtist: String?=null
        var _songId: Long =0
        try{
            path            = arguments?.getString("path")
            _songTitle      = arguments?.getString("songTitle")
            _songArtist     = arguments?.getString("songArtist")
            _songId         = arguments?.getString("SongId")!!.toLong()
            currentPosition = arguments?.getInt("position")!!
            fetchSongs      =arguments?.getParcelableArrayList("songData")

            currentSongHelper?.songPath   =path
            currentSongHelper?.songTitle  =_songTitle
            currentSongHelper?.songArtist =_songArtist
            currentSongHelper?.songId     =_songId
            currentSongHelper?.currentPosition=currentPosition

            updateTextViews(currentSongHelper?.songTitle as String,currentSongHelper?.songArtist as String)

        }
        catch(e:Exception){
            e.printStackTrace()
        }
        mediaplayer= MediaPlayer()
        mediaplayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try{
            mediaplayer?.setDataSource(myActivity, Uri.parse(path))
            mediaplayer?.prepare()
        }
        catch(e:Exception){
            e.printStackTrace()
        }
        mediaplayer?.start()
        processInformation(mediaplayer as MediaPlayer)
        if(currentSongHelper?.isPlaying as Boolean){
            playpauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
        }
            else {
            playpauseImageButton?.setBackgroundResource(R.drawable.play_icon)
        }
        mediaplayer?.setOnCompletionListener {
            onSongComplete()
        }
        clickHandler()

    }

    fun clickHandler() {
        shuffleImageButton?.setOnClickListener({
            if(currentSongHelper?.isShuffle as Boolean){
                shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
                currentSongHelper?.isShuffle=false
            }
            else{
                currentSongHelper?.isShuffle=true
                currentSongHelper?.isLoop=false
                shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_icon)
                loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)
            }


        })
        nextImageButton?.setOnClickListener({
            currentSongHelper?.isPlaying = true
            if (currentSongHelper?.isShuffle as Boolean) {
                playNext("PlayNextLikeNormalShuffle")
            }
            else{
                playNext("PlayNextNormal")
            }

        })
        previousImageButton?.setOnClickListener({
            currentSongHelper?.isPlaying = true
            if (currentSongHelper?.isShuffle as Boolean) {
               loopImageButton?.setBackgroundResource(R.drawable.loop_white_icon)
            }
            playPrevious()

        })
        loopImageButton?.setOnClickListener({
            if(currentSongHelper?.isLoop as Boolean){
                currentSongHelper?.isLoop=false
            }
            else{
                currentSongHelper?.isLoop=true
                currentSongHelper?.isShuffle=false
                loopImageButton?.setBackgroundResource(R.drawable.loop_icon)
                shuffleImageButton?.setBackgroundResource(R.drawable.shuffle_white_icon)
            }

        })
        playpauseImageButton?.setOnClickListener({
            if (mediaplayer?.isPlaying as Boolean) {
                mediaplayer?.pause()
                currentSongHelper?.isPlaying = false
                playpauseImageButton?.setBackgroundResource(R.drawable.play_icon)
            } else {
                mediaplayer?.start()
                currentSongHelper?.isPlaying = true
                playpauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
            }

        })
    }
        fun playNext(check:String){
            if(check.equals("PlayNextNormal",true)){
                currentPosition=currentPosition+1
            }
            else if(check.equals("PlayNextLikeNormalShuffle",true)){
                var randomObject= Random()
                var randomposition=randomObject.nextInt(fetchSongs?.size?.plus(1) as Int)
                currentPosition=randomposition

            }
            if(currentPosition==fetchSongs?.size){
                currentPosition=0
            }
            currentSongHelper?.isLoop=false
            var nextSong = fetchSongs?.get(currentPosition)
            currentSongHelper?.songTitle=nextSong?.songTitle
            currentSongHelper?.songPath=nextSong?.songData
            currentSongHelper?.currentPosition=currentPosition
            currentSongHelper?.songId=nextSong?.songId as Long

            updateTextViews(currentSongHelper?.songTitle as String,currentSongHelper?.songArtist as String)

            mediaplayer?.reset()
            try{
                mediaplayer?.setDataSource(myActivity,Uri.parse(currentSongHelper?.songPath))
                mediaplayer?.prepare()
                mediaplayer?.start()
                processInformation(mediaplayer as MediaPlayer)
            }
            catch(e:Exception){
                e.printStackTrace()
            }

        }
    fun playPrevious() {
        currentPosition = currentPosition - 1
        if (currentPosition == -1) {
            currentPosition = 0
        }
        if (currentSongHelper?.isPlaying as Boolean) {
            playpauseImageButton?.setBackgroundResource(R.drawable.pause_icon)
        } else {
            playpauseImageButton?.setBackgroundResource(R.drawable.play_icon)
        }
        currentSongHelper?.isLoop = false
        var nextSong = fetchSongs?.get(currentPosition)
        currentSongHelper?.songPath = nextSong?.songData
        currentSongHelper?.songTitle = nextSong?.songTitle
        currentSongHelper?.songArtist = nextSong?.artist
        currentSongHelper?.songId = nextSong?.songId as Long
        updateTextViews(currentSongHelper?.songTitle as String,currentSongHelper?.songArtist as String)

        mediaplayer?.reset()
        try {
            mediaplayer?.setDataSource(myActivity, Uri.parse(currentSongHelper?.songPath))
            mediaplayer?.prepare()
            mediaplayer?.start()
            processInformation(mediaplayer as MediaPlayer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun onSongComplete(){
        if(currentSongHelper?.isShuffle as Boolean){
            playNext("PlayNextLikeNormalShuffle")
            currentSongHelper?.isPlaying=true

        }
        else {
            if(currentSongHelper?.isLoop as Boolean){
                playNext("PlayNextNormal")
                currentSongHelper?.isPlaying=true
                var nextSong =fetchSongs?.get(currentPosition)

                currentSongHelper?.songTitle=nextSong?.songTitle
                currentSongHelper?.songPath=nextSong?.songData
                currentSongHelper?.currentPosition=currentPosition
                currentSongHelper?.songId=nextSong?.songId as Long
                updateTextViews(currentSongHelper?.songTitle as String,currentSongHelper?.songArtist as String)

                mediaplayer?.reset()
                try {
                    mediaplayer?.setDataSource(myActivity, Uri.parse(currentSongHelper?.songPath))
                    mediaplayer?.prepare()
                    mediaplayer?.start()
                    processInformation(mediaplayer as MediaPlayer)
                }
                catch (e:Exception){
                    e.printStackTrace()
                }
            }
            else{
                playNext("PlayNextNormal")
                currentSongHelper?.isPlaying=true
            }



        }
    }
    fun updateTextViews(songtitle:String,songArtist:String){
        songTitleView?.setText(songtitle)
        songArtistView?.setText(songArtist)
    }

    fun processInformation(mediaPlayer: MediaPlayer) {
        val finalTime = mediaPlayer.duration
        val startTime = mediaPlayer.currentPosition
        seekbar?.max=finalTime
        startTimeText?.setText(String.format("%d: %d",
                TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) -
                        TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()))))
        endTimeText?.setText(String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong()) -
                        TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()))))

        seekbar?.setProgress(startTime)
        Handler().postDelayed(updateSongTime,1000)
    }
}




