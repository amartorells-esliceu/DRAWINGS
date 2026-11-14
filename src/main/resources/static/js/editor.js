// Carrega les figures desades i inicialitza l'estat de l'editor.
let figures = initialFigures || [];
let selectedFigure = null;
let isDragging = false;
let currentType = "CIRCLE";
let isDrawing = false;
let currentFreehandFigure = null;
let currentPointsFigure = null;
let isSaving = false;
let saveAgain = false;
let canvasChanged = false;
let editStartState = null;
let dragStartState = null;
let dragChanged = false;
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

// Desa en una instantània les dades que formen el dibuix.
function getEditorState() {
    return JSON.stringify({
        figures: figures,
        width: canvas.width,
        height: canvas.height,
        name: form ? form.inputName.value : ""
    });
}

function rememberState() {
    editStartState = null;
    undoStack.push(getEditorState());
    redoStack.length = 0;
}

function startEdit() {
    editStartState = getEditorState();
}

function finishEdit() {
    if (editStartState === null) return;

    const previousState = editStartState;
    editStartState = null;
    if (previousState !== getEditorState()) {
        undoStack.push(previousState);
        redoStack.length = 0;
        saveDrawing();
    }
}

// Restaura una instantània i la desa al servidor.
function restoreState(snapshot) {
    const state = JSON.parse(snapshot);
    figures = state.figures;
    canvas.width = state.width;
    canvas.height = state.height;
    if (form) form.inputName.value = state.name;
    if (inputCanvasWidth) inputCanvasWidth.value = state.width;
    if (inputCanvasHeight) inputCanvasHeight.value = state.height;
    selectedFigure = null;
    currentPointsFigure = null;
    isDragging = false;
    isDrawing = false;
    currentFreehandFigure = null;
    canvasChanged = false;
    editStartState = null;
    redraw();
    renderFigureList();
    saveDrawing();
}

function undoDrawing() {
    if (undoStack.length === 0) return;
    redoStack.push(getEditorState());
    restoreState(undoStack.pop());
}

function redoDrawing() {
    if (redoStack.length === 0) return;
    undoStack.push(getEditorState());
    restoreState(redoStack.pop());
}

// Converteix la posició del ratolí a les coordenades internes del canvas.
function getMousePos(event) {
    const rect = canvas.getBoundingClientRect();
    return {
        x: Math.round((event.clientX - rect.left) * (canvas.width / rect.width)),
        y: Math.round((event.clientY - rect.top) * (canvas.height / rect.height))
    };
}

// Esborra el canvas i torna a dibuixar totes les figures.
function redraw() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    for (let i = 0; i < figures.length; i++) {
        const fig = figures[i];
        const half = (fig.size || 50) / 2;
        ctx.fillStyle = fig.color;
        ctx.strokeStyle = fig.color;
        ctx.lineWidth = 3;

        // Dibuixa les figures bàsiques a partir del centre i de la mida.
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
                const radius = (j % 2 === 0) ? half : half * 0.4;
                const angle = (j * Math.PI / 7) - (Math.PI / 2);
                const x = fig.x + Math.cos(angle) * radius;
                const y = fig.y + Math.sin(angle) * radius;

                if (j === 0) {
                    ctx.moveTo(x, y);
                } else {
                    ctx.lineTo(x, y);
                }
            }
            ctx.closePath();
            ctx.fill();
        } else if (fig.type === "FREEHAND" || fig.type === "POINTS") {
            if (!fig.points || fig.points.length === 0) continue;

            // En el mode per punts, mostra cada coordenada com un punt.
            if (fig.type === "POINTS") {
                for (let j = 0; j < fig.points.length; j++) {
                    ctx.beginPath();
                    ctx.arc(fig.points[j].x, fig.points[j].y, 4, 0, Math.PI * 2);
                    ctx.fill();
                }
            }

            // Uneix els punts per dibuixar el traç o la línia.
            ctx.beginPath();
            ctx.moveTo(fig.points[0].x, fig.points[0].y);
            for (let j = 1; j < fig.points.length; j++) {
                ctx.lineTo(fig.points[j].x, fig.points[j].y);
            }
            ctx.stroke();
        }

        // Marca les figures bàsiques seleccionades amb un rectangle discontinu.
        if (fig === selectedFigure && fig.x !== undefined) {
            ctx.strokeStyle = "#000000";
            ctx.lineWidth = 1;
            ctx.setLineDash([4, 4]);
            ctx.strokeRect(fig.x - half - 4, fig.y - half - 4, half * 2 + 8, half * 2 + 8);
            ctx.setLineDash([]);
        }
    }
}

