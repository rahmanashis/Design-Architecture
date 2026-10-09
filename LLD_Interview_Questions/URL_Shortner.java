
/*
URLShortener:

Description of the problem:
 - long url --> hard to share..
 - be convert a long url to short url, easy to share
Example:
 - linkedin
 - tiny.url
 -

 shortURL -->
 	https://tinyurl.com/z8f4d6n5 ---> mapped to the long URL --> google.doc..asdasasaads



FR: what the MVP functionalities/features/action
	what is the product doing for the user


----------------------THINKING-----------------------------
Functional Requirements:
1. take a longURL and return a short url
2. get the longURL redirect from the shortURL.
3. custom url option to users
4. domain also
5. generate QR for the link
6. custom expiry time provided by the users
7. click Analytics feature


Non-Functional Requirements:
	Examples:
		low latency
			--> O(1) Fast lookup
		extentible
		Thread safe
		Short URL should be unique
		security
		scalable
		SOLID Principle
		throughput
		rate limiting

-----------------------------------------------------------

Requirements:
Functional Requirements:
	1. shorten a long URL --> return the short URL
	2. Redirect(302) a short URL --> original URL
	3. optional: custom alias


Non-Functional:
	short URL should be unique
	Base62 Encoder
	Storage(Map)
	Service Layer


High Level Flow:
Long URL ---> generate ID --> encode Base62 --> store --> return shortURL

short URL --> decode. ---> fetch from map --> return Original URL



Classes:
UrlShortnerService




















google.com/dasdaskldasljdkadjalksdad/dasakljdklsdjlakdjlad/dalkjjdasldjlak/
1. Random Generator
		--> collision -->
2. Hashing

3. Base62
	[a-z][A-Z][0-9]

id:

---> longNumber ---> unique
			15 --> 111
			16 	--->


100000 -->

log62(123456)


id: 1

1/62 ---> 1
2
3
4
5
6
7
8
9
10




1 2 3 4 5 6 7 8 9

62^4

100 Base10

			Base16


100 / 16 	---> 4
6 /16 		---> 6

64(Base16) == 100(base10)


15 --> base2

15/2.  ---> 1
7/2.    ---> 1

3/2. -----> 1



12332
31231
Base16



100000
 --->  azxdsereq
100001 --> e23923498


mr2vxw7w

Client --- ShortURL ---> Application ------- Google.com

							URL
								-- longURL
										--- google.com



URLShortnerService
URLRepository
base62 Encoder
IdGenerator
UrlMapping


client ---- service ---> urlRepository
				    ---> base62
				    ---> IdGenerator
				    --> UrlMapping

			API:
				--> getShortUrl(String longURL)
				---> getLongUrl(String shortURL)

			Domain:	http:/short.ly/
*/



//observer design pattern..
interface EncodingStretegy{
    String encode(long id);
}

class Base62Encoder implements EncodingStretegy{

    private static final String BASE62 = "0 - 61";

    @Override
    public String encode(long id){	//10109120
        StringBuider sb = new StringBuilder();

        while(id > 0){
            sb.append(BASE62.charAt((int)id%62));
            id /= 62;
        }
        return sb.reverse().toString();
    }
}

class SimpleHashing implements EncodingStretegy{
    //someWork here
    public String encode(long id){	//10109120

        //write the logic for this method
        return "long url"
    }
}



interface observers{
    void update(String shortURL);	// which shortURL is hit
    void alert(User user); // when a specific longURL a person is trying to convert..
}


class ClickAggregator implements observers{
    void update(String shortURL){
        System.out.println("shortURL is updated/clickedasdasdasdasdasd");
    }
    void alert(User user){
        //this user is illgal,,....
    }
}


interface Subject{
    void addObserver(observers o);
    void removeObserver(observers o);
    void notify();
}

class UrlShortnerService implements Subject{
//Assumption: ignore qualifiers..

    private List<observers> list_observer = new ArrayList<>();
    private static final String DOMAIN = "http://short.ly/"
    private final UrlRepository repository;
    private final IdGenerator idGenerator;
    // private final Base62Encoder base62Encoder; //encoding //before stretegy design pattern
    private EncodingStretegy encodingStretegy;

    public UrlShortnerService() {
        this.repository  = new UrlRepository();
        this.idGenerator = new IdGenerator();
        this.encodingStretegy 	= new Base62Encoder();
    }

    public void setStretegy(EncodingStretegy encodingStretegy){
        this.encodingStretegy 	= encodingStretegy;
    }


    public String getShortUrl(String longUrl){

        if(long_to_short.containsKey(longUrl))
            return long_to_short.get(longUrl);

        long id = idGenerator.getNextId();
        String shortKey = base62Encoder.encode(id); //av13xfe3
        String shortUrl = DOMAIN + shortKey;
        UrlMapping mapping = new UrlMapping(shortUrl, longUrl);
        repository.save(shortKey, mapping);

        return shortUrl;
        //get the code and put in the repository
        // mapping : shortUrl -- longURL

    }
    public String getLongUrl(String shortUrl){
        UrlMapping mapping = repository.get(shortUrl);
        if(mapping != null){
            // notify the observer
            return mapping.getLongUrl(shortUrl);
        }

    }
    void addObserver(observers o){
        observers.add(o);
    }
    void removeObserver(observers o){
        observers.remove(o);
    }
    void notify(NotificationType type){

    }


}


/*
	URLRepository
	base62 Encoder
	IdGenerator
	UrlMapping
*/

class UrlRepository{

    private final Map<String, UrlMapping> map = new HashMap<>();    //
    private final Map<String, String> long_to_short = new HashMap<>();
    public void save(String  shortUrl, urlMapping mapping){
        map.put(shortUrl, mapping);
        long_to_short.put(mapping.getLongUrl(), mapping.getShortUrl());
    }

    public UrlMapping get(String shortUrl){
        if(map.containsKey(shortUrl))
            return map.get(shortUrl);
        else{
            return null;
        }
    }
}

class IdGenerator {
    private final AtomicLong counter = new AtomicLong(100000);

    public long getNextId() {
        return counter.getAndIncrement();
    }
}




/*

shortURL
longURL
clickCount
*/

class UrlMapping {
    private final String longUrl;
    private final String shortURL;
    private final int clickCount;

    public UrlMapping(String shortURL, String longURL){
        this.longUrl  = longURL;
        this.shortURL = shortURL;
        this.clickCount = 0;
    }

    public long getShortUrl(){
        return shortURL;
    }

    public long getLongUrl(){
        return longURL;
    }
    public void clickCountIncrement(){
        clickCount++;
    }
    public int getClickCount(){
        return clickCount;
    }
}




/*
Add some patterns here..

1. Stretegy design Pattern: Base62 is hardcorded and it will be difficult to change
2. Observer design pattern

*/