// =========================================================
// 1. VARIABLES GLOBALS I ELEMENTS DE LA PÀGINA
// =========================================================
let figures = initialFigures || [];
let selectedFigure = null;
let currentType = "CIRCLE";
let isDragging = false;
let isDrawing = false;
let currentFreehandFigure = null;
let currentPointsFigure = null;
let estatAbansEditar = null;
let dragStartState = null;
let dragStartX = null;
let dragStartY = null;
let isSaving = false;
let saveAgain = false;

// Piles per a Desfer (Undo) i Refer (Redo)
const undoStack = [];
const redoStack = [];

const canvas = document.getElementById("canvas");
const ctx = canvas.getContext("2d");
const form = document.getElementById("drawingForm");
const inputColor = document.getElementById("inputColor");
const inputSize = document.getElementById("inputSize");
const inputCanvasWidth = document.getElementById("inputCanvasWidth");
const inputCanvasHeight = document.getElementById("inputCanvasHeight");
const figureList = document.getElementById("figureList");

// =========================================================
// 2. DESFER I REFER (UNDO / REDO)
// =========================================================
function obtenirEstatActual() {
  return JSON.stringify({
    figures: figures,
    width: canvas.width,
    height: canvas.height,
    name: form ? form.inputName.value : "",
  });
}

// Cridem aquesta funció JUST ABANS de modificar el dibuix
function guardarPasUndo() {
  estatAbansEditar = null;
  undoStack.push(obtenirEstatActual());
  redoStack.length = 0; // En fer un canvi nou, es buida el "Refer"
}

function iniciarEdicio() {
  estatAbansEditar = obtenirEstatActual();
}

function acabarEdicio() {
  if (estatAbansEditar === null) return;

  const estatAnterior = estatAbansEditar;
  const estatActual = obtenirEstatActual();
  estatAbansEditar = estatActual;
  if (estatAnterior !== estatActual) {
    undoStack.push(estatAnterior);
    redoStack.length = 0;
    saveDrawing();
  }
}

function aplicarEstat(estatJson) {
  const estat = JSON.parse(estatJson);
  figures = estat.figures;
  canvas.width = estat.width;
  canvas.height = estat.height;

  if (form) form.inputName.value = estat.name;
  if (inputCanvasWidth) inputCanvasWidth.value = estat.width;
  if (inputCanvasHeight) inputCanvasHeight.value = estat.height;

  selectedFigure = null;
  currentPointsFigure = null;
  isDragging = false;
  isDrawing = false;
  currentFreehandFigure = null;
  estatAbansEditar = null;
  dragStartState = null;
  dragStartX = null;
  dragStartY = null;
  redraw();
  renderFigureList();
  saveDrawing();
}

function undoDrawing() {
  if (undoStack.length === 0) return;
  redoStack.push(obtenirEstatActual());
  const anterior = undoStack.pop();
  aplicarEstat(anterior);
}

function redoDrawing() {
  if (redoStack.length === 0) return;
  undoStack.push(obtenirEstatActual());
  const seguent = redoStack.pop();
  aplicarEstat(seguent);
}

// =========================================================
// 3. DIBUIXAR AL CANVAS I LLISTA LATERAL
// =========================================================
function getMousePos(event) {
  const rect = canvas.getBoundingClientRect();
  return {
    x: Math.round((event.clientX - rect.left) * (canvas.width / rect.width)),
    y: Math.round((event.clientY - rect.top) * (canvas.height / rect.height)),
  };
}

