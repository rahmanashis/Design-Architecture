class Song:
    def __init__(self,title:str):
        self.__title = title
    def get_title(self):
        return self.__title

class PlayList:
    def __init__(self):
        self.__playlist = []
    def add_song(self,s:Song):
        self.__playlist.append(s)
    def get_playlist(self):
        return self.__playlist


pl = PlayList()
pl.add_song(Song('Song1'))
pl.add_song(Song('Song2'))
pl.add_song(Song('Song3'))
pl.add_song(Song('Song4'))

for i in range(len(pl.get_playlist())):
    print(pl.get_playlist()[i].get_title())

#output
# Song1
# Song2
# Song3
# Song4

""" The problem is here I used playlist as List. So it is possible to get the index.
But what for set or linkedlist?
for them user have to know the internel code to get the title
but user should not know about the code inside the package
So we have the build something other so that user can get the list of songs without knowing the internal code
So we can make some method so that by calling them user can get the list of songs"""