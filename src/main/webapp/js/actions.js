/*
In questo file sono contenute tutte le operazioni in Javascript per effettuare determinate
azioni logiche al verificarsi di un particolare evento.
*/

function showSuggestions(list, input) {
    $.ajax({
        type: "GET",
        url: "show-suggestions",
        data: {input: input},
        success: function (suggestions) {
            let datalist = $("#" + list);

            datalist.empty();// svuoto la lista di suggerimenti in modo da evitare duplicati o di mostrare vecchi suggerimenti

            suggestions.forEach(function (suggestion) {
                $("<option>").attr("value", suggestion).appendTo(datalist);
            })
        }
    })
}

$(document).ready(function() {
    /* Gestione dei filtri */
    let showMore = 0;
    let filters = { input : "", days : "1|2|3|4|5|6|7", maxprice : 100, sort : "", rating : 0, offset: 0 }
    function applyFilters() {
        $.ajax({
            type: "GET",
            url: "apply-filters",
            data: filters,
            success: function (tutors) {
                let cardContainer = $("#card-container-grid");
                let noResultsContainer = $("#no-results-container");

                if (showMore === 0)
                    cardContainer.empty();// svuoto il container dei tutor in modo da evitare duplicati o di mostrare i tutor precedenti (ciò non avviene se è stato premuto il bottone per caricare altri tutor)
                noResultsContainer.remove();// se esiste, rimuovo il container di errore per evitare di duplicarlo

                if(tutors.length === 0) {
                    let noResultsDiv = $("<div>").attr("id","no-results-container").appendTo("body");
                    $("<img>").attr({src:"img/research-failed.png",alt:"Ricerca fallita."}).appendTo(noResultsDiv);
                    $("<p>").html("Ci dispiace!<br>Non riusciamo a trovare ciò che stai cercando. :(<br>Prova a modificare i <span>filtri</span>!").appendTo(noResultsDiv);
                }
                else {
                    tutors.forEach(function (tutor) {
                        console.log(tutor);
                        let cardDiv = $("<div>").attr("class", "card").appendTo(cardContainer);

                        let imagePath = "propics_by_email/" + tutor.emailUtente.replace(/[^a-zA-Z0-9]/g, "_") + ".jpg";
                        checkForImage(imagePath,function (exists) {
                            if(exists)
                                $("<img>").attr({ src: imagePath, alt: "Immagine profilo del tutor.", class: "card-image"}).appendTo(cardDiv);
                            else
                                $("<img>").attr({src: "img/default-user-icon.jpg",alt: "Immagine profilo del tutor.",class: "card-image"}).appendTo(cardDiv);
                        });

                        let cardButtonDiv = $("<div>").attr("class", "card-button").appendTo(cardDiv);
                        let formDiv = $("<form>").attr({action: "tutor-page", method: "get", onsubmit: "return validateTutorEmailCard()"}).appendTo(cardButtonDiv);

                        $("<input>").attr({type: "hidden", id: "tutorEmail", name: "tutorEmail", value: tutor.emailUtente}).appendTo(formDiv);
                        let button = $("<button>").attr({type: "submit", name: "arrow-button", class: "arrow-icon"}).appendTo(formDiv);

                        $("<img>").attr({src: "img/arrow.png", alt: "Bottone per la pagina del tutor."}).appendTo(button);

                        let cardContentDiv = $("<div>").attr("class", "card-content").appendTo(cardDiv);
                        $("<h3>").text(tutor.nome + " " + tutor.cognome).appendTo(cardContentDiv);
                        let averageRatingDiv = $("<div>").attr("class", "average-rating").appendTo(cardContentDiv);

                        $("<img>").attr({src: "img/rating-star.png", alt: "Stella di recensione."}).appendTo(averageRatingDiv);
                        $("<span>").text(" " + tutor.recensioneMedia.toFixed(1)).appendTo(averageRatingDiv);

                        let subjectsTagsDiv = $("<div>").attr("class","subjects-tags").appendTo(cardContentDiv);
                        let subjects = tutor.settori.split("-");
                        subjects.forEach(function (subject) {
                            let subjectDiv = $("<div>").attr("class","subject").appendTo(subjectsTagsDiv);
                            subjectDiv.append(subject.substring(0,1).toUpperCase() + subject.substring(1));
                        })

                        let cardDescriptionDiv = $("<div>").attr("class","card-description").appendTo(cardContentDiv);
                        $("<p>").text(tutor.descrizioneCard).appendTo(cardDescriptionDiv);

                        let hourlyRateDiv = $("<div>").attr("class","hourly-rate").appendTo(cardDiv);
                        hourlyRateDiv.text(tutor.tariffaOraria.toFixed(1) + " €");
                        $("<span>").text("/h").appendTo(hourlyRateDiv);
                    })
                }

                if (tutors.length < 15)
                    $("#show-more").css("display","none");
                showMore = 0;
            }
        })
    }


    $(".giorno-checkbox").on("click", function () {
        let daysArr = $(".giorno-checkbox:checked").map(function () {
            return this.value;
        }).get();
        filters.days = daysArr.join("|");
        filters.offset = 0;
        $("#show-more").css("display","inline-block");
        applyFilters();
    })

    $("#range-prezzi").on("change", function () {
        filters.maxprice = this.value;
        filters.offset = 0;
        $("#show-more").css("display","inline-block");
        applyFilters();
    })

    $("#sort").on("change", function () {
        filters.sort = this.value;
        filters.offset = 0;
        $("#show-more").css("display","inline-block");
        applyFilters();
    })

    $(".rating-button").on("click", function () {
        $(".rating-button").css("border","none");
        $(this).css("border","1px solid #9457ff");

        filters.rating = this.value;
        filters.offset = 0;
        $("#show-more").css("display","inline-block");
        applyFilters();
    })

    $("#search-button-grid").on("click", function () {
        filters.input = $("#search-bar-grid").val();
        filters.offset = 0;
        $("#show-more").css("display","inline-block");
        applyFilters();
    })

    $("#show-more").on("click", function () {
        filters.offset += 15;
        showMore = 1;
        applyFilters();
    })
    /*--------*/

    /* Abilitazione del bottone "Modifica Avatar" una volta caricata l'immagine profilo */
    const file = $("#image-file");

    const submitButton = $("#confirm-image");

    file.on("change", function() {
        if (file.val())
            submitButton.prop("disabled", false);
        else
            submitButton.prop("disabled", true);
    });
    /*----------------------------------------------------------------------------------*/

    /* Popup di Login e Registrazione nella pagina contact-us.jsp */
    $("#login-button-contact").on("click", function (){
        closeLoginAndRegistrationPopup('registration-popup');
        openLoginAndRegistrationPopup('login-popup');
        $("#login-popup").css({'box-shadow': '0 0 15px 10px #DB97FC', 'transition' : '600ms'});

        setTimeout(function() {
            $("#login-popup").css({'box-shadow': 'none', 'transition' : '600ms'});
        }, 3000);
    })

    $("#registration-button-contact").on("click", function (){
        closeLoginAndRegistrationPopup('login-popup');
        openLoginAndRegistrationPopup('registration-popup');
        $("#registration-popup").css({'box-shadow': '0 0 15px 10px #DB97FC', 'transition' : '600ms'});

        setTimeout(function() {
            $("#registration-popup").css({'box-shadow': 'none', 'transition' : '600ms'});
        }, 3000);
    })

    /*------------------------------------------------------------*/
});



