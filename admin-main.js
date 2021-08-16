// ================== Variabili globali ===============
var editFlag = false;
var typeId = -1;
var emptyFlag = false;
var deleteFlag = false;
var selections = [];
var mode = "product";
// ===================================================
$(document).ready(function () {
  renderProducts();
  // Controlli ai pulsanti del menu dropdown
  $("#catalogo").click(function () {
    renderProducts();
    catalogueControls();
    mode = "catalogue";
    $(".grid").css("display", "grid");
    $(".grid").css("width", "90vw");
    $(".grid").css("position", "");
    $(".offer-column").css("display", "none");
    $(".category-column").css("display", "none");
  });

  $("#categorie").click(function () {
    $(".controls").empty();
    $(".controls").html(
      `<button class="new-category"> Nuova Categoria</button>
        <button> Modifica Categoria Selezionata </button>
        <button> Elimina Categoria Selezionata </button>
        `
    );
    $(".grid").css("display", "inline-grid");
    $(".grid").css("width", "70vw");
    // $(".grid").css("position", "absolute");
    // $(".grid").css("left", "25vw");
    $(".category-column").css("display", "inline-flex");
    $(".category-column").css("height", $(".grid").css("height"));
    $(".offer-column").css("display", "none");
    renderCategories();
    renderProducts();
    mode = "category";
  });

  $("#offerte").click(function () {
    mode = "offer";
    $(".controls").empty();
    $(".controls").html(
      `<button class="select-all">Seleziona tutti</button>
       <button class="deselect-all">Deseleziona tutto</button>
      `
    );
    $(".grid").css("display", "inline-grid");
    $(".grid").css("width", "70vw");
    $(".grid").css("left", "40%");
    $(".offer-column").css("display", "inline-flex");
    $(".offer-column").css("height", $(".grid").css("height"));
    $(".category-column").css("display", "none");
    $(".product-text .edit").css("display", "none");
    $(".product-text .delete").css("display", "none");
    $(".product-text .select").css("display", "inline");
  });

  $("#promozioni").click(function () {
    mode = "promotion";
    $(".controls").empty();
    $(".grid").css("display", "grid");
    $(".offer-column").css("display", "none");
  });

  // Controllo pulsante nuovo prodotto
  $(".controls").on("click", ".nuovo-prodotto", function () {
    productTypeForm();
    $(".modal").css("display", "block");
  });

  // Controllo pulsante per la selezione in bullk
  $(".controls").on("click", ".selezione-prodotti", function () {
    console.log("selezione");
    deleteFlag = true;
    $(".controls").empty();
    $(".controls").html(
      `
      <button id="bulk-delete">Elimina Selezionati</button>
      <button id="undo-products">Annulla</button>
      `
    );

    $(".product-text .edit").css("display", "none");
    $(".product-text .delete").css("display", "none");
    $(".product-text .select").css("display", "inline");
  });

  // Controllo eliminazione in bulk
  $(".controls").on("click", "#bulk-delete", function () {
    for (sel of selections) {
      deleteType(sel);
    }
    $(".select").each(function () {
      $(this).prop("checked", false);
    });
    selections.splice(0);
  });

  // Annullamento eleminazione in bulk
  $(".controls").on("click", "#undo-products", function () {
    catalogueControls();
    renderProducts();
  });
  // Controllo selezione checkbox
  $(".grid").on("change", ".select", function () {
    if ($(this).is(":checked")) {
      selections.push(+$(this).val());
      $(this).parent().parent().css("background-color", "#fdd3c7");
    } else {
      $(this).parent().parent().css("background-color", "#46d9cd");
      let idx = selections.indexOf($(this).val());
      selections.splice(idx, 1);
    }
    if (mode == "offer") {
      $(".offer-column .selection-counter")
        .html("")
        .html(`Sono stati selezionati ${selections.length} elementi`);
    }
  });

  // Controllo apertura modale edit
  $(".grid").on("click", ".edit", function () {
    editFlag = true;
    typeId = +$(this).attr("product-id");
    $(".modal").css("display", "block");
    productTypeForm();
    fillTypeForm(+$(this).attr("product-id"));
  });

  // Controllo apertura modale delete
  $(".grid").on("click", ".delete", function () {
    deleteFlag = true;
    deleteType($(this).attr("product-id"));
  });
  // Controllo chiusura modale
  $(".close").click(function (event) {
    event.stopPropagation();
    $(".modal").css("display", "none");
  });

  // Controllo apertura dettaglio
  $(".modal").on("click", "#item-detail", function () {
    mode = "item";
    $("#type-id").val(typeId);
    if (typeId != -1) {
      itemForm();
      fillItemForm(typeId);
    } else {
      alert("Prima è necessario creare il nuovo oggetto");
    }
  });

  $(".modal").on("change", "#taglia", function () {
    matchSizeColor(typeId);
  });

  $(".modal").on("change", "#colore", function () {
    matchSizeColor(typeId);
  });

  // Controllo salvataggio item
  $(".modal").on("click", "#save-detail", function () {
    if (emptyFlag) {
      saveNewItem();
    } else {
      saveItemEdit();
    }
  });

  $(".modal").on("click", ".back", function () {
    switch (mode.toLowerCase()) {
      case "offer": {
        if (offerFlag) {
          $(".modal").css("display", "none");
          $(".grid .select").each(function () {
            $(this).prop("checked", false);
            selections = [];
          });
          $("#discount").val("");
          $(".offer-column .selection-counter")
            .html("")
            .html(`Sono stati selezionati ${selections.length} elementi`);
        }
        break;
      }

      case "product": {
        $(".modal").css("display", "none");
        renderProducts();
        deleteFlag = false;
        break;
      }

      case "item": {
        productTypeForm();
        fillTypeForm(typeId);
      }
    }
  });
  // Controllo salvataggio tipo
  $(".modal").on("click", "#save-type", function () {
    if (editFlag) {
      editType();
    } else {
      saveType();
    }
  });

  // ===================== OFFERTE ======================
  $(".offer-column").on("click", "#discount-button", function () {
    applyDiscount();
  });

  $(".controls").on("click", ".select-all", function () {
    $(".select").each(function () {
      $(this).prop("checked", true);
      $(this).parent().parent().css("background-color", "#fdd3c7");
      selections.push($(this).val());
    });
    $(".offer-column .selection-counter")
      .html("")
      .html(`Sono stati selezionati ${selections.length} elementi`);
  });

  $(".controls").on("click", ".deselect-all", function () {
    $(".select").each(function () {
      $(this).prop("checked", false);
      $(this).parent().parent().css("background-color", "#46d9cd");
    });
    selections.splice(0);
    $(".offer-column .selection-counter")
      .html("")
      .html(`Sono stati selezionati ${selections.length} elementi`);
  });

  // ======================== CATEGORIE =======================
  $(".category-column").on("click", ".category-button", function () {
    $(".grid").empty();
    renderProducts($(this).attr("product-id"));
    $(".category-column #all-categories").css("background-color", "#fdd3c7");
    $(".category-button").each(function () {
      $(this).css("background-color", "#fdd3c7");
    });
    $(this).css("background-color", "#46d9cd");
  });

  $(".category-column").on("click", "#all-categories", function () {
    $(".category-button").each(function () {
      $(this).css("background-color", "#fdd3c7");
    });
    $(this).css("background-color", "#46d9cd");
    renderProducts();
  });

  $(".controls").on("click", "#new-category", function () {
    $(".modal");
  });
});
