from __future__ import unicode_literals
import youtube_dl
import os
import sys

if sys.platform == 'win32':
	TEMPO_DIR = 'TempMusics\\'
	MUSIC_DIR = os.environ['USERPROFILE'] + '\\Music\\YouTubeDL'
	if not os.path.exists(MUSIC_DIR):
		os.system('mkdir "' + MUSIC_DIR + '"')
	CMD = 'move ' + TEMPO_DIR + '* "' + MUSIC_DIR + '"'
elif sys.platform == 'darwin':
	TEMPO_DIR = 'TempMusics/'
	MUSIC_DIR = os.environ['HOME'] + '/Music/iTunes/iTunes Media/Automatically Add to Music.localized/'
	CMD = 'mv ' + TEMPO_DIR + '* "' + MUSIC_DIR + '"'

if len(sys.argv) == 2:
    input_file = sys.argv[1]
else:
    input_file = 'input.dat'

with open(input_file, 'r') as fp:
    lines = fp.readlines()

ydl_opts = {
    'format': 'bestaudio/best',
    'postprocessors': [{
        'key': 'FFmpegExtractAudio',
        'preferredcodec': 'mp3',
        'preferredquality': '192',
    }],
}

failedSongs = []
for line in lines:
    infos = line.strip().split('||', 1)
    url = infos[0].strip()

    if len(infos) == 2:
        outputName = infos[1].strip()
    else:
        outputName = ''

    if outputName:
        ydl_opts['outtmpl'] = TEMPO_DIR + outputName + '.%(ext)s'
    elif 'outtmpl' in ydl_opts:
        ydl_opts['outtmpl'] = TEMPO_DIR + '%(title)s.%(ext)s'

    try:
        with youtube_dl.YoutubeDL(ydl_opts) as ydl:
            ydl.download([url])
    except:
        failedSongs.append(url + '||' + outputName + '\n')

with open(input_file, 'w') as fp:
    for song in failedSongs:
        fp.write(song)

print(CMD)
os.system(CMD)
