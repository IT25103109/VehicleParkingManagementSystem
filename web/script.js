document.addEventListener("DOMContentLoaded", function () {
    initLiveClock();
    initDashboard();
    initVehicleForm();
    initSearchForm();
    initViewPage();
    initEditForm();
});

function initLiveClock() {
    const clock = document.getElementById("liveClock");
    const date = document.getElementById("liveDate");

    if (!clock || !date) return;

    function updateClock() {
        const now = new Date();
        clock.textContent = now.toLocaleTimeString();
        date.textContent = now.toLocaleDateString(undefined, {
            weekday: "long",
            year: "numeric",
            month: "long",
            day: "numeric"
        });
    }

    updateClock();
    setInterval(updateClock, 1000);
}

function getVehicles() {
    let vehicles = JSON.parse(localStorage.getItem("vehicles")) || [];
    let changed = false;

    vehicles = vehicles.map((vehicle, index) => {
        if (!vehicle.id) {
            vehicle.id = `vehicle-${index}-${vehicle.vehicleNumber || Date.now()}`;
            changed = true;
        }
        if (!vehicle.createdAt) {
            vehicle.createdAt = new Date().toISOString();
            changed = true;
        }
        if (!vehicle.updatedAt) {
            vehicle.updatedAt = vehicle.createdAt;
            changed = true;
        }
        return vehicle;
    });

    if (changed) {
        localStorage.setItem("vehicles", JSON.stringify(vehicles));
    }

    return vehicles;
}

function saveVehicles(vehicles) {
    localStorage.setItem("vehicles", JSON.stringify(vehicles));
}

function getActivities() {
    return JSON.parse(localStorage.getItem("vehicleActivities")) || [];
}

function saveActivities(activities) {
    localStorage.setItem("vehicleActivities", JSON.stringify(activities));
}

function addActivity(action, vehicle) {
    const activities = getActivities();

    activities.unshift({
        title: `${action}: ${vehicle.vehicleNumber}`,
        time: new Date().toISOString(),
        details: `${vehicle.type} - ${vehicle.ownerName}`
    });

    saveActivities(activities.slice(0, 8));
}

function initDashboard() {
    const total = document.getElementById("dashboardTotal");
    const cars = document.getElementById("dashboardCars");
    const bikes = document.getElementById("dashboardBikes");
    const large = document.getElementById("dashboardLarge");
    const activityList = document.getElementById("recentActivityList");

    if (!total && !activityList) return;

    const vehicles = getVehicles();
    const largeTypes = ["Bus", "Lorry"];

    if (total) total.textContent = vehicles.length;
    if (cars) cars.textContent = vehicles.filter(v => v.type === "Car").length;
    if (bikes) bikes.textContent = vehicles.filter(v => v.type === "Bike").length;
    if (large) large.textContent = vehicles.filter(v => largeTypes.includes(v.type)).length;

    if (activityList) {
        const activities = getActivities();
        activityList.innerHTML = "";

        if (activities.length === 0) {
            activityList.innerHTML = `<li><span class="activity-title">No recent activity</span><span class="activity-time">Actions will appear here.</span></li>`;
        } else {
            activities.forEach(activity => {
                const item = document.createElement("li");
                item.innerHTML = `
                    <span class="activity-title">${escapeHtml(activity.title)}</span>
                    <span class="activity-time">${formatDateTime(activity.time)} · ${escapeHtml(activity.details || "")}</span>
                `;
                activityList.appendChild(item);
            });
        }
    }
}

function initVehicleForm() {
    const form = document.getElementById("vehicleForm");
    if (!form) return;

    const message = document.getElementById("message");

    const previewMap = [
        ["type", "previewType"],
        ["vehicleNumber", "previewNumber"],
        ["ownerName", "previewOwner"],
        ["color", "previewColor"]
    ];

    previewMap.forEach(([from, to]) => {
        const input = document.getElementById(from);
        if (input) {
            input.addEventListener("input", updateAddPreview);
            input.addEventListener("change", updateAddPreview);
        }
    });

    updateAddPreview();

    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const type = document.getElementById("type").value.trim();
        const vehicleNumber = document.getElementById("vehicleNumber").value.trim();
        const ownerName = document.getElementById("ownerName").value.trim();
        const color = document.getElementById("color").value.trim();

        if (!type || !vehicleNumber || !ownerName || !color) {
            showMessage(message, "Please fill all fields.", "error");
            return;
        }

        const vehicles = getVehicles();
        const exists = vehicles.some(v => v.vehicleNumber.toLowerCase() === vehicleNumber.toLowerCase());

        if (exists) {
            showMessage(message, "Vehicle already exists.", "error");
            return;
        }

        const vehicle = {
            id: `vehicle-${Date.now()}`,
            type,
            vehicleNumber,
            ownerName,
            color,
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString()
        };

        vehicles.push(vehicle);
        saveVehicles(vehicles);
        addActivity("Added", vehicle);

        showMessage(message, "Vehicle added successfully.", "success");
        form.reset();
        updateAddPreview();
    });
}