function redraw() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  for (let i = 0; i < figures.length; i++) {
    const fig = figures[i];
    const half = (fig.size || 50) / 2;
    ctx.fillStyle = fig.color;
    ctx.strokeStyle = fig.color;
    ctx.lineWidth = 3;

    if (fig.type === "CIRCLE") {
      ctx.beginPath();
      ctx.arc(fig.x, fig.y, half, 0, Math.PI * 2);
      ctx.fill();
    } else if (fig.type === "SQUARE") {
      ctx.fillRect(fig.x - half, fig.y - half, half * 2, half * 2);
    } else if (fig.type === "TRIANGLE") {
      ctx.beginPath();
      ctx.moveTo(fig.x, fig.y - half);
      ctx.lineTo(fig.x - half, fig.y + half);
      ctx.lineTo(fig.x + half, fig.y + half);
      ctx.closePath();
      ctx.fill();
    } else if (fig.type === "STAR") {
      ctx.beginPath();
      for (let j = 0; j < 14; j++) {
        const radius = j % 2 === 0 ? half : half * 0.4;
        const angle = (j * Math.PI) / 7 - Math.PI / 2;
        const x = fig.x + Math.cos(angle) * radius;
        const y = fig.y + Math.sin(angle) * radius;
        if (j === 0) ctx.moveTo(x, y);
        else ctx.lineTo(x, y);
      }
      ctx.closePath();
      ctx.fill();
    } else if (fig.type === "FREEHAND" || fig.type === "POINTS") {
      if (!fig.points || fig.points.length === 0) continue;

      if (fig.type === "POINTS") {
        for (let j = 0; j < fig.points.length; j++) {
          ctx.beginPath();
          ctx.arc(fig.points[j].x, fig.points[j].y, 4, 0, Math.PI * 2);
          ctx.fill();
        }
      }

      ctx.beginPath();
      ctx.moveTo(fig.points[0].x, fig.points[0].y);
      for (let j = 1; j < fig.points.length; j++) {
        ctx.lineTo(fig.points[j].x, fig.points[j].y);
      }
      ctx.stroke();
    }

    // Marca la figura seleccionada amb un requadre discontinu
    if (fig === selectedFigure && fig.x !== undefined) {
      ctx.strokeStyle = "#000000";
      ctx.lineWidth = 1;
      ctx.setLineDash([4, 4]);
      ctx.strokeRect(
        fig.x - half - 4,
        fig.y - half - 4,
        half * 2 + 8,
        half * 2 + 8,
      );
      ctx.setLineDash([]);
    }
  }
}

function renderFigureList() {
  if (!figureList) return;
  figureList.innerHTML = "";

  for (let i = 0; i < figures.length; i++) {
    const fig = figures[i];
    if (!fig.name) fig.name = "Figura " + (i + 1);

    const li = document.createElement("li");
    const input = document.createElement("input");
    input.type = "text";
    input.value = fig.name;
    input.disabled = !form;

    if (fig === selectedFigure) {
      input.style.fontWeight = "bold";
      input.style.border = "2px solid #007bff";
    }

    // En fer clic a l'input de la llista, seleccionem la figura
    input.addEventListener("focus", function () {
      iniciarEdicio();
      selectedFigure = fig;
      if (inputColor) inputColor.value = fig.color;
      if (inputSize && fig.size) inputSize.value = fig.size;
      redraw();
    });

    input.addEventListener("input", function () {
      fig.name = input.value;
    });
    input.addEventListener("change", acabarEdicio);

    li.appendChild(input);
    figureList.appendChild(li);
  }
}

// =========================================================
// 4. DESAR AL SERVIDOR (FETCH)
// =========================================================
async function saveDrawing() {
  if (!form) return;
  if (isSaving) {
    saveAgain = true;
    return;
  }

  isSaving = true;
  const saveStatus = document.getElementById("saveStatus");

  try {
    do {
      saveAgain = false;
      const id = form.inputDrawingId.value;
      const dto = {
        id: id !== "" ? id : null,
        name: form.inputName.value,
        figures: figures,
        width: canvas.width,
        height: canvas.height,
      };

      saveStatus.textContent = "Desant...";
      try {
        const response = await fetch("/drawings/save", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(dto),
        });

        if (!response.ok) {
          saveStatus.textContent = "Error al desar (" + response.status + ").";
        } else {
          const data = await response.json();
          form.inputDrawingId.value = data.id;
          if (form.inputName.value === dto.name)
            form.inputName.value = data.name;
          saveStatus.textContent = "Desat!";

          // Si era un dibuix nou, actualitzem l'URL sense recarregar
          if (id === "") {
            window.history.pushState({}, "", "/drawings/editor?id=" + data.id);
          }
        }
      } catch (error) {
        saveStatus.textContent = "Error de connexió al desar.";
        console.error("No s'ha pogut desar el dibuix:", error);
      }
    } while (saveAgain);
  } finally {
    isSaving = false;
  }
}

