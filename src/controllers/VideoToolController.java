package controllers;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.beans.*;
import javafx.scene.control.Label;
import javafx.beans.binding.Bindings;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * VideoToolController - Controls the User Interaction on the
 *                       HyperLinking Video Tool Page
 */
public class VideoToolController extends AbstractController
{
	@FXML
	private MediaView _primaryVideoView;

	@FXML
	private MediaView _secondaryVideoView;

	@FXML
	private Slider _primaryVideoSlider;

	@FXML
	private Slider _secondaryVideoSlider;

	/** FXML filename associated with this Controller */
	private static final String FXML_NAME = "VideoTool.fxml";

	/** Primary Video File */
	private File _primaryVideo;

	/** Secondary Video File */
	private File _secondaryVideo;

	/** Primary Media Object */
	private Media _primaryMedia;

	/** Secondary Media Object */
	private Media _secondaryMedia;

	private MediaPlayer mediaPlayer1;

	private MediaPlayer mediaPlayer2;

	/**
	* Constructor
	*/
	public VideoToolController()
	{
		super(FXML_NAME);

	  // Initialize Video Views to Hidden
	  _primaryVideoView.setVisible(true);
	  _secondaryVideoView.setVisible(true);
	  _primaryVideoSlider.setVisible(true);

	  // Initialize Video Files to be null
	  _primaryVideo = new File("");
	  _secondaryVideo = new File("");
	  


	}

	/**
	* setPrimaryVideo - Sets the Current Primary Video
	*
	* @param file - The Primary Video
	*/
	public void setPrimaryVideo(final File primaryVideo)
	{
	// Update the Primary Video

		_primaryVideo = primaryVideo;
		try {
		  _primaryMedia = new Media(_primaryVideo.toURI().toURL().toString());
		}
		catch (Exception e){
		  return;
		}
		mediaPlayer1 = new MediaPlayer(_primaryMedia);
		_primaryVideoView.setMediaPlayer(mediaPlayer1);


		setPrimarySlider();
	}

	/**
	* setSecondaryVideo - Sets the Current Secondary Video
	*
	* @param file - The Secondary Video
	*/
	public void setSecondaryVideo(final File secondaryVideo)
	{
		// Update the Secondary Video
		_secondaryVideo = secondaryVideo;

		try {
			_secondaryMedia = new Media(_secondaryVideo.toURI().toURL().toString());
		}
		catch (Exception e){
			return;
		}
		mediaPlayer2 = new MediaPlayer(_secondaryMedia);

		_secondaryVideoView.setMediaPlayer(mediaPlayer2);
	}

  public void setPrimarySlider()
	{
	  // Update the Primary Video

  		_primaryVideoSlider.setMin(1);
  		_primaryVideoSlider.setMax(9000);
  		_primaryVideoSlider.setValue(1);
		_primaryVideoSlider.setMajorTickUnit(1);
		_primaryVideoSlider.setSnapToTicks(true);
		_primaryVideoSlider.valueProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
				int val = (int) _primaryVideoSlider.getValue();
				int start = (int) Math.round((100./3) * val);
				System.out.println(start);
				mediaPlayer1.seek(new Duration(start));
			}
		});
	}



}