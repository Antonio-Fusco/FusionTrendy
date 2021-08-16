// ====================== RENDERING ===============================================
function renderProducts(categoryId) {
  let complete_path = "/item-types";
  if (categoryId != undefined) {
    complete_path += `/category/${categoryId}`;
  }
  $(".grid").html("");
  $.get(complete_path, function (res) {
    for (let i = 0; i < res.length; i++) {
      $(`
          <div class="product" product-id="${res[i].id}">
            <a href="product-id="${res[i].id}">
              <img src="${res[i].images[0].filepath}" alt=""
            /></a>
            <div class="product-text">
              <p>${res[i].name}</p>
              <span class="material-icons-round edit" product-id="${res[i].id}"> edit </span>
              <span class="material-icons-round delete" product-id="${res[i].id}"> delete </span>
              <input type="checkbox" class="select" value="${res[i].id}"/>
            </div>
          </div>
      `)
        .appendTo(".grid")
        .hide()
        .fadeIn(300 * i);
    }
  });
}

function renderCategories() {
  $(".category-column").empty();
  $(".category-column").append(
    `<button id="all-categories">Tutte le categorie</button>`
  );
  $.get("/categories", function (res) {
    for (let i = 0; i < res.length; i++) {
      $(`
      <button product-id="${res[i].id}" class="category-button">${cleanString(
        res[i].name
      )}</button>
      `)
        .appendTo(".category-column")
        .hide()
        .fadeIn(300 * i);
    }
  });
}
//====================================================================
function catalogueControls() {
  $(".controls").empty();
  $(".controls").html(
    `<button class="nuovo-prodotto"> Nuovo Prodotto</button>
      <button class="selezione-prodotti"> Selezione Elementi </button>`
  );
  $(".grid").css("display", "grid");
  $(".offer-column").css("display", "none");
}

function productTypeForm() {
  let form = `
    <div class="input-column">
            <input type="hidden" id="id-tipo" value=""/>
            <label for="nome-tipo">Nome prodotto</label>
            <input
              type="text"
              id="nome-tipo"
              placeholder="Inserire nome prodotto..."
            />
            <label for="descrizione-tipo">Descrizione</label>
            <textarea
              name=""
              id="descrizione-tipo"
              cols="50"
              rows="8"
              placeholder="Inserire una descrizione..."
            ></textarea>
            <label for="categoria">Categoria</label>
            <select name="" id="categoria">
                <option value="">Categoria...</option>
                <option value="" id="new-category">Nuova categoria</option>
              </select>
            <label for="prezzo">Prezzo</label>
            <input type="number" min=0 step=0.01 id="prezzo" placeholder="Inserire prezzo...">
            <label for="sconto">Sconto</label>
            <input type="number" min=0 id="sconto" placeholder="Inserire sconto percentuale...">
          </div>
          <div class="button-column">
            <div class="tooltip">
              <span class="tooltiptext">
                Carica le foto del prodotto, le prime due appariranno
                nell'anteprima
              </span>
              <button>Carica foto</button>
            </div>
            <div class="tooltip">
              <span class="tooltiptext" style="top: -80%">
                Da qui è possibile settare i colori, le taglie, e le quantità per ciascuna loro combinazione
              </span>
              <button id="item-detail">Dettaglio</button>
            </div>
            <div class="tooltip">
              <span class="tooltiptext">Rimuovi tutte le foto caricate</span>
              <button>Elimina foto</button>
            </div>
            <button id="save-type">Salva</button>
          </div>
  `;

  $(".modal-content").html(form);
}

function itemForm() {
  $(".modal-content").empty();
  $(`
    <div class="detail"/>
       Dettaglio taglia e colore
       <input type="hidden" id="type-id" value=""/>
       <input type="hidden" id="item-id" value=""/>
       <label for="colore">Colore</label>
       <select name="" id="colore">
          <option value="" selected>Colore</option>
          <option value="" id="new-color">Nuovo colore...</option>
       </select>
       <label for="quantity">Quantità</label>
       <input
          type="number"
          id="quantity"
          placeholder="Inserire la quantità di prodotto..."
        />
        <label for="taglia">Taglia</label>
        <select name="" id="taglia">
          <option value="" selected>Taglia</option>
          <option value="" id="new-size">Nuova taglia...</option>
        </select>
        <button id="save-detail">Salva dettaglio</button>
    </div>
  `).appendTo(".modal-content");
}