// =========================================================
// 5. ESDEVENIMENTS DELS CONTROLS I DEL CANVAS
// =========================================================
function seleccionarEina(tipus, idBoto) {
  currentType = tipus;
  currentPointsFigure = null;
  selectedFigure = null;
  redraw();
  renderFigureList();

  // Marca visualment el botó actiu amb la classe CSS "active"
  const botons = document.querySelectorAll(".sidebar button");
  for (let i = 0; i < botons.length; i++) {
    botons[i].classList.remove("active");
  }
  const botoActiu = document.getElementById(idBoto);
  if (botoActiu) botoActiu.classList.add("active");
}

if (form) {
  form.addEventListener("submit", function (event) {
    event.preventDefault();
    saveDrawing();
  });

  form.inputName.addEventListener("focus", iniciarEdicio);
  form.inputName.addEventListener("change", acabarEdicio);

  document.getElementById("btnUndo").addEventListener("click", undoDrawing);
  document.getElementById("btnRedo").addEventListener("click", redoDrawing);

  document
    .getElementById("btnAddCircle")
    .addEventListener("click", function () {
      seleccionarEina("CIRCLE", "btnAddCircle");
    });
  document
    .getElementById("btnAddSquare")
    .addEventListener("click", function () {
      seleccionarEina("SQUARE", "btnAddSquare");
    });
  document
    .getElementById("btnAddTriangle")
    .addEventListener("click", function () {
      seleccionarEina("TRIANGLE", "btnAddTriangle");
    });
  document.getElementById("btnAddStar").addEventListener("click", function () {
    seleccionarEina("STAR", "btnAddStar");
  });
  document
    .getElementById("btnAddFreehand")
    .addEventListener("click", function () {
      seleccionarEina("FREEHAND", "btnAddFreehand");
    });
  document
    .getElementById("btnAddPoints")
    .addEventListener("click", function () {
      seleccionarEina("POINTS", "btnAddPoints");
    });

  // Dimensions del canvas
  inputCanvasWidth.addEventListener("focus", iniciarEdicio);
  inputCanvasWidth.addEventListener("input", function () {
    const width = Number(inputCanvasWidth.value);
    if (Number.isInteger(width) && width > 0) {
      canvas.width = width;
      redraw();
    }
  });

  inputCanvasWidth.addEventListener("change", acabarEdicio);

  inputCanvasHeight.addEventListener("focus", iniciarEdicio);
  inputCanvasHeight.addEventListener("input", function () {
    const height = Number(inputCanvasHeight.value);
    if (Number.isInteger(height) && height > 0) {
      canvas.height = height;
      redraw();
    }
  });
  inputCanvasHeight.addEventListener("change", acabarEdicio);

  // Canvi de color i mida de la figura seleccionada
  inputColor.addEventListener("focus", iniciarEdicio);
  inputColor.addEventListener("input", function () {
    if (selectedFigure) {
      selectedFigure.color = inputColor.value;
      redraw();
    }
  });
  inputColor.addEventListener("change", acabarEdicio);

  inputSize.addEventListener("focus", iniciarEdicio);
  inputSize.addEventListener("input", function () {
    if (selectedFigure && selectedFigure.x !== undefined) {
      selectedFigure.size = Number(inputSize.value) || 50;
      redraw();
    }
  });
  inputSize.addEventListener("change", acabarEdicio);

  // Eliminar seleccionada
  document
    .getElementById("btnDeleteSelected")
    .addEventListener("click", function () {
      if (!selectedFigure) return;
      guardarPasUndo();

      for (let i = 0; i < figures.length; i++) {
        if (figures[i] === selectedFigure) {
          figures.splice(i, 1);
          break;
        }
      }

      selectedFigure = null;
      redraw();
      renderFigureList();
      saveDrawing();
    });

  // Esborrar-ho tot
  document
    .getElementById("btnDeleteAll")
    .addEventListener("click", function () {
      if (figures.length === 0) return;
      guardarPasUndo();

      figures = [];
      selectedFigure = null;
      currentPointsFigure = null;
      redraw();
      renderFigureList();
      saveDrawing();
    });

  // Clic al canvas: seleccionar o crear figura
  canvas.addEventListener("mousedown", function (event) {
    const pos = getMousePos(event);
    let clicked = null;

    for (let i = figures.length - 1; i >= 0; i--) {
      const fig = figures[i];
      if (
        fig.x !== undefined &&
        Math.hypot(pos.x - fig.x, pos.y - fig.y) <= (fig.size || 50) / 2
      ) {
        clicked = fig;
        break;
      }
    }

    if (clicked) {
      selectedFigure = clicked;
      isDragging = true;
      dragStartState = obtenirEstatActual();
      dragStartX = clicked.x;
      dragStartY = clicked.y;
      inputColor.value = clicked.color;
      inputSize.value = clicked.size || 50;
      redraw();
      renderFigureList();
      return;
    }

    selectedFigure = null;
    guardarPasUndo();

    if (currentType === "FREEHAND") {
      isDrawing = true;
      currentFreehandFigure = {
        name: "Traç " + (figures.length + 1),
        type: "FREEHAND",
        color: inputColor.value,
        points: [pos],
      };
      figures.push(currentFreehandFigure);
      renderFigureList();
    } else if (currentType === "POINTS") {
      if (!currentPointsFigure) {
        currentPointsFigure = {
          name: "Punts " + (figures.length + 1),
          type: "POINTS",
          color: inputColor.value,
          points: [],
        };
        figures.push(currentPointsFigure);
      }
      currentPointsFigure.points.push(pos);
      redraw();
      renderFigureList();
      saveDrawing();
    } else {
      figures.push({
        name: currentType + " " + (figures.length + 1),
        type: currentType,
        color: inputColor.value,
        x: pos.x,
        y: pos.y,
        size: Number(inputSize.value) || 50,
      });
      redraw();
      renderFigureList();
      saveDrawing();
    }
  });

  // Moure el ratolí pel canvas
  canvas.addEventListener("mousemove", function (event) {
    const pos = getMousePos(event);

    if (isDragging && selectedFigure) {
      selectedFigure.x = pos.x;
      selectedFigure.y = pos.y;
      redraw();
    } else if (isDrawing && currentFreehandFigure) {
      currentFreehandFigure.points.push(pos);
      redraw();
    }
  });

  // Deixar anar el ratolí: acaba d'arrossegar o dibuixar traç i desa
  window.addEventListener("mouseup", function () {
    if (isDragging && selectedFigure) {
      const moved =
        selectedFigure.x !== dragStartX || selectedFigure.y !== dragStartY;
      if (moved) {
        undoStack.push(dragStartState);
        redoStack.length = 0;
        saveDrawing();
      }
    } else if (isDrawing) {
      saveDrawing();
    }
    isDragging = false;
    isDrawing = false;
    currentFreehandFigure = null;
    dragStartState = null;
    dragStartX = null;
    dragStartY = null;
  });

  // Marca el cercle com a eina inicial
  seleccionarEina("CIRCLE", "btnAddCircle");
}

// Si l'usuari prem Enrere o Endavant al navegador, recarreguem l'estat de l'URL
window.addEventListener("popstate", function () {
  window.location.reload();
});

// Dibuix inicial en carregar la pàgina
redraw();
renderFigureList();
