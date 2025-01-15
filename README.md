z
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

Dintre cele 10 idei, am selectat **5 funcționalități de bază** pentru MVP (Minimum Viable Product). Aceste funcționalități le voi demonstra cu ajutorul programului Postman.

### 1. Afișarea concertelor
**Descriere**: 
- Utilizatorul poate vizualizarea toate concertele aflate în baza de date. Pentru fiecare concert se afișează detalii precum: numele concertului, data și ora, locația în care se află, capacitatea totală și locuri disponibile. Totodată, un concert poate oferi și informații precum: artiștii care vor cânta la acel concert, detaliile biletelor vândute și informații generale despre concert.
**Scopul oferit**:
- Această funcționalitate permite oricui să vadă ce concerte sunt în desfășurare sau urmează.
- Acesta reprezintă un punct de pornire pentru ca un user să decidă la ce concert dorește să participe și prin urmare, să cumpere bilete. !!!
**Flux de lucru simplificat***:
  - Utilizatorul accesează endpoint-ul GET /concerts
    
![image](https://github.com/user-attachments/assets/2f90391b-64e0-4708-86de-4c307278fac3)â

  - Serverul returnează un array/o listă cu obiecte ConcertDTO ce conțin informații despre fiecare concert
    
![image](https://github.com/user-attachments/assets/f3b84ac9-317f-43c8-bade-55f23731e5f5)

![image](https://github.com/user-attachments/assets/549d7ce4-fec7-4cab-a19c-d46250206b65)

![image](https://github.com/user-attachments/assets/0b6c2073-2b7e-420f-944b-587ff6d074da) 

### 2. Ștergerea unei descrieri asociate concertului
**Descriere**: 
- Utilizatorul poate șterge o descriere asociată unui concert specific, folosind endpointul său. Aceasta permite eliminarea informațiilor dorite despre un concert
**Scopul oferit**:
- Ajută la menținerea bazei de date curate și la actualizarea informațiilor, eliminând descrierile nevalide sau care nu sunt necesare.
**Flux de lucur simplificat**:
- Utilizatorul accesează endpoint-ul DELETE /concert-descriptions/{id}
În acest caz, vom șterge descriere care face legătură cu concertul care are ID-ul 1 !!!!!!!!!!!!!!!!!!

![image](https://github.com/user-attachments/assets/b91ae7b6-fba4-4bc7-a6a8-21f5a7ae42d3)

- Serverul identifică descrierea cu ID-ul specificat și o șterge.
  
![image](https://github.com/user-attachments/assets/a73b2fb1-9fc9-4c3e-92fe-83accdc00907)

- Dacă descrierea nu există, serverul returnează un mesaj de eroare.
  
![image](https://github.com/user-attachments/assets/3f6dce1d-d841-4db5-a32e-37ec8c6ea04a)

### 3. Crearea unei locații
**Descriere**:
- Utilizatorul poate adăuga o locație nouă în baza de date, oferind informații precum numele locației, adresa și capacitatea totală.
**Scopul oferit**:
Permite adăugarea rapidă de locații noi pentru organizarea concertelor, actualizând datele aplicației în funcție de nevoi.
**Flux de lucru simplificat**:
- Utilizatorul accesează endpoint-ul POST /locations.
  
![image](https://github.com/user-attachments/assets/8168978a-35fb-43cc-bf82-501d0cff9338)

- Serverul primește și validează corpul cererii JSON pentru a crea locația.
- Dacă cererea este validă, locația este creată și se returnează un răspuns 201 Created.
  
 ![image](https://github.com/user-attachments/assets/e7c4e497-70fc-4bd0-b412-9866024c8af9)

### 4. Crearea unui artist nou
**Descriere:**
- Utilizatorul poate adăuga un artist nou în baza de date, specificând numele acestuia și genurile muzicale asociate.
**Scopul oferit**:
- Facilitează extinderea catalogului de artiști disponibili pentru concerte.
**Flux de lucru simplificat**:
- Utilizatorul accesează endpoint-ul POST /artists.
  
![image](https://github.com/user-attachments/assets/e9ef9095-83a8-4b4c-b966-187d40c77cd1)

- Serverul primește și validează corpul cererii JSON.
- Artistul este creat și răspunsul include informațiile despre artistul adăugat.
  
![image](https://github.com/user-attachments/assets/f43bc927-1b02-44b9-8a6a-4a4cf48933d1)

### 7. Editarea detaliilor unei locații
**Descriere:**
- Utilizatorul poate edita informațiile unei locații existente, cum ar fi numele, adresa sau capacitatea maximă. Această funcționalitate este utilă pentru actualizarea datelor despre locații deja existente în baza de date.
**Scopul oferit**:
- Ajuta la actualizarea informțiilor necesare din baza de date despre locații, ceea ce este esențial pentru gestionarea concertelor și planificarea evenimentelor.
**Flux de lucru simplificat**:
- Utilizatorul accesează endpoint-ul PUT /locations/{id}, unde {id} reprezintă ID-ul locației care se dorește a fi editată.
  
  Pentru a demonstra acest lucru, prima data vom afișa detaliile aflate deja în baza de date pentru locația cu ID-ul
  
![image](https://github.com/user-attachments/assets/35b9fcec-2a25-478a-92ff-1b2bec1b4066)
 În continuare, folosim endpointul PUT /locations/7 pentru a introduce datele corecte pentru locația cu ID-ul 4.
- Serverul preia informațiile din request, actualizează locația corespunzătoare în baza de date și returnează locația actualizată
  
![image](https://github.com/user-attachments/assets/358cd973-c240-4c08-b00d-35dc1e03b174)


