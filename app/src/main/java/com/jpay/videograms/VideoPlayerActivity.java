package com.jpay.videograms;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.jpay.mvcvm.exceptions.MissingControllerException;
import com.jpay.mvcvm.utils.JLog;
import com.jpay.mvcvm.views.JView;
import com.jpay.videograms.controllers.VideoPlayerController;
import com.jpay.videograms.models.Videograms;
import com.jpay.videograms.utils.Utils;
import com.jpay.videograms.viewmodels.ViewModelManager;

import java.io.File;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoPlayerActivity extends AppCompatActivity implements JView {

    private VideoView videoView;
    private VideoPlayerController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        try {
            mController = new VideoPlayerController();
            mController.onCreate(this);
        } catch (MissingControllerException ex) {
            JLog.printStrackTrace(ex);
        }

        Bundle e = getIntent().getExtras();
        if (e != null) {
            Bundle data = getIntent().getBundleExtra(Constants.EXTRA_INTENT_VIDEOGRAMS_BUNDLE);
            Videograms playingVideo = data.getParcelable(Constants.EXTRA_INTENT_VIDEOGRAMS);
            int currentVideogramsPos = data.getInt(Constants.EXTRA_INTENT_VIDEOGRAMS_POSITION);
            mController.initVariable(playingVideo, currentVideogramsPos);
        }

        videoView = (VideoView) findViewById(R.id.myvideoview);
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return mController.onPlayingVideoError(what, extra);
            }
        });
        MediaController mediaController = new MediaController(this, true);
        mediaController.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Next Button clicked*/
                mController.onNextButtonClicked();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Prev Button clicked*/
                mController.onPrevButtonClicked();
            }
        });
        videoView.setMediaController(mediaController);
        findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mController.onRequestDataOnLoad();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mController != null) {
            mController.onResume();
        }
    }

    @Override
    public void onDestroy() {
        if (mController != null) {
            mController.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mController != null) {
            mController.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mController != null) {
            mController.onStop();
        }
    }

    /**
     * Prepare and start video
     *
     * @param videoFile
     */
    public void loadVideoViewData(final File videoFile) {
        videoView.post(new Runnable() {
            @Override
            public void run() {
                videoView.setVideoURI(Uri.parse(videoFile.getAbsolutePath()));
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        videoView.requestFocus();
                        videoView.start();
                    }
                });
            }
        });
    }

    public void showVideoNotFoundError() {
        Utils.showAlertMessage(this, "Video not found!", "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    public void showEmptyVideoError() {
        Utils.showAlertMessage(this, "Empty video!", "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    public void showPlayVideoError() {
        Utils.showAlertMessage(this, "Unable to play the video!", "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    public int getStopPosition() {
        if (videoView == null) {
            return 0;
        }
        return videoView.getCurrentPosition();
    }

    public void resumeVideo(int stopPosition) {
        videoView.seekTo(stopPosition);
    }

    public void displayNoMorePreviousVideoError() {
        Utils.showAlertMessage(this, "Not more previous video to play!", "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    public void displayNoMoreNextVideoError() {
        Utils.showAlertMessage(this, "Not more next video to play!", "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }

    public void stopVideoView() {
        videoView.stopPlayback();
    }
}
