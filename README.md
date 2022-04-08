# Corona-Konferenz-Assistent

## Überblick

Mit dem Corona-Konferenz-Assistenten lassen sich Meetings in Person tracken und man wird in der Vorbereitung und dem Abhalten dieser unterstützt. Sie kombiniert Kontakttagebuch mit Checkliste und Erinnerungen für das Corona-Regel-konforme Abhalten persönlicher Meetings.

Mithilfe der App sollen in Zeiten von Corona persönliche Meetings so sicher wie möglich abgehalten werden können.

Sie wendet sich jede Person, die ihre persönlichen Meetings sicherer gestalten möchte, empfohlen wird sie besonders für Teamleiter von Arbeitsgruppen.

[**Detailliertere Übersicht über die App und ihre Features**](https://github.com/ASE-Projekte-WS-2021/ase-ws-21-konferenzassistent/blob/main/documentation/folien.pdf)

## Baue App aus Quellcode

- Klone Repository mit `git clone https://github.com/ASE-Projekte-WS-2021/ase-ws-21-konferenzassistent.git`
- Öffne geklontes Repository mit Android Studio (getestet unter Version Bumblebee | 2021.1.1 Patch 2)
- Wähle `Build > Build Bundle(s) / APK(s) > Build APK(s)`
- Gebaute APK befindet sich im Unterordner `app/build/outputs/apk/debug` unter dem Namen `app-debug.apk`

## Team

Oft überschneiden sich Aufgabenbereiche, da wir viel mit Pair programming gearbeitet haben. Die Schwerpunkte der einzelnen Mitglieder waren: 

### Johannes Hoffmann ([@jmhoffmann9612](https://github.com/jmhoffmann9612), [@Syhans](https://github.com/Syhans))

- Organisation allgemein
- Meetingsfilter, Layout-Design
- Implementation RecyclerViews

### Maximilian Huber ([@MaximilianHuber](https://github.com/MaximilianHuber))

- Planung und Implementierung der Datenbank
- Bugs finden und fixen/vorbeugen
- Informationsbuttons 

### Yu Liu ([@Leosssss](https://github.com/Leosssss))

- Quality Assurance
- Layout-Design

### Tobias Zels ([@TobiasZels](https://github.com/TobiasZels))

- Softwarearchitektur
- Implementation allgemein
- Countdowns, Services, Custom Checklisten/Countdowns
