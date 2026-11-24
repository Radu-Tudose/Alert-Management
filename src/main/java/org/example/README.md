Cele 4 Design Pattern-uri folosite sunt:

1. Singleton - folosit pentru Database, din moment ce acest API are nevoie de o singura baza de date, centralizata,
cu toate informatiile intr-un singur loc. Folosind Singleton, se pastreaza unicitatea instantei de Database

2. Builder - cerinta spune explicit ca Server si Location au si atribute optionale, deci Builder, un mod mai simpatic
de a instantia un nou obiect din clase care au atat atribute obligatorii, cat si optionale, se muleaza perfect pe
cerinta (creaza un nou obiect, si adauga eventuale  campuri optionale prezente)

3. Factory - am folosit Factory pentru a instantia un nou obiect de tip User (sau o subclasa a lui User), din
moment ce subclasele sale doar mai contin 1/2 atribute aditionale (e mai dragut sa scrii in Database doar buildUser,
sa i trantesti toate atributele posibile, si o functie in spate sa faca ea distinctia dinte clase)

4. Observer - scria explicit in cerinta ca, atunci cand este creata o noua alerta, serverul legat de alerta trebuie
sa notifice ResourceGroup ul care il monitorizeaza, deci Observer este perfect. am legat un Server de un Group,
si cand este creata o noua Alerta, aceasta este retinuta de Server, iar Group este "notificat", adica alerta
este retinuta intr-un array de alerte primite pentru a simula un log al alertelor 