This program will work for Mac user.
It downloads the list of musics from INPUT_FILE and move to Mac Music App library.

Requirements:
  - Python3 installed.
  - Homebrew: /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install.sh)"
  - YouTubeDL: pip3 install youtube_dl

Input file format:
  - Plain text document
  - URL [|| OPTIONAL_CUSTOM_NAME]

To run the program with custom input file:
  - python3 YouTubeDL.py <input_file_path>

To run the program with defaul input file:
  - python3 YouTubeDL.py