function updateAddPreview() {
    const type = document.getElementById("type");
    const number = document.getElementById("vehicleNumber");
    const owner = document.getElementById("ownerName");
    const color = document.getElementById("color");

    const previewType = document.getElementById("previewType");
    const previewNumber = document.getElementById("previewNumber");
    const previewOwner = document.getElementById("previewOwner");
    const previewColor = document.getElementById("previewColor");

    if (!previewType) return;

    previewType.textContent = type?.value || "-";
    previewNumber.textContent = number?.value.trim() || "-";
    previewOwner.textContent = owner?.value.trim() || "-";
    previewColor.textContent = color?.value.trim() || "-";
}

function initSearchForm() {
    const form = document.getElementById("searchForm");
    if (!form) return;

    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const searchNumber = document.getElementById("searchNumber").value.trim();
        const result = document.getElementById("searchResult");
        const vehicles = getVehicles();

        const found = vehicles.find(v => v.vehicleNumber.toLowerCase() === searchNumber.toLowerCase());

        if (found) {
            result.innerHTML = `
                <strong>Type:</strong> ${escapeHtml(found.type)}<br>
                <strong>Vehicle Number:</strong> ${escapeHtml(found.vehicleNumber)}<br>
                <strong>Owner Name:</strong> ${escapeHtml(found.ownerName)}<br>
                <strong>Color:</strong> ${escapeHtml(found.color)}<br>
                <strong>Last Updated:</strong> ${formatDateTime(found.updatedAt)}
            `;
        } else {
            result.textContent = "Vehicle not found.";
        }
    });
}

function initViewPage() {
    const tableBody = document.getElementById("vehicleTableBody");
    if (!tableBody) return;

    const search = document.getElementById("tableSearch");
    const filter = document.getElementById("typeFilter");

    if (search) search.addEventListener("input", renderVehicleTable);
    if (filter) filter.addEventListener("change", renderVehicleTable);

    renderVehicleTable();
}