function checkForImage(imagePath, callback) {
    $.ajax( {
        type: "GET",
        url: "check-image",
        data: { imagePath : imagePath },
        success: function (exists) {
            callback(exists);
        }
    })
}

function removeLesson(id, prezzoLezione) {
    $.ajax({
        type: 'GET', //tipo del metodo da usare nella servlet
        url: 'remove-lesson', //url della servlet a cui mandare la request
        data: {id: id}, //dati passati alla request
        success: function (response) {
            let cartContainerDiv = $("#cart-container");

            //rimuovo l'elemento della lista del carrello
            $("#lesson" + id + "-separator").remove();
            $("#lesson" + id).remove();

            //cambio il numero di lezioni
            let carico = parseInt($("#quantity").text());
            let numLezNuovo = carico - 1;

            let totale  = parseFloat($("#total").text());

            if (numLezNuovo === 1) //se rimane una sola lezione nel carrello
                $("#cart-quantity").html("<span id='quantity'>" + numLezNuovo + "</span> lezione nel carrello");
            else if (numLezNuovo > 1) //se rimangono più lezioni nel carrello
                $("#cart-quantity").html("<span id='quantity'>" + numLezNuovo + "</span> lezioni nel carrello");
            else if (numLezNuovo === 0) { //se non rimangono lezioni nel carrello
                $("#cart-quantity").html("<span id='quantity'>" + numLezNuovo + "</span> lezioni nel carrello");

                cartContainerDiv.append("<hr>");
                let paragrafo = $("<p>").attr("id","empty-cart").text("Il tuo carrello è vuoto. Conosci altri tutor e prenota una lezione che ti interessa!"); //aggiungo il paragrafo se il carrello è vuoto
                cartContainerDiv.append(paragrafo);

                //aggiungo il bottone per visualizzare altri tutor
                const form = $("<form>").attr({action: "show-tutors", method: "get"});
                const bottone = $("<input>").attr({type: "submit",id: "show-tutors-cart", class: "standard-button", value: "Visualizza i Tutor"});
                form.append(bottone);
                cartContainerDiv.append(form);

                //disabilito la sezione per il pagamento
                $("#payment-container").css({"opacity": "40%", "pointer-events": "none"});
            }

            //aggiorno il totale del carrello
            let totaleAggiornato = (totale - prezzoLezione).toFixed(1);
            $("#cart-total").html("Totale:<br><span id='total'>" + totaleAggiornato + " €</span>");
        }
    });
}