/*
In questo file sono contenute tutte le operazioni in Javascript che permettono di mostrare
un determinato popup, finestra, ecc. al verificarsi di un particolare evento.
*/

function openLoginAndRegistrationPopup(target) {
    const popup = document.getElementById(target);
    popup.style.transition = "left 500ms";
    let left = 76.5;
    popup.style.left = left + "%";
}

function closeLoginAndRegistrationPopup(target) {
    const popup = document.getElementById(target);
    popup.style.transition = "left 500ms";
    let left = 110;
    popup.style.left = left + "%";
}

let timeout;
function displayPersonalList(display) {
    const personalList = document.getElementById("personal-list");
    if(display === "show") {
        personalList.style.display = "block";
        clearTimeout(timeout);
    }
    else if(display === "hide")
        timeout = setTimeout(function() {
            personalList.style.transition = "opacity 200ms";
            personalList.style.opacity = "0";
            setTimeout(function() {
                personalList.style.display = "none";
                personalList.style.opacity = "100%";
            }, 200);
        }, 500);
    else if (display === "stop")
        clearTimeout(timeout);
}

function updateLabel(input) {
    $("#prezzo").text(input + "€");
}

function changeName(){
    document.getElementById("change-name-form").style.display = "block";
    document.getElementById("change-name-button").style.display = "none";
}
function changeDays(){
    document.getElementById("line").style.height = "205%";
    document.getElementById("change-days-form").style.display = "block";
    document.getElementById("giorni-separator").style.display = "block";
    document.getElementById("change-days-button").style.display = "none";
}
function changeHourlyRate(){
    document.getElementById("change-hrate-form").style.display = "block";
    document.getElementById("change-hrate-button").style.display = "none";
}
function changeCardDescr(){
    document.getElementById("change-card-descr-form").style.display = "block";
    document.getElementById("change-card-descr-button").style.display = "none";
}
function changeComplDescr(){
    document.getElementById("change-compl-descr-form").style.display = "block";
    document.getElementById("change-compl-descr-button").style.display = "none";
}

function booking(){
    let bookLessonContainer = document.getElementById("book-lesson-container");
    let bookLessonButton = document.getElementById("book-lesson-button");

    bookLessonContainer.style.transition = "height 500ms";
    bookLessonButton.style.transition = "opacity 300ms";
    bookLessonContainer.style.height = "550px";
    bookLessonButton.style.opacity = "0";
}

function cancelBooking() {
    let bookLessonContainer = document.getElementById("book-lesson-container");
    let bookLessonButton = document.getElementById("book-lesson-button");

    bookLessonContainer.style.transition = "height 500ms";
    bookLessonButton.style.transition = "opacity 300ms";
    bookLessonContainer.style.height = "0";
    bookLessonButton.style.opacity = "1";
}

function addRating(){
    document.getElementById("add-rating-form").style.display = "block";
    document.getElementById("add-rating-button").style.display = "none";
}

$(document).ready(function (){
    let cartQuantity = parseInt($("#quantity").text());

    if (cartQuantity === 0) {
        $("#payment-container").css({"opacity": "30%", "pointer-events": "none"});
    }
});

$(document).ready(function (){
    setInterval(displayNextImage, 5000);
});
function sleep(ms){
    return new Promise (
        resolve => setTimeout(resolve, ms)
    );
}

let counter = 1;
async function displayNextImage(){
    let opacity = 1;
    while(opacity>0){
        opacity-=0.1;
        $("#index-bg").css("opacity",opacity);
        await sleep(40);
    }
    counter = (counter % 3) + 1;
    $("#index-bg").css("background-image", "radial-gradient(circle, rgba(0, 0, 0, 0.2), rgb(0, 0, 0)), url(img/foto" + counter + ".jpg)");
    while(opacity<1){
        opacity+=0.1;
        $("#index-bg").css("opacity",opacity);
        await sleep(40);
    }
}