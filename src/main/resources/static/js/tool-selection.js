let activeBtn = document.getElementById("btnAddCircle");

if (activeBtn) {
    activeBtn.classList.add("active");
}

function setActiveButton(btn) {
    activeBtn.classList.remove("active");
    btn.classList.add("active");
    activeBtn = btn;
}

document.getElementById("btnAddCircle")?.addEventListener("click", function() { setActiveButton(this); });
document.getElementById("btnAddSquare")?.addEventListener("click", function() { setActiveButton(this); });
document.getElementById("btnAddTriangle")?.addEventListener("click", function() { setActiveButton(this); });
document.getElementById("btnAddStar")?.addEventListener("click", function() { setActiveButton(this); });
document.getElementById("btnAddFreehand")?.addEventListener("click", function() { setActiveButton(this); });
document.getElementById("btnAddPoints")?.addEventListener("click", function() { setActiveButton(this); });