function renderVehicleTable() {
    const tableBody = document.getElementById("vehicleTableBody");
    const noData = document.getElementById("noDataMessage");
    const search = document.getElementById("tableSearch");
    const filter = document.getElementById("typeFilter");

    const vehicleCount = document.getElementById("vehicleCount");
    const carsCount = document.getElementById("carsCount");
    const bikesCount = document.getElementById("bikesCount");
    const largeCount = document.getElementById("largeCount");

    const vehicles = getVehicles();
    const largeTypes = ["Bus", "Lorry"];

    if (vehicleCount) vehicleCount.textContent = vehicles.length;
    if (carsCount) carsCount.textContent = vehicles.filter(v => v.type === "Car").length;
    if (bikesCount) bikesCount.textContent = vehicles.filter(v => v.type === "Bike").length;
    if (largeCount) largeCount.textContent = vehicles.filter(v => largeTypes.includes(v.type)).length;

    const searchText = search ? search.value.trim().toLowerCase() : "";
    const typeValue = filter ? filter.value : "";

    const filtered = vehicles.filter(vehicle => {
        const matchesSearch =
            vehicle.vehicleNumber.toLowerCase().includes(searchText) ||
            vehicle.ownerName.toLowerCase().includes(searchText) ||
            vehicle.color.toLowerCase().includes(searchText);

        const matchesType = !typeValue || vehicle.type === typeValue;

        return matchesSearch && matchesType;
    });

    tableBody.innerHTML = "";

    if (filtered.length === 0) {
        noData.style.display = "block";
        return;
    }

    noData.style.display = "none";

    filtered.forEach(vehicle => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td><span class="badge">${escapeHtml(vehicle.type)}</span></td>
            <td>${escapeHtml(vehicle.vehicleNumber)}</td>
            <td>${escapeHtml(vehicle.ownerName)}</td>
            <td>${escapeHtml(vehicle.color)}</td>
            <td>${formatShortDate(vehicle.updatedAt)}</td>
            <td>
                <div class="action-buttons">
                    <button type="button" class="secondary-btn" onclick="editVehicle('${escapeJs(vehicle.id)}')">Edit</button>
                    <button type="button" class="danger-btn" onclick="deleteVehicle('${escapeJs(vehicle.id)}')">Delete</button>
                </div>
            </td>
        `;
        tableBody.appendChild(row);
    });
}

function deleteVehicle(id) {
    const vehicles = getVehicles();
    const vehicle = vehicles.find(v => v.id === id);
    if (!vehicle) return;

    const confirmed = confirm("Are you sure you want to delete this vehicle?");
    if (!confirmed) return;

    const updated = vehicles.filter(v => v.id !== id);
    saveVehicles(updated);
    addActivity("Deleted", vehicle);
    renderVehicleTable();
}

function editVehicle(id) {
    window.location.href = `editVehicle.html?id=${encodeURIComponent(id)}`;
}

function initEditForm() {
    const form = document.getElementById("editVehicleForm");
    if (!form) return;

    const params = new URLSearchParams(window.location.search);
    const id = params.get("id");
    const vehicles = getVehicles();
    const vehicle = vehicles.find(v => v.id === id);
    const message = document.getElementById("editMessage");

    if (!vehicle) {
        showMessage(message, "Vehicle not found.", "error");
        return;
    }

    document.getElementById("editType").value = vehicle.type;
    document.getElementById("editVehicleNumber").value = vehicle.vehicleNumber;
    document.getElementById("editOwnerName").value = vehicle.ownerName;
    document.getElementById("editColor").value = vehicle.color;

    ["editType", "editVehicleNumber", "editOwnerName", "editColor"].forEach(idName => {
        const input = document.getElementById(idName);
        if (input) {
            input.addEventListener("input", updateEditPreview);
            input.addEventListener("change", updateEditPreview);
        }
    });

    updateEditPreview();

    form.addEventListener("submit", function (event) {
        event.preventDefault();

        const updatedType = document.getElementById("editType").value.trim();
        const updatedNumber = document.getElementById("editVehicleNumber").value.trim();
        const updatedOwner = document.getElementById("editOwnerName").value.trim();
        const updatedColor = document.getElementById("editColor").value.trim();

        if (!updatedType || !updatedNumber || !updatedOwner || !updatedColor) {
            showMessage(message, "Please fill all fields.", "error");
            return;
        }

        const duplicate = vehicles.some(v =>
            v.id !== id && v.vehicleNumber.toLowerCase() === updatedNumber.toLowerCase()
        );

        if (duplicate) {
            showMessage(message, "Another vehicle already uses this number.", "error");
            return;
        }

        const updatedVehicle = {
            ...vehicle,
            type: updatedType,
            vehicleNumber: updatedNumber,
            ownerName: updatedOwner,
            color: updatedColor,
            updatedAt: new Date().toISOString()
        };

        const newVehicles = vehicles.map(v => v.id === id ? updatedVehicle : v);
        saveVehicles(newVehicles);
        addActivity("Updated", updatedVehicle);

        showMessage(message, "Vehicle updated successfully.", "success");

        setTimeout(() => {
            window.location.href = "viewVehicles.html";
        }, 700);
    });
}

function updateEditPreview() {
    const type = document.getElementById("editType");
    const number = document.getElementById("editVehicleNumber");
    const owner = document.getElementById("editOwnerName");
    const color = document.getElementById("editColor");

    const previewType = document.getElementById("editPreviewType");
    const previewNumber = document.getElementById("editPreviewNumber");
    const previewOwner = document.getElementById("editPreviewOwner");
    const previewColor = document.getElementById("editPreviewColor");

    if (!previewType) return;

    previewType.textContent = type?.value || "-";
    previewNumber.textContent = number?.value.trim() || "-";
    previewOwner.textContent = owner?.value.trim() || "-";
    previewColor.textContent = color?.value.trim() || "-";
}

function showMessage(element, text, type) {
    element.textContent = text;
    element.className = `message-box ${type}`;
}

function formatDateTime(value) {
    if (!value) return "-";
    return new Date(value).toLocaleString();
}

function formatShortDate(value) {
    if (!value) return "-";
    return new Date(value).toLocaleDateString();
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function escapeJs(value) {
    return String(value).replaceAll("'", "\\'");
}