function fillTypeForm(id) {
  $.get(`/item-types/${id}`, function (res) {
    $("#id-tipo").val(res.id);
    $("#nome-tipo").val(res.name);
    $("#descrizione-tipo").val(res.description);
    $("#prezzo").val(res.price);
    $("#sconto").val(res.discount);

    $.get("/categories", function (categories) {
      for (category of categories) {
        let option = `<option value=${category.id}>${cleanString(
          category.name
        )}</option>`;
        if (category.id == res.category.id) {
          option = `<option value=${category.id} selected>${cleanString(
            category.name
          )}</option>`;
        }
        $(option).insertBefore("#new-category");
      }
    });
  });
}

function fillItemForm(typeId) {
  let colors = [];
  let sizes = [];
  $.get(`/items/type/${typeId}`, function (items) {
    for (item of items) {
      if (!colors.includes(item.color)) {
        colors.push(item.color);
        $(`
      <option value=${item.color}>${cleanString(item.color)}</option>
      `).insertBefore("#new-color");
      }
      if (!sizes.includes(item.size)) {
        sizes.push(item.size);
        $(`
      <option value=${item.size}>${item.size.toUpperCase()}</option>
      `).insertBefore("#new-size");
      }
    }
  });
}

function matchSizeColor(typeId) {
  let color = $("#colore").val();
  let size = $("#taglia").val();
  if (color != "" || size != "") {
    $.get(`/items/type/${typeId}/size/${size}/color/${color}`, function (res) {
      console.log(res.quantity);
      let quantity = 0;
      emptyFlag = true;
      if (res.quantity != undefined) {
        quantity = res.quantity;
        emptyFlag = false;
        $("#item-id").val(res.id);
      }
      $("#quantity").val(quantity);
    });
  }
}

function saveType() {
  const name = $("#nome-tipo").val();
  const description = $("#descrizione-tipo").val();
  const price = $("#prezzo").val();
  const categoryId = $("#categoria").val();
  $.get(`/categories/${categoryId}`, function (category) {
    const type = { id: typeId, name, description, price, category };
    $.ajax({
      type: "POST",
      url: `/item-types/`,
      data: JSON.stringify(type),
      contentType: "application/json",
      success: function () {
        console.log("success");
        successModal();
      },
    });
  });
}

function editType() {
  const id = $("#id-tipo").val();
  const name = $("#nome-tipo").val();
  const description = $("#descrizione-tipo").val();
  const price = $("#prezzo").val();
  const categoryId = $("#categoria").val();
  $.get(`/categories/${categoryId}`, function (category) {
    const type = { id: typeId, name, description, price, category };
    $.ajax({
      type: "PUT",
      url: `/item-types/${id}`,
      data: JSON.stringify(type),
      contentType: "application/json",
      success: function () {
        console.log("success");
        successModal();
      },
    });
  });
}

function saveNewItem() {
  const color = $("#colore").val();
  const size = $("#taglia").val();
  const quantity = $("#quantity").val();
  let item = { color, size, quantity, typeId };
  $.ajax({
    type: "POST",
    url: `/items/type/${typeId}`,
    data: JSON.stringify(item),
    contentType: "application/json",
    success: function () {
      console.log("success");
      successModal();
    },
  });
}

function saveItemEdit() {
  const id = $("#item-id").val();
  const color = $("#colore").val();
  const size = $("#taglia").val();
  const quantity = $("#quantity").val();
  let item = { id, color, size, quantity, typeId };
  $.ajax({
    type: "PUT",
    url: `/items/${id}/${typeId}`,
    data: JSON.stringify(item),
    contentType: "application/json",
    success: function () {
      console.log("success");
      successModal();
    },
  });
}
// ============================= DELETE ===============================
function deleteType(id) {
  $.ajax({
    type: "DELETE",
    url: `item-types/${id}`,
    success: function () {
      console.log("success");
      successModal();
    },
  });
}
// ============================= OFFERTE ==============================
function applyDiscount() {
  let discount = $("#discount").val();
  for (sel of selections) {
    $.ajax({
      type: "PUT",
      url: `/item-types/${sel}/discount/${discount}`,
      data: JSON.stringify(),
      contentType: "application/json",
      success: function () {
        console.log("success");
        successModal();
      },
    });
  }
}

// ========================== ACCESSORI ===========================
function cleanString(str) {
  str = str.toLowerCase();
  temp = str.substring(0, 1).toUpperCase();
  return temp + str.substring(1);
}

function successModal() {
  $(".modal-content").empty();
  $(`
        <div class="input-column">
        <p> Operazione Riuscita !</p>
        <button class="back"> OK </button>
        </div>
        `).appendTo(".modal-content");
  $(".modal").css("display", "block");
}
