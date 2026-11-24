### 1. README complet

Explica de manera breu què fa l'aplicació i quines funcionalitats ofereix. Inclou-hi:

- Una descripció del projecte i les tecnologies utilitzades.
- Captures de pantalla de l'aplicació o un GIF curt de l'editor en funcionament.
- Requisits previs, com ara la versió de Java, Maven i MySQL.
- Instruccions pas a pas per configurar la base de dades i executar l'aplicació.
- Enllaços a la documentació tècnica i a les tasques pendents.

### 2. Configuració segura i reproduïble

- No pugis contrasenyes, tokens ni altres secrets al repositori.
- Llegeix la configuració sensible des de variables d'entorn.
- Afegeix un fitxer d'exemple, com ara `.env.example`, sense credencials reals, si el projecte utilitza variables d'entorn.
- Documenta quines variables cal definir i com iniciar la base de dades.
- Prepara scripts o migracions SQL perquè altres persones puguin crear i actualitzar l'esquema de manera reproduïble.

### 3. Proves automatitzades

Afegeix proves per a les funcionalitats principals, especialment l'inici de sessió, la gestió de dibuixos, els permisos i les operacions de l'editor. Documenta com executar-les des de Maven o IntelliJ IDEA.

### 4. Integració contínua

Configura GitHub Actions perquè, en cada `push` i `pull request`, compili el projecte i executi les proves. Això ajuda a detectar errors abans d'integrar canvis.

## Millores de qualitat i documentació

### 5. Codi consistent

Mantén noms clars i un estil coherent. Separa les responsabilitats entre controladors, serveis, models i repositoris, i evita duplicar lògica.

### 6. Documentació tècnica

Descriu l'arquitectura, les classes principals, el model de dades, les dependències i les decisions de disseny. Afegeix un diagrama senzill si ajuda a entendre com es relacionen les parts.

### 7. Llicència i contribucions

Afegeix una llicència que indiqui com es pot reutilitzar el projecte. Si vols acceptar aportacions, inclou un `CONTRIBUTING.md` amb els passos per preparar l'entorn, executar les proves i proposar canvis.


### 8. Demo i captures

Inclou captures actualitzades o una demo curta que mostri les funcions principals. No hi incloguis dades personals ni credencials.

### 9. Versions i releases

Quan tinguis una versió estable, crea una release amb un número de versió, un resum dels canvis i instruccions per executar-la.
