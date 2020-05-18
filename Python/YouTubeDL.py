from pytube import YouTube

OUT_DIR = '/Users/dickfoong/Downloads/'
LINK = 'https://www.youtube.com/watch?v=kTJbE3sfvlI'

yt = YouTube(LINK)
video = yt.streams.first()

video.download(OUT_DIR)
