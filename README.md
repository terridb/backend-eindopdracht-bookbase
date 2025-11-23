# BookBase - Backend Eindopdracht

## Inhoudsopgave

1. [Inleiding](#1-inleiding)
2. [Web-API en functionaliteit](#2-web-api-en-functionaliteit)
3. [Projectstructuur en gebruikte technieken](#3-projectstructuur-en-gebruikte-technieken)
4. [Benodigdheden](#4-benodigdheden)
5. [Installatie instructies](#5-installatie-instructies)
6. [Tests](#6-tests)
7. [Testgebruikers](#7-testgebruikers)

## 1. Inleiding

BookBase is een web-API voor het beheren van alle belangrijke processen binnen een bibliotheek.
De API bundelt alle belangrijke processen waar een bibliotheek mee te maken krijgt: het beheren van de collectie, het
registreren van uitleenacties, reserveringen en het afhandelen van boetes.

## 2. Web-API en functionaliteit

De webAPI biedt de volgende kernfunctionaliteiten:

1. Leden kunnen zich inloggen en registreren.
2. Bibliothecarissen kunnen de collectie beheren (boeken toevoegen, wijzigen, verwijderen) en boekcovers uploaden.
3. Leden kunnen boeken zoeken, reserveren, verlengen en hun geleende boeken inzien.
4. Medewerkers kunnen boeken uitlenen, innemen, reserveringen beheren, een reserveringsoverzicht downloaden en boetes
   registreren/afhandelen.

## 3. Projectstructuur en gebruikte technieken

### 3.1 Projectstructuur

```
 src
  ├───main
  │   ├───java
  │   │   └───com
  │   │       └───terrideboer
  │   │           └───bookbase
  │   │               ├───configuration
  │   │               ├───controllers
  │   │               ├───dtos
  │   │               │   ├───authentication
  │   │               │   ├───authors
  │   │               │   ├───bookcopies
  │   │               │   ├───books
  │   │               │   ├───fines
  │   │               │   ├───loans
  │   │               │   ├───reservations
  │   │               │   └───users
  │   │               ├───exceptions
  │   │               ├───filters
  │   │               ├───mappers
  │   │               ├───models
  │   │               │   └───enums
  │   │               ├───repositories
  │   │               ├───services
  │   │               └───utils
  │   └───resources
  │       ├───static
  │       └───templates
  └───test
      ├───java
      │   └───com
      │       └───terrideboer
      │           └───bookbase
      │               └───services
      └───resources
```

### 3.2 Gebruikte technieken

- **Java 21**
- **Spring Boot 3.5.7**
    - Web
    - Data JPA
    - Validation
    - Security
    - Test
- **Maven**
- **PostgreSQL**
- **OpenPDF 3.0.0**
- **JJWT 0.11.5**
- **H2**

## 4. Benodigdheden

Om de web-API te kunnen draaien, heb je het volgende nodig:

- **Java 21**
- **Een IDE**, zoals <a target="_blank" href="https://www.jetbrains.com/idea/">IntelliJ</a>
- **PostgreSQL**, inclusief database
- **pgAdmin**
- **Postman**

## 5. Installatie instructies

**Stap 1**: Zorg ervoor dat je alle benodigdheden hebt geinstalleerd.

**Stap 2**: Clone de Github repository met behulp van onderstaande SSH link naar jouw locale machine.

```shell
git@github.com:terridb/backend-eindopdracht-bookbase.git
```

Indien je IntelliJ gebruikt:

1. Ga naar File > New > Project from version control...
2. Vul bovenstaande link in
3. Druk op Clone

Indien je een andere IDE gebruikt, raadpleeg de instructies van deze IDE om de repository te kunnen clonen.

**Stap 3**: Maak een nieuwe database binnen PgAdmin aan door met de rechtermuisknop op "Databases" te klikken en naar
Create > Database... te gaan. Geef deze database een naam: "bookbase".

**Stap 4**: Ga binnen de IDE naar `application.properties` via src > main > resources.
Stel de volgende waarden in:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bookbase
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD
jwt.secret=YOUR_GENERATED_SECRET
```

Let op:

- Zorg ervoor dat je bij `spring.datasource.password` het wachtwoord invult dat je gekozen hebt tijdens de installatie
  van PostgreSQL.
- De web-API gebruikt JWT voor authenticatie. Hiervoor is een secret key nodig. Maak [hier](https://jwtsecrets.com/) een
  secret key aan en voeg deze toe aan `application.properties`.

**Stap 5**: Run de applicatie door binnen IntelliJ op "Run BookbaseApplication" te klikken. De applicatie zou nu moeten
draaien op http://localhost:8080.

## 6. Tests

Voor het testen binnen deze applicatie wordt gebruikgemaakt van:

- **Spring Boot Test**
- **JUnit**
- **Mockito**
- **H2**

Om zowel de unit- als integratie-testen uit te voeren, kun je met de rechtermuisknop klikken op de volgende map:
test > java. Selecteer daarna "Run all tests".

## 7. Testgebruikers

De web-API bevat 13 vooraf aangemaakte testgebruikers, zodat alle endpoints efficiënt getest kunnen worden in Postman.
12 van deze gebruikers zijn hieronder, inclusief bijbehorende inloggegevens, vermeld. Eén account is voor een delete-test
gereserveerd en is daarom niet opgenomen.

| Role      | Email                      | Password |
|-----------|----------------------------|----------|
| Member    | karel.kiwi@gmail.com       | test     |
| Member    | mara.mango@gmail.com       | test     |
| Member    | barrie.bosbes@gmail.com    | test     |
| Member    | pino.pruim@gmail.com       | test     |
| Member    | ella.eland@gmail.com       | test     |
| Member    | noah.noten@gmail.com       | test     |
| Member    | lisa.langpootmug@gmail.com | test     |
| Employee  | bob.sleejen@bookbase.nl    | test     |
| Employee  | sia.later@bookbase.nl      | test     |
| Librarian | ben.weg@bookbase.nl        | test     |
| Librarian | frits.frituur@bookbase.nl  | test     |

Deze gebruikers zijn bedoeld om endpoints met verschillende autorisatieniveaus te testen (Member, Employee, Librarian)
en om data te leveren aan endpoints zoals loans en reservations.