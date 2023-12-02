# lib-short-id
Librairie to generate a url shortId unique from an UUID

Exemple of generate shortId : ```auSQ5rwLslP1bYOOE4hPuH```
Useful for short links to a resource link QRCode.
This shortId is reversible and can retrieve the original id (Long or UUID).

## Usage with UUID

- Generate a unique random shortId (based on a UUID) :

```
String shortId = ShortIdGenerator.build().fromRandomUUID() 
```

- Generate a shortId from an UUID

```
String shortId = ShortIdGenerator.build().fromUUID(UUID.randomUUID())
```

- Retrieved the original UUID

```
UUID uuid = shortIdGenerator.convertShortIdToUUID(shortId)
```

- Override the default char list used

```
String myCharList = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_#"
ShortIdGenerator.build(charList).fromRandomUUID() 
```

## Usage with Long id

- Generate a shortId from a random Long

```
String shortId = ShortIdGenerator.build().fromRandomLong() 
```

- Generate a shortId from a Long

```
String shortId = ShortIdGenerator.build().fromLong(3456789L) 
```

- Retrieve the original Long from the shortId

```
Long retrievedLong = shortIdGenerator.convertShortIdToLong(shortId); 
```