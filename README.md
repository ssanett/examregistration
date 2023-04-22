# Vizsgaremek - terembeosztás applikáció

## Leírás


A vizsgaremek egy vizsgára történő terembeosztást valósít meg. Az applikácicóban létrehozhatóak diákok, termek és 
vizsgáztatók. Ezenkívül az ezeket kezelő funkciókat valósítja meg a teljesség igénye nélkül. Hozzá lehet rendelni
vizsgáztatóhoz termet, diákhoz termet valamint ezeket módosítani lehet. Valamint megvalósítható ezek törlése és felvétele is.
A diák terembe helyezésénél figyelembe veszi a teremben zajló vizsgatárgyakat illetve a terem kapacitását. A vizsgáztató és
a terem összekapcsolásánál pedig, hogy a terem rendelkezik-e már vizsgáztatóval, valamint hogy a vizsgáztató tantárgya 
megegyezik-e a teremben zajló vizsgával. 


## Használata


### Room (Entitás)

- **`id`**
- **`number`** terem száma
- **`capacity`** terembe maximálisan beosztható diákok száma maximum 20, nem lehet nulla
- **`subject`** teremben zajló vizsa tantárgya
- **`students`** terembe beosztott diákok listája


| HTTP metódus | végpont           | leírás |
|--------------|-------------------| -------|
| POST         | "`/api/rooms/create`" | létrehoz egy új termet diákok nélkül|
| GET|"`/api/rooms`" | lekérdezi az összes termet és visszaadja az adataival együtt|
| GET |"`/api/rooms/{number}`" |lekérdezi az adott számú termet és visszaadja az adataival együtt|
| PUT|"`/api/rooms/{roomId}`"|hozzáadja a megadott azonosítójú teremhez a tantárgyat|
|DELETE|"`/api/rooms/{number}`"|törli az adatbázisból a megadott számú termet|
|DELETE|"`/api/rooms/delete/{roomId}`"|törli az adatbázisból a megadott azonosítójú termet|


### Examiner (Entitás)

- **`id`**
- **`firstName`** nem lehet üres
- **`lastName`** nem lehet üres
- **`taughtSubjects`** tantárgyak listája, amit tanít és vizsgáztathat
- **`room`** vizsgaterem

|HTTP metódus|végpont|leírás|
|---|---|---|
|POST|"`/api/examiners`"|létrehoz egy vizsgáztatót|
|PUT|"`/api/examiners/{examinerId}`"|a megadott azonosítójú vizsgáztatóhoz hozzáadja a JSON-ben megadott termet|
|PUT|"`/api/examiners/room/{roomId}`"|a megadott azonosítójú teremhez hozzáadja a JSON-ben megadott vizsgáztatót|
|GET|"`/api/examiners/{examinerId}`"|lekérdezi a megadott azonosítójú vizsgáztót|
|GET|"`/api/examiners`"|lekérdezi az összes vizsgáztatót|
|DELETE|"`/api/examiners/{examinerId}`"|törli a megadott azonosítójú vizsgáztatót|

### Student (Entitás)



- **`id`**
- **`firstName`** nem lehet üres
- **`lastName`** nem lehet üres
- **`subjects`** tanuló vizsgatárgyainak listája
- **`room`** vizsgaterem

|HTTP metódus|végpont|leírás|
|---|---|---|
|GET|"`/api/students/room/{roomNumber}`"|lekérdezi a megadott számú teremben lévő összes tanulót|
|POST|"`/api/students`"|létrehoz egy diákot terem nélkül|
|PUT|"`/api/students/student/{roomId}`"|hozzáadja a megadott azonosítójú teremhez a JSON-ben megadott azonosítójú (már az adatbázisban szereplő) diákot|
|PUT|"`/api/students/{roomId}/{studentId}`"|a megadott azonosítójú teremből törli a megadott azonosítójú diákot|
|DELETE|"`/api/students/{studentId}`"|törli a megadott azonosítójú diákot az adatbázisból|






A `student` és a `room` között kétirányű több-egy, az `examiner` és a `room` között egyirányú egy-egy kapcsolat van.

### Subject (Enum)

-**`value`**

## Technológiai részletek
Ez egy háromrétegű webes alkalmazás controller, service és repository réteggel, minden entitáshoz kapcsolódó, 
a rétegnek megfelelő elnevezésekkel. A megvalósítás JAVA nyelven, Spring Boot keretrendszer használatával történik.
Az alkalmazás képes HTTP kéréseket fogadni, azokat a RESTful webszolgáltatások segítéségvel valósítja meg. 
Adattárolásra SQl alapú MariaDb adatbázist használ, melyben a táblákat Liquibase hozza létre a megfelelő migrációs fájlokkal. 
A tesztelésre a WebClient-tel implementált integrációs tesztek állnak rendelkeésre. A tesztelés külön adatbázistáblákkal történik.
Az alkalmazás tartalma egy Swagger felületet. 




