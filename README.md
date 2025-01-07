# **WAV Music Player**

This project is a comprehensive WAV music player application developed using Java Swing for the graphical user interface (GUI) and the Java Sound API for seamless audio playback. It combines intuitive GUI design, efficient audio processing, and database integration to deliver a complete, functional music player that provides users with an engaging and user-friendly music listening experience.

The application integrates with a MySQL database to manage and display a list of available songs, allowing users to:

**Select a Song**: The list of songs is dynamically populated from the MySQL database, and when a song is selected, its metadata (such as genre, singer, release year, and language) is displayed in the GUI.

**Playback Controls**: Users can play, pause, and resume the selected song. The playback can be controlled with Play, Pause, and Resume buttons, ensuring a smooth user experience. If a song is paused, the position is recorded, and it can be resumed from the same spot.

**Volume Control**: A JSlider allows users to adjust the volume of the song in real time, offering easy and interactive control over audio levels.

**Progress Tracking**: A JProgressBar visually tracks the current playback position. The progress is updated every second using a Timer object based on the length of the audio clip. This provides users with an intuitive understanding of how much of the song has played.

**Audio Playback**: The Java Sound API is used to load and play WAV files. The song's file path is retrieved from the database based on the user’s selection. The audio is pre-loaded into a Clip object, allowing it to be played efficiently. The song length is measured in frames, which are then converted to milliseconds to synchronize the progress bar.

**Database Integration**: The application connects to a MySQL database, where song details such as the file path, genre, singer, and year are stored. Upon song selection, the program fetches relevant information (such as song name, file path, and other metadata) and prepares the song for playback.