function validateEmail(email){
    let validEmailRegex = /^[a-z0-9._-]+@[a-z0-9.-]+\.[a-z]{2,3}$/;
    //validazione email
    if(email.length === 0 || !email.match(validEmailRegex) || email.length > 50) {
        alert("Email non valida!");
        return false;
    }
    return true;
}

function validatePassword(password){
    let validPasswordRegex = /^(?=.*[0-9])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{10,}$/
    //validazione email
    if(password.length === 0 || !password.match(validPasswordRegex) || password.length > 20) {
        alert("Password non valida!");
        return false;
    }
    return true;
}

function validateLogin() {
    let email = document.getElementById("email-login").value;
    let password = document.getElementById("password-login").value;
    return validateEmail(email) && validatePassword(password) ;
}

function validateRegistration() {
    let validFirstName = true, validLastName = true, validGender = true, validType = true, validBday = true;
    //validazione nome e cognome
    let validNameRegex = /^[^0-9!@#$%^&*()]+$/;
    let firstName = document.getElementById("first-name").value;
    if(firstName.length === 0 || !firstName.match(validNameRegex) || firstName.length < 1 || firstName.length > 50) {
        alert("Il nome non può contenere numeri o simboli e non può esser più lungo di 50 caratteri!");
        validFirstName = false;
    }
    let lastName = document.getElementById("last-name").value;
    if(lastName.length === 0 || !lastName.match(validNameRegex) || firstName.length < 1 || lastName.length > 50) {
        alert("Il cognome non può contenere numeri o simboli e non può esser più lungo di 50 caratteri!");
        validLastName = false;
    }

    //validazione sesso
    let gender = document.getElementsByName("gender");
    let selectedGender;
    for (let genderElem of gender)
        if (genderElem.checked) {
            selectedGender = genderElem.value;
            break;
        }
    if(selectedGender === null){
        alert("L'inserimento del genere è obbligatorio!");
        validGender = false;
    } else if(selectedGender !== "M" && selectedGender !== "F"){
        validGender = false;
    }

    //validazione tipo di account
    let type = document.getElementsByName("type");
    let selectedType;
    for (let typeElem of type)
        if (typeElem.checked) {
            selectedType = typeElem.value;
            break;
        }
    if(selectedType === null){
        alert("L'inserimento del tipo di account è obbligatorio!");
        validType = false;
    } else if(selectedType !== "student" && selectedType !== "tutor"){
        validType = false;
    }

    //validazione data di nascita
    let birthdayString = document.getElementById("birthday").value;
    const dateParts1 = birthdayString.split("-");
    let today = new Date();
    let year = today.getFullYear().toString();
    let month = (today.getMonth() + 1).toString().padStart(2, '0');
    let day = today.getDate().toString().padStart(2, '0');
    const dateParts2 = [year, month, day];
    let compare = data(dateParts1, dateParts2);
    if(compare === 0 || compare === 1){
        alert("Data di nascita non valida!");
        validBday = false;
    }

    //validazione email e password
    let email = document.getElementById("email-registration").value;
    let password = document.getElementById("password-registration").value;

    return validFirstName && validLastName && validGender && validType && validBday && validateEmail(email) && validatePassword(password);
}

function data(dateParts1, dateParts2){
    //controllo dell'anno
    if (dateParts1[0] < dateParts2[0])
        return -1; // dateParts1 viene prima di dateParts2
    else if (dateParts1[0] > dateParts2[0])
        return 1; // dateParts1 viene dopo dateParts2

    //controllo dei mesi
    if (dateParts1[1] < dateParts2[1])
        return -1; // dateParts1 viene prima di dateParts2
    else if (dateParts1[1] > dateParts2[1])
        return 1; // dateParts1 viene dopo dateParts2

    //controllo dei giorni
    if (dateParts1[2] < dateParts2[2])
        return -1; // dateParts1 viene prima di dateParts2
    else if (dateParts1[2] > dateParts2[2])
        return 1; // dateParts1 viene dopo dateParts2

    return 0;
}

//validazione dei dati inseriti nel form per completare la registrazione del tutor
function validateCompleteRegistration(){
    //controllo sui titoli
    let titoli = document.getElementsByName("titolo");
    let validTitoliSettoriRegex = /^[a-zA-Z\s']+$/;
    let validTitoli = true;
    let lung = 0, num_elem = 0;
    let alertStringTitoli = "";

    //validazione titoli
    if(titoli.length === 0){ //se i titoli per qualche motivo non sono stato riempiti, manco uno.
        alertStringTitoli = "I campi per i titoli vanno riempiti!";
        validTitoli = false;
    }
    for(let titolo of titoli){
        if (!titolo.value) { //se la stringa è vuota
            alertStringTitoli ="I campi per i titoli vanno riempiti!";
            validTitoli = false;
            break;
        }
        if (!validTitoliSettoriRegex.test(titolo.value)) { //se il titolo non rispetta la regexp
            alertStringTitoli = "Non sono ammessi i caratteri speciali nei titoli!";
            validTitoli = false;
            break;
        }
        num_elem++;
        lung += titolo.length;
    }
    if(num_elem - 1 + lung > 500){ //se il num ti trattini compresi tra le stringhe, insieme alla lunghezza totale, sfora 150
        alertStringTitoli = "Lunghezza dei titoli eccessiva!";
        validTitoli = false;
    }
    if(!validTitoli){
        alert(alertStringTitoli);
        return false;
    }


    //controllo sui settori
    let settori = document.getElementsByName("settore");
    let validSettori = true;
    lung = 0;
    num_elem = 0;
    let alertStringSettori = "";

    if(settori.length === 0){ //se i settori per qualche motivo non sono stato riempiti, manco uno.
        alertStringSettori = "I campi per i settori vanno riempiti!";
        validSettori = false;
    }
    for(let settore of settori){
        if (!settore.value) { //se la stringa è vuota
            alertStringSettori ="I campi per i settori vanno riempiti!";
            validSettori = false;
            break;
        }
        if (!validTitoliSettoriRegex.test(settore.value)) { //se il titolo non rispetta la regexp
            alertStringSettori = "Non sono ammessi i caratteri speciali nei settori!";
            validSettori = false;
            break;
        }
        num_elem++;
        lung += settore.length;
    }
    if(num_elem - 1 + lung > 150){ //se il num ti trattini compresi tra le stringhe, insieme alla lunghezza totale, sfora 150
        alertStringSettori = "Lunghezza dei settori eccessiva!";
        validSettori = false;
    }
    if(!validSettori){
        alert(alertStringSettori);
        return false;
    }


    //controllo sui giorni
    let lun = document.getElementById("lunedì");
    let mar = document.getElementById("martedì");
    let mer = document.getElementById("mercoledì");
    let gio = document.getElementById("giovedì");
    let ven = document.getElementById("venerdì");
    let sab = document.getElementById("sabato");
    let dom = document.getElementById("domenica");

    //se l'utente ha cambiato i valori dei giorni...
    if(lun.value !== "lunedì" || mar.value !== "martedì" || mer.value !== "mercoledì" || gio.value !== "giovedì" || ven.value !== "venerdì" || sab.value !== "sabato"|| dom.value !== "domenica" )
        return false;
    //se nessuno è stato checkato
    if(!lun.checked && !mar.checked && !mer.checked && !gio.checked && !ven.checked && !sab.checked && !dom.checked ){
        alert("Devi inserire almeno un giorno di disponibilità!");
        return false;
    }

    //validazione tariffa oraria
    let tariffaOraria = document.getElementById("tariffaOraria");
    if(tariffaOraria === null){
        alert("Valore della tariffa oraria non valido!");
        return false;
    } else if(tariffaOraria.value < 1 || tariffaOraria.value > 100 || isNaN(tariffaOraria.value)){
        alert("Valore della tariffa oraria non valido!");
        return false;
    }

    //controllo sulla descrizione della card e quella completa
    let descrizioneCard = document.getElementById("descrizione-card").value;
    if(descrizioneCard.length === 0 || descrizioneCard.length < 50 || descrizioneCard.length > 150){
        alert("Descrizione card non valida!");
        return false;
    }
    let descrizioneCompleta = document.getElementById("descrizione-completa").value;
    if(descrizioneCompleta.length === 0|| descrizioneCompleta.length < 200 || descrizioneCompleta.length > 1500){
        alert("Descrizione completa non valida!");
        return false;
    }

    return true;
}

function validateBookLesson(){
    let email = $("#tutorEmail").val();
    let validNumOrePren = true, validSettore = true;

    //validazione numero di ore prenotate
    let numOrePrenotate = document.getElementById("numOrePrenotate");
    if(numOrePrenotate.value < 1 || isNaN(numOrePrenotate.value)){
        alert("Devi prenotare almeno un'ora per la lezione!");
        validNumOrePren = false;
    }

    //validazione settore prenotato
    let validSettoreRegex = /^[a-zA-Z\s']+$/;
    let settore = document.getElementsByName("materiaLezione");
    let selectedSettore;

    for (let settoreElem of settore)
        if (settoreElem.checked) {
            selectedSettore = settoreElem.value;
            break;
        }
    if(selectedSettore === null){
        alert("L'inserimento della materia è obbligatorio!");
        validSettore = false;
    } else if(selectedSettore.length === 0 || !validSettoreRegex.test(selectedSettore)){
        alert("Materia non valida!");
        validSettore = false;
    }

    return validateEmail(email) && validNumOrePren && validSettore;
}

function validateChangeNameStudent(){
    let email = $("#changeNameStudentEmail").val();
    return validateEmail(email);
}

function validateChangeNameTutor(){
    //validazione email
    let email = $("#changeNameTutorEmail").val();
    return validateEmail(email);
}

function validateChangeName(){
    let email = $("#changeNameEmail").val();
    let validFirstName = true, validLastName = true;

    //validazione nome e cognome
    let validNameRegex = /^[^0-9!@#$%^&*()]+$/;
    let newFirstName = $("#newFirstName").val();
    let newLastName = $("#newLastName").val();
    if(!newFirstName || !newFirstName.match(validNameRegex) || newFirstName.length < 1 || newFirstName.length > 50) {
        alert("Il nome non può contenere numeri o simboli e non può esser più lungo di 50 caratteri!");
        validFirstName = false;
    }
    if(!newLastName || !newLastName.match(validNameRegex) || newLastName.length < 1 || newLastName.length > 50) {
        alert("Il cognome non può contenere numeri o simboli e non può esser più lungo di 50 caratteri!");
        validLastName = false;
    }

    return validateEmail(email) && validFirstName && validLastName;
}

function validateChangeDays(){
    let lun = document.getElementById("newLunedì");
    let mar = document.getElementById("newMartedì");
    let mer = document.getElementById("newMercoledì");
    let gio = document.getElementById("newGiovedì");
    let ven = document.getElementById("newVenerdì");
    let sab = document.getElementById("newSabato");
    let dom = document.getElementById("newDomenica");

    //se l'utente ha cambiato i valori dei giorni...
    if(lun.value !== "lunedì" || mar.value !== "martedì" || mer.value !== "mercoledì" || gio.value !== "giovedì" || ven.value !== "venerdì" || sab.value !== "sabato"|| dom.value !== "domenica" )
        return false;
    //se nessuno è stato checkato
    if(!lun.checked && !mar.checked && !mer.checked && !gio.checked && !ven.checked && !sab.checked && !dom.checked ){
        alert("Devi inserire almeno un giorno di disponibilità!");
        return false;
    }

    return true;
}

function validateChangeHRate(){
    //validazione tariffa oraria
    let tariffaOraria = document.getElementById("newTariffaOraria");
    if(tariffaOraria === null){
        alert("Valore della tariffa oraria non valido!");
        return false;
    } else if(tariffaOraria.value < 1 || tariffaOraria.value > 100 || isNaN(tariffaOraria.value)){
            alert("Valore della tariffa oraria non valido!");
            return false;
    }

    return true;
}

function validateChangeCardDescr(){
    let newCompleteDescription = $("#newCardDescription").val();
    if(newCompleteDescription === null || newCompleteDescription.length < 50 || newCompleteDescription.length > 150){
        alert("Descrizione card non valida!");
        return false;
    }

    return true;
}

function  validateChangeCompleteDescr(){
    let newCompleteDescription = $("#newCompleteDescription").val();
    if(newCompleteDescription === null || newCompleteDescription.length < 200 || newCompleteDescription.length > 1500){
        alert("Descrizione completa non valida!");
        return false;
    }
    return true;
}

function validateChangeCardDescrAdmin(){
    //validazione email
    let email = $("#changeCardDescrEmail").val();
    return validateEmail(email);
}

function  validateChangeCompleteDescrAdmin(){
    //validazione email
    let email = $("#changeComplDescrEmail").val();
    return validateEmail(email);
}

function validateRating(ratingValue) {
    if (!ratingValue.length || isNaN(ratingValue) || ratingValue < 0 || ratingValue > 5) {
        alert("Seleziona una valutazione valida!");
        return false;
    }
    return true;
}

function validateEmailBan(id){
    let email = $("#" + id).val();
    return validateEmail(email);
}

function aggiungiTitolo(){
    const elem1 = document.createElement("input");
    elem1.type= "text";
    elem1.name= "titolo";
    elem1.required=true;
    document.getElementById("titoli").appendChild(elem1);
    const elem2 = document.createElement("br");
    document.getElementById("titoli").appendChild(elem2);
}

function aggiungiSettore(){
    const elem1 = document.createElement("input");
    elem1.type= "text";
    elem1.name= "settore";
    elem1.required=true;
    document.getElementById("settori").appendChild(elem1);
    const elem2 = document.createElement("br");
    document.getElementById("settori").appendChild(elem2);
}

function validateTutorEmailCard(){
    let email = $("#tutorEmail").val();
    return validateEmail(email);
}