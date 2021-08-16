$(document).ready(function () {
  // Caricamento prima slide della presentazione
  showDivs(slideIndex);
  responsivePadding();
  // Setto l'altezza del blocco dei comandi pari a quella dell'immagine dello slidet
  $(window).on("resize", function () {
    responsivePadding();
  });

  // Callback pulsante indietro
  $(".left").click(function () {
    plusDivs(-1);
  });

  // Callback pulsante avanti
  $(".right").click(function () {
    plusDivs(1);
  });

  // Callback dots
  for (let i = 1; i <= $(".dots").length; i++) {
    $(`.dots:nth-child(${i})`).click(function () {
      $(`.dots`).css("background-color", "transparent");
      $(this).css("background-color", "white");
      slideIndex = i - 1;
      showDivs(slideIndex);
    });
  }

  $(".dots").mouseover(function () {
    $(`.dots`).css("background-color", "transparent");
    $(this).css("background-color", "white");
  });

  $(".dots").mouseleave(function () {
    $(`.dots`).css("background-color", "transparent");
    $(`.dots`).eq(slideIndex).css("background-color", "white");
  });
});

var slideIndex = 0;
function showDivs(index) {
  $(".mySlides").each(function (i) {
    if (i == index) {
      $(this).css("display", "block");
    } else {
      $(this).css("display", "none");
    }
  });
}

function plusDivs(increment) {
  let n = $(".mySlides").length;
  if (slideIndex + increment >= n) {
    slideIndex = 0;
  } else if (slideIndex + increment < 0) {
    slideIndex = n - 1;
  } else {
    slideIndex += increment;
  }
  showDivs(slideIndex);
  $(`.dots`).css("background-color", "transparent");
  $(`.dots:nth-child(${slideIndex + 1})`).css("background-color", "white");
}

function responsivePadding() {
  let innerH = $(".left").height() / 2 + 1.5;
  let innerW = $(".left").width();

  $(".left")
    .css("padding-top", $(".slider").outerHeight() / 2.0 - innerH)
    .css(
      "padding-left",
      (($(window).width() - $(".mySlides").outerWidth()) / 2 - innerW) / 2
    );
  $(".left")
    .css("padding-bottom", $(".slider").outerHeight() / 2.0 - innerH)
    .css(
      "padding-right",
      (($(window).width() - $(".mySlides").outerWidth()) / 2 - innerW) / 2
    );
  $(".right")
    .css("padding-top", $(".slider").outerHeight() / 2.0 - innerH)
    .css(
      "padding-left",
      (($(window).width() - $(".mySlides").outerWidth()) / 2 - innerW) / 2 - 1.5
    );
  $(".right")
    .css("padding-bottom", $(".slider").outerHeight() / 2 - innerH)
    .css(
      "padding-right",
      (($(window).width() - $(".mySlides").outerWidth()) / 2 - innerW) / 2
    );
}
