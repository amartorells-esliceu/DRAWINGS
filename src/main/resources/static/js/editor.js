let figures = [];
const canvas = document.getElementById("canvas");
const ctx = canvas.getContext("2d");

// Adapta el clic a la mida real del canvas.
function getMousePos(event) {
    const rect = canvas.getBoundingClientRect();
    const scaleX = canvas.width / rect.width;
    const scaleY = canvas.height / rect.height;

    return {
        x: (event.clientX - rect.left) * scaleX,
        y: (event.clientY - rect.top) * scaleY
    };
}

// Neteja el canvas i torna a pintar les figures.
function redraw() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    figures.forEach(drawFigure);
}

// Dibuixa una figura segons el seu tipus.
function drawFigure(figure) {
    ctx.fillStyle = figure.color;
    ctx.strokeStyle = figure.color;

    if (figure.type === "FREEHAND" || figure.type === "POINTS") {
        const points = figure.points;

        ctx.lineWidth = 3;
        ctx.beginPath();
        ctx.moveTo(points[0].x, points[0].y);
        points.slice(1).forEach(function(point) {
            ctx.lineTo(point.x, point.y);
        });
        ctx.stroke();

        if (figure.type === "POINTS") {
            points.forEach(function(point) {
                ctx.beginPath();
                ctx.arc(point.x, point.y, 4, 0, Math.PI * 2);
                ctx.fill();
            });
        }
        return;
    }

    const half = (figure.size || 50) / 2;
    ctx.beginPath();

    if (figure.type === "CIRCLE") {
        ctx.arc(figure.x, figure.y, half, 0, Math.PI * 2);
    } else if (figure.type === "SQUARE") {
        ctx.fillRect(figure.x - half, figure.y - half, half * 2, half * 2);
        return;
    } else if (figure.type === "TRIANGLE") {
        ctx.moveTo(figure.x, figure.y - half);
        ctx.lineTo(figure.x - half, figure.y + half);
        ctx.lineTo(figure.x + half, figure.y + half);
        ctx.closePath();
    } else if (figure.type === "STAR") {
        for (let i = 0; i < 14; i++) {
            const radius = (i % 2 === 0) ? half : half * 0.4;
            const angle = (i * Math.PI / 7) - (Math.PI / 2);
            const x = figure.x + Math.cos(angle) * radius;
            const y = figure.y + Math.sin(angle) * radius;

            if (i === 0) {
                ctx.moveTo(x, y);
            } else {
                ctx.lineTo(x, y);
            }
        }
        ctx.closePath();
    }

    ctx.fill();
}

// Carrega les figures desades en obrir l'editor.
figures = initialFigures || [];
redraw();

const form = document.getElementById("drawingForm");

// Desa el dibuix sense recarregar la pàgina.
if (form) {
    form.addEventListener("submit", async function(event) {
        event.preventDefault();

        const idInput = document.getElementById("inputDrawingId");
        const nameInput = document.getElementById("inputName");
        const saveStatus = document.getElementById("saveStatus");
        const id = idInput.value;

        const dto = {
            id: id !== "" ? id : null,
            name: nameInput.value,
            figures: figures
        };

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
        idInput.value = data.id;
        saveStatus.textContent = "Dibuix desat correctament!";

        if (id === "") {
            window.history.pushState({}, "", "/drawings/editor?id=" + data.id);
        }
    });
}

let currentType = "CIRCLE";
let isDrawing = false;
let currentFreehandFigure = null;
let currentPointsFigure = null;
const inputColor = document.getElementById("inputColor");
const inputSize = document.getElementById("inputSize");

if (form) {
document.getElementById("btnAddCircle").addEventListener("click", function() {
        currentType = "CIRCLE";
        currentPointsFigure = null;
    });

    document.getElementById("btnAddSquare").addEventListener("click", function() {
        currentType = "SQUARE";
        currentPointsFigure = null;
    });

    document.getElementById("btnAddTriangle").addEventListener("click", function() {
        currentType = "TRIANGLE";
        currentPointsFigure = null;
    });

    document.getElementById("btnAddStar").addEventListener("click", function() {
        currentType = "STAR";
        currentPointsFigure = null;
    });

    document.getElementById("btnAddFreehand").addEventListener("click", function() {
        currentType = "FREEHAND";
        currentPointsFigure = null;
    });

    document.getElementById("btnAddPoints").addEventListener("click", function() {
        currentType = "POINTS";
        currentPointsFigure = null;
    });

    // Inicia un traç a mà alçada.
    canvas.addEventListener("mousedown", function(event) {
        if (currentType !== "FREEHAND") return;

        isDrawing = true;
        const { x, y } = getMousePos(event);

        currentFreehandFigure = {
            type: "FREEHAND",
            color: inputColor.value,
            points: [{ x: x, y: y }]
        };
        figures.push(currentFreehandFigure);

        ctx.strokeStyle = inputColor.value;
        ctx.lineWidth = 3;
        ctx.beginPath();
        ctx.moveTo(x, y);
    });

    // Afegeix punts mentre es mou el ratolí.
    canvas.addEventListener("mousemove", function(event) {
        if (!isDrawing || currentType !== "FREEHAND") return;

        const { x, y } = getMousePos(event);

        currentFreehandFigure.points.push({ x: x, y: y });

        ctx.lineTo(x, y);
        ctx.stroke();
    });

    // Tanca el traç a mà alçada.
    canvas.addEventListener("mouseup", function() {
        isDrawing = false;
        currentFreehandFigure = null;
    });

    // Uneix cada punt nou amb l'anterior.
    canvas.addEventListener("click", function(event) {
        if (currentType !== "POINTS") return;

        const { x, y } = getMousePos(event);

        ctx.fillStyle = inputColor.value;
        ctx.strokeStyle = inputColor.value;
        ctx.lineWidth = 3;

        // Marca el punt seleccionat.
        ctx.beginPath();
        ctx.arc(x, y, 4, 0, Math.PI * 2);
        ctx.fill();

        if (currentPointsFigure === null) {
            // Crea la figura amb el primer punt.
            currentPointsFigure = {
                type: "POINTS",
                color: inputColor.value,
                points: [{ x: x, y: y }]
            };
            figures.push(currentPointsFigure);
        } else {
            // Uneix el punt nou amb l'últim.
            const pts = currentPointsFigure.points;
            const prev = pts[pts.length - 1];

            ctx.beginPath();
            ctx.moveTo(prev.x, prev.y);
            ctx.lineTo(x, y);
            ctx.stroke();

            pts.push({ x: x, y: y });
        }
    });

    // Dibuixa una figura bàsica amb la mida triada.
    canvas.addEventListener("click", function(event) {
        if (currentType === "FREEHAND" || currentType === "POINTS") return;

        const { x, y } = getMousePos(event);
        const size = Number(inputSize.value) || 50;

        figures.push({
            type: currentType,
            color: inputColor.value,
            x: x,
            y: y,
            size: size
        });
        drawFigure(figures[figures.length - 1]);
    });
}