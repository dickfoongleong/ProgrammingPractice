This program will work for Mac user.
It downloads the list of musics from INPUT_FILE and move to Mac Music App library.

Requirements:
  - Python 3.8 installed
  - YouTubeDL Library: pip3 install youtube_dl
  - FFmpeg: Needed to convert Video to Audio format.
    => For Windows 10:
      => Downloads at https://ffmpeg.zeranoe.com/builds/
      => Extract all and rename the extracted folder to FFmpeg
      => Move FFmpeg to C: or C\"Program File" folder
      => Add FFmpeg/bin to System Environment or User Environment PATH.
    => For macOS Homebrew:
      => Run at terminal: { /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install.sh)" }
      => brew install ffmpeg

Input file format:
  - Plain text document
  - URL [|| OPTIONAL_CUSTOM_NAME]

To run the program with custom input file:
  - python3 YouTubeDL.py <input_file_path>

To run the program with defaul input file:
  - python3 YouTubeDL.py
