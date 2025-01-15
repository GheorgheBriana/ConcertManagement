
 # Concert Management :guitar:

**Concert Management** este o aplicație Spring Boot care permite gestionarea concertelor, artiștilor, locațiilor, genurilor muzicale și biletelor.

## Descriere generală

Acest proiect urmărește să ofere un sistem administrare a unui **catalog de concerte**, oferind diferite funcționalități, precum:
- Crearea și listarea concertelor, cu date despre locații și artiști asociați.
- Vânzarea biletelor, cu actualizarea automată a capacității disponibile.
- Gestionarea genurilor muzicale și a artiștilor.
- Crearea descrierilor detaliate pentru fiecare concert.

## Tehnologii și biblioteci :woman_technologist:

- **Java 17 (sau 11+)**
- **Spring Boot** (REST, Data JPA)
- **Hibernate** + **MySQL** (pentru persistarea datelor)
- **Maven** (sau Gradle) pentru build și dependency management
- **Swagger/OpenAPI** (pentru documentarea endpoint-urilor)
- **JUnit + Mockito** (pentru testare)

## Idei de business (10)

1. **Afișare concerte**: Vizualizezi toate concertele disponibile, cu detalii precum dată/ora, locație, capacitate etc.

2. **Crearea unui artist**: Adăugarea un artist nou, cu nume și genurile muzicale asociate.

3. **Editarea detaliilor unei locații**: Poți modifica informațiile unei locații existente (nume, adresă).

4. **Afișare genuri muzicale sortate după ID**: Sistemul returnează toate genurile muzicale ordonate crescător după ID.

5. **Ștergerea descrierii unui concert**: Posibilitatea de a șterge doar descrierea concertului, fără a șterge concertul în sine.

6. **Asociere artist-concert**: Adminul poate adăuga/șterge un artist pentru un concert.

7. **Afișarea biletelor cele mai scumpe**: Vrem să vedem topul biletelor cu preț maxim, pentru anumite secțiuni VIP, etc.

8. **Afișarea utilizatorilor care au cumpărat bilete**: Lista cu userii (prin ID, nume) care au tichete la un concert anume.

9. **Afișarea celor mai populare genuri muzicale**: Genurile cu cei mai mulți artiști asociați apar primele (ex. Rock, Jazz...).

10. **Afișarea concertului cu cele mai multe bilete vândute**: Identificăm concertul cu `capacity - capacityAvailable` cel mai mare.

---

## Funcționalități principale (MVP)

Dintre cele 10 idei, am selectat **5 funcționalități de bază** pentru MVP (Minimum Viable Product):

### 1. Afișarea concertelor
**Descriere**: 
- Utilizatorul poate vizualizarea toate concertele aflate în baza de date. Pentru fiecare concert se afișează detalii precum: numele concertului, data și ora, locația în care se află, capacitatea totală și locuri disponibile. Totodată, un concert poate oferi și informații precum: artiștii care vor cânta la acel concert, detaliile biletelor vândute și informații generale despre concert.
**Scopul oferit**:
- Această funcționalitate permite oricui să vadă ce concerte sunt în desfășurare sau urmează.
- Acesta reprezintă un punct de pornire pentru ca un user să decidă la ce concert dorește să participe și prin urmare, să cumpere bilete. !!!
**Flux de lucru simplificat***:
  - Utilizatorul accesează endpoint-ul GET /concerts
  - Serverul returnează un array/o listă cu obiecte ConcertDTO ce conțin informații despre fiecare concert.
În continuare voi prezenta cum arată acest proces în Postman.
După compilarea programului și asigurarea că nu e există erori, introducem în Postman un request de tip GET către adresa:

![image](https://github.com/user-attachments/assets/2f90391b-64e0-4708-86de-4c307278fac3)

Se apasă SEND, iar dacă totul este configurat corect va apărea un răspuns de tip 200 OK și un array de obiecte JSON care reprezintă lista de concerte aflată în baza de date.

![image](https://github.com/user-attachments/assets/f3b84ac9-317f-43c8-bade-55f23731e5f5)

![image](https://github.com/user-attachments/assets/549d7ce4-fec7-4cab-a19c-d46250206b65)

![image](https://github.com/user-attachments/assets/0b6c2073-2b7e-420f-944b-587ff6d074da)

### 2. Ștergerea unei descrieri asociate concertului






- **Descriere**: orice utilizator poate vedea lista completă de concerte, inclusiv data, locul, capacitatea și locurile disponibile.
- **Scop**: oferă o privire de ansamblu, permițând ulterior cumpărarea de bilete sau administrarea altor detalii.
- **Request Postman**: `GET /concerts`  
  *Exemplu Răspuns (200 OK)*:
  ```json
  [
    {
      "id": 1,
      "name": "Concert Rock",
      ...
    },
    ...
  ]