// Crea un camp de text per a cada figura de la llista.
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

        // El focus selecciona la figura sense recrear la llista.
        input.addEventListener("focus", function() {
            startEdit();
            selectedFigure = fig;
            if (inputColor) inputColor.value = fig.color;
            if (inputSize && fig.size) inputSize.value = fig.size;
            redraw();

            for (let j = 0; j < figureList.children.length; j++) {
                const listInput = figureList.children[j].firstChild;
                listInput.style.fontWeight = listInput === input ? "bold" : "";
                listInput.style.border = listInput === input ? "2px solid #007bff" : "";
            }
        });

        // Desa el nom cada vegada que s'edita el camp.
        input.addEventListener("input", function() {
            fig.name = input.value;
        });
        input.addEventListener("change", finishEdit);

        li.appendChild(input);
        figureList.appendChild(li);
    }
}

// Mostra les figures i els seus noms en obrir l'editor.
redraw();
renderFigureList();

// Desa l'estat actual; si ja hi ha un desament en curs, desa els canvis en acabar.
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
                height: canvas.height
            };

            saveStatus.textContent = "Desant...";
            const response = await fetch("/drawings/save", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(dto)
            });

            if (!response.ok) {
                saveStatus.textContent = `Error al desar el dibuix (${response.status}).`;
                return;
            }

            const data = await response.json();
            form.inputDrawingId.value = data.id;
            if (form.inputName.value === dto.name) form.inputName.value = data.name;
            saveStatus.textContent = "Dibuix desat correctament!";

            if (id === "") {
                window.history.pushState({}, "", "/drawings/editor?id=" + data.id);
            }
        } while (saveAgain);
    } catch (error) {
        saveStatus.textContent = "Error al desar el dibuix.";
        console.error("No s'ha pogut desar el dibuix:", error);
    } finally {
        isSaving = false;
    }
}

