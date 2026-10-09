package LLD_Interview_Questions;

public class URL_Shortner {

}

/*
* Functional Requirements:
    * Convert long URL -> short URL
    * Retrieve long URL from the short URL
    * Track click count(optional)
    * Support custom alias(optional)
*
* Non-Functional:
* Fast lookup ->O(1)
* Scalable
* Thread-safe
* Unique short URLs
*
* Core Design Ideas:
* HashMap --> Store Mappings
* Base62 Encoder --> generate short codes
* ID Generator --> Unique IDs
* Service Layer --> Bussiness Logic
*
*
* Class Design:
*
* URLShortner (Main Service)
*   --> URL Repository (Storage)
*   --> Base62Encoder(Encode ID)
*
* Main Classes:
*
* URLShortenerService --> Main API
* UrlRepository --> In-memory DB
* Base62Encoder --> Encode IDs
* UrlMapping --> Entity
* IdGenerator --> Generate IDs
*
* */


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

class UrlShortenerService {

    private static final String DOMAIN = "http://short.ly/";

    private final UrlRepository repository;
    private final Base62Encoder encoder;
    private final IdGenerator idGenerator;
    
    public UrlShortenerService() {
        this.repository  = new UrlRepository();
        this.encoder     = new Base62Encoder();
        this.idGenerator = new IdGenerator();
    }

    //Create short URL
    public String shortenUrl(String longUrl){
        long id = idGenerator.generateId();
        String code = encoder.encode(id);

        String shortUrl = DOMAIN + code;

        UrlMapping mapping = new UrlMapping(
            id,
            longUrl,
            shortUrl
        );

        repository.save(mapping);
        return shortUrl;

    }

    //Get Original URL
    public String getOriginalUrl(String shortUrl) {

        UrlMapping mapping = repository.findByShortUrl(shortUrl);
        if(mapping == null){
            return null;
        }
        mapping.incrementClicks();

        return mapping.getLongUrl();
    
    }

    //Analytics
    public long getClickCount(String shortUrl){
        UrlMapping mapping = repository.findByShortUrl(shortUrl);
        if(mapping == null){
            return 0;
        }
        return mapping.getClicks();
    }
}

class UrlRepository{
    
    //shortURL --> Mapping
    private final Map<String, UrlMapping> shortToLong = new HashMap<>();

    // id --> Mapping
    private final Map<Long, UrlMapping> idMap = new HashMap<>();

    public synchronized void save(UrlMapping mapping){
        shortToLong.put(mapping.getShortUrl(), mapping);
        idMap.put(mapping.getId(), mapping);

    }
    public synchronized UrlMapping findByShortUrl(String shortUrl) {
        return shortToLong.get(shortUrl);
    }
}

class UrlMapping {

    private long id;
    private String longUrl;
    private String shortUrl;
    private long clicks;

    public UrlMapping(long id, String longUrl, String shortUrl) {
        this.id = id;
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.clicks = 0;
    }

    public long getId() {
        return id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public long getClicks() {
        return clicks;
    }

    public void incrementClicks() {
        clicks++;
    }
}



//ID Generator
class IdGenerator {
    private final AtomicLong counter = new AtomicLong(100000);
    public long generateId() {
        return counter.incrementAndGet();
    }
}

// Base62 Encoder
class Base62Encoder {
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String encode(long num){
        StringBuilder sb = new StringBuilder();

        while(num > 0){
            int rem = (int) (num % 62);
            sb.append(BASE62.charAt(rem));
            num = num/62;
        }

        return sb.reverse().toString();
    }
}


/*
Time complexity:
ShortURL --> O(1)
getURL -- O(1)
save  -- O(1)
Space --> O(N)
HashMap
base62
AtomicLong
RepositoryLayer
Entity Class


How will you scale?
1. Database
2. Distributed IDs

Use:
    Snowflake ID
    UUID
    Zookeeper

cachine:
    Add Redis for Hot URLs

    shortenUrl(Strin longURL, )


*/