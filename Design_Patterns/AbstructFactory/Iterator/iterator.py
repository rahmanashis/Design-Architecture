from abc import ABC,abstractmethod

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
    # def get_playlist(self): This was in without iterator
    #     return self.__playlist
    def create_iterator(self):
            return PlaylistIterator(self.__playlist)

class Iterator:
    @abstractmethod
    def has_next(self) -> bool:
        pass
    @abstractmethod
    def next(self) -> Song | None:
        pass


class PlaylistIterator(Iterator):
    def __init__(self,song_list):
        self.__song_list = song_list
        self.__position = 0
    def has_next(self):
        if self.__position < len(self.__song_list):
            return True
        return False
    
    def next(self) -> Song | None:
        while self.has_next() == True:
            song = self.__song_list[self.__position]
            self.__position += 1
            return song
        return None


playlist = PlayList()
playlist.add_song(Song("song1"))
playlist.add_song(Song("song2"))
playlist.add_song(Song("song3"))
playlist.add_song(Song("song4"))

iterator = playlist.create_iterator()

while iterator.has_next():
    song = iterator.next()
    if song is not None:
        print(song.get_title())