if (form) {
    // El botó de desament manual també utilitza la funció comuna.
    form.addEventListener("submit", function(event) {
        event.preventDefault();
        saveDrawing();
    });

    // Desa el canvi de nom del dibuix quan l'usuari acaba d'editar-lo.
    form.inputName.addEventListener("focus", startEdit);
    form.inputName.addEventListener("change", finishEdit);

    document.getElementById("btnUndo").addEventListener("click", undoDrawing);
    document.getElementById("btnRedo").addEventListener("click", redoDrawing);

    // Cada botó activa una eina diferent.
    document.getElementById("btnAddCircle").addEventListener("click", function() {
        currentType = "CIRCLE";
        currentPointsFigure = null;
        selectedFigure = null;
        redraw();
        renderFigureList();
    });

    document.getElementById("btnAddSquare").addEventListener("click", function() {
        currentType = "SQUARE";
        currentPointsFigure = null;
        selectedFigure = null;
        redraw();
        renderFigureList();
    });

    document.getElementById("btnAddTriangle").addEventListener("click", function() {
        currentType = "TRIANGLE";
        currentPointsFigure = null;
        selectedFigure = null;
        redraw();
        renderFigureList();
    });

    document.getElementById("btnAddStar").addEventListener("click", function() {
        currentType = "STAR";
        currentPointsFigure = null;
        selectedFigure = null;
        redraw();
        renderFigureList();
    });

    document.getElementById("btnAddFreehand").addEventListener("click", function() {
        currentType = "FREEHAND";
        currentPointsFigure = null;
        selectedFigure = null;
        redraw();
        renderFigureList();
    });

    document.getElementById("btnAddPoints").addEventListener("click", function() {
        currentType = "POINTS";
        currentPointsFigure = null;
        selectedFigure = null;
        redraw();
        renderFigureList();
    });

    // Canvia les dimensions lògiques del canvas.
    inputCanvasWidth.addEventListener("focus", startEdit);
    inputCanvasWidth.addEventListener("input", function() {
        const width = Number(inputCanvasWidth.value);
        if (Number.isInteger(width) && width > 0) {
            canvas.width = width;
            redraw();
        }
    });
    inputCanvasWidth.addEventListener("change", finishEdit);

    inputCanvasHeight.addEventListener("focus", startEdit);
    inputCanvasHeight.addEventListener("input", function() {
        const height = Number(inputCanvasHeight.value);
        if (Number.isInteger(height) && height > 0) {
            canvas.height = height;
            redraw();
        }
    });
    inputCanvasHeight.addEventListener("change", finishEdit);

    // El color s'aplica a la figura seleccionada; la mida, només a les bàsiques.
    inputColor.addEventListener("focus", startEdit);
    inputColor.addEventListener("input", function() {
        if (selectedFigure) {
            selectedFigure.color = inputColor.value;
            redraw();
        }
    });
    inputColor.addEventListener("change", finishEdit);

    inputSize.addEventListener("focus", startEdit);
    inputSize.addEventListener("input", function() {
        if (selectedFigure && selectedFigure.x !== undefined) {
            selectedFigure.size = Number(inputSize.value) || 50;
            redraw();
        }
    });
    inputSize.addEventListener("change", finishEdit);

    // Elimina la figura seleccionada.
    document.getElementById("btnDeleteSelected").addEventListener("click", function() {
        if (!selectedFigure) return;
        rememberState();

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

    // Elimina totes les figures.
    document.getElementById("btnDeleteAll").addEventListener("click", function() {
        if (figures.length > 0) rememberState();
        figures = [];
        selectedFigure = null;
        currentPointsFigure = null;
        redraw();
        renderFigureList();
        saveDrawing();
    });

    // Selecciona una figura existent o en crea una de nova.
    canvas.addEventListener("mousedown", function(event) {
        finishEdit();
        const pos = getMousePos(event);
        canvasChanged = false;
        let clicked = null;

        // Busca una figura dins del radi del clic, començant per l'última dibuixada.
        for (let i = figures.length - 1; i >= 0; i--) {
            const fig = figures[i];
            if (fig.x !== undefined &&
                Math.hypot(pos.x - fig.x, pos.y - fig.y) <= (fig.size || 50) / 2) {
                clicked = fig;
                break;
            }
        }

        if (clicked) {
            selectedFigure = clicked;
            isDragging = true;
            dragStartState = getEditorState();
            dragChanged = false;
            inputColor.value = clicked.color;
            inputSize.value = clicked.size || 50;
            redraw();
            renderFigureList();
            return;
        }

        // Les figures a mà alçada i per punts no es poden seleccionar ni moure.
        selectedFigure = null;
        renderFigureList();

        if (currentType === "FREEHAND") {
            // Desa cada posició del ratolí per formar el traç lliure.
            rememberState();
            isDrawing = true;
            currentFreehandFigure = {
                name: "Traç " + (figures.length + 1),
                type: "FREEHAND",
                color: inputColor.value,
                points: [pos]
            };
            figures.push(currentFreehandFigure);
            canvasChanged = true;
            renderFigureList();
        } else if (currentType === "POINTS") {
            // Cada clic afegeix un punt a la figura actual.
            rememberState();
            if (!currentPointsFigure) {
                currentPointsFigure = {
                    name: "Punts " + (figures.length + 1),
                    type: "POINTS",
                    color: inputColor.value,
                    points: []
                };
                figures.push(currentPointsFigure);
            }
            currentPointsFigure.points.push(pos);
            canvasChanged = true;
            redraw();
            renderFigureList();
        } else {
            // Crea una figura bàsica centrada al lloc del clic.
            rememberState();
            figures.push({
                name: currentType + " " + (figures.length + 1),
                type: currentType,
                color: inputColor.value,
                x: pos.x,
                y: pos.y,
                size: Number(inputSize.value) || 50
            });
            canvasChanged = true;
            redraw();
            renderFigureList();
        }
    });

    // Arrossega una figura bàsica o continua el traç lliure.
    canvas.addEventListener("mousemove", function(event) {
        const pos = getMousePos(event);

        if (isDragging && selectedFigure) {
            // El centre de la figura segueix directament el cursor.
            if (selectedFigure.x === pos.x && selectedFigure.y === pos.y) return;
            if (!dragChanged) {
                undoStack.push(dragStartState);
                redoStack.length = 0;
                dragChanged = true;
            }
            selectedFigure.x = pos.x;
            selectedFigure.y = pos.y;
            canvasChanged = true;
            redraw();
            return;
        }

        if (isDrawing && currentType === "FREEHAND") {
            currentFreehandFigure.points.push(pos);
            canvasChanged = true;
            redraw();
        }
    });

    // Acaba l'arrossegament o el traç quan es deixa anar el ratolí.
    window.addEventListener("mouseup", function() {
        if (canvasChanged) saveDrawing();
        canvasChanged = false;
        isDragging = false;
        isDrawing = false;
        currentFreehandFigure = null;
        dragStartState = null;
        dragChanged = false;
    });
}

// Recarrega el dibuix que correspon a l'URL en navegar enrere o endavant.
window.addEventListener("popstate", function() {
    window.location.reload();
});
