let figures = [];
const canvas = document.getElementById("canvas");
const ctx = canvas.getContext("2d");

const form = document.getElementById("drawingForm");

// Envia el formulari i desa el dibuix sense recarregar la pàgina
if (form) {
    form.addEventListener("submit", async function(event) {
        event.preventDefault();

        const id = form.inputDrawingId.value;
        const name = form.inputName.value;

        const dto = {
            id: id !== "" ? id : null,
            name: name,
            figures: figures
        };

        const response = await fetch("/drawings/save", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(dto)
        });

        if (response.ok) {
            const data = await response.json();
            
            form.inputDrawingId.value = data.id;
            document.getElementById("saveStatus").textContent = "Dibuix desat correctament!";
            
            if (id === "") {
                window.history.pushState({}, "", "/drawings/editor?id=" + data.id);
            }
        } else {
            document.getElementById("saveStatus").textContent = "Error al desar el dibuix.";
        }
    });
}