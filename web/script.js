document.addEventListener("DOMContentLoaded", function () {
    const vehicleForm = document.getElementById("vehicleForm");
    const searchForm = document.getElementById("searchForm");
    const vehicleTableBody = document.getElementById("vehicleTableBody");
    const editVehicleForm = document.getElementById("editVehicleForm");

    if (vehicleForm) {
        vehicleForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const type = document.getElementById("type").value.trim();
            const vehicleNumber = document.getElementById("vehicleNumber").value.trim();
            const ownerName = document.getElementById("ownerName").value.trim();
            const color = document.getElementById("color").value.trim();
            const message = document.getElementById("message");

            if (type === "" || vehicleNumber === "" || ownerName === "" || color === "") {
                showMessage(message, "Please fill all fields.", "error");
                return;
            }

            let vehicles = JSON.parse(localStorage.getItem("vehicles")) || [];

            const exists = vehicles.some(vehicle =>
                vehicle.vehicleNumber.toLowerCase() === vehicleNumber.toLowerCase()
            );

            if (exists) {
                showMessage(message, "Vehicle already exists.", "error");
                return;
            }

            const vehicle = {
                type: type,
                vehicleNumber: vehicleNumber,
                ownerName: ownerName,
                color: color
            };

            vehicles.push(vehicle);
            localStorage.setItem("vehicles", JSON.stringify(vehicles));

            showMessage(message, "Vehicle added successfully.", "success");
            vehicleForm.reset();
        });
    }

    if (vehicleTableBody) {
        loadVehicles();
    }

    if (searchForm) {
        searchForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const searchNumber = document.getElementById("searchNumber").value.trim();
            const searchResult = document.getElementById("searchResult");

            let vehicles = JSON.parse(localStorage.getItem("vehicles")) || [];

            const foundVehicle = vehicles.find(vehicle =>
                vehicle.vehicleNumber.toLowerCase() === searchNumber.toLowerCase()
            );

            if (foundVehicle) {
                searchResult.innerHTML =
                    `<strong>Type:</strong> ${foundVehicle.type}<br>
                     <strong>Vehicle Number:</strong> ${foundVehicle.vehicleNumber}<br>
                     <strong>Owner Name:</strong> ${foundVehicle.ownerName}<br>
                     <strong>Color:</strong> ${foundVehicle.color}`;
            } else {
                searchResult.textContent = "Vehicle not found.";
            }
        });
    }

    if (editVehicleForm) {
        loadVehicleForEdit();

        editVehicleForm.addEventListener("submit", function (event) {
            event.preventDefault();

            const params = new URLSearchParams(window.location.search);
            const index = params.get("index");
            const editMessage = document.getElementById("editMessage");

            let vehicles = JSON.parse(localStorage.getItem("vehicles")) || [];

            if (index === null || vehicles[index] === undefined) {
                showMessage(editMessage, "Vehicle not found.", "error");
                return;
            }

            const updatedType = document.getElementById("editType").value.trim();
            const updatedVehicleNumber = document.getElementById("editVehicleNumber").value.trim();
            const updatedOwnerName = document.getElementById("editOwnerName").value.trim();
            const updatedColor = document.getElementById("editColor").value.trim();

            if (updatedType === "" || updatedVehicleNumber === "" || updatedOwnerName === "" || updatedColor === "") {
                showMessage(editMessage, "Please fill all fields.", "error");
                return;
            }

            const duplicate = vehicles.some((vehicle, i) =>
                i != index &&
                vehicle.vehicleNumber.toLowerCase() === updatedVehicleNumber.toLowerCase()
            );

            if (duplicate) {
                showMessage(editMessage, "Another vehicle already uses this number.", "error");
                return;
            }

            vehicles[index] = {
                type: updatedType,
                vehicleNumber: updatedVehicleNumber,
                ownerName: updatedOwnerName,
                color: updatedColor
            };

            localStorage.setItem("vehicles", JSON.stringify(vehicles));
            showMessage(editMessage, "Vehicle updated successfully.", "success");

            setTimeout(() => {
                window.location.href = "viewVehicles.html";
            }, 700);
        });
    }
});

function showMessage(element, text, type) {
    element.textContent = text;
    element.className = "message " + type;
}

function loadVehicles() {
    const vehicleTableBody = document.getElementById("vehicleTableBody");
    const noDataMessage = document.getElementById("noDataMessage");
    let vehicles = JSON.parse(localStorage.getItem("vehicles")) || [];

    vehicleTableBody.innerHTML = "";

    if (vehicles.length === 0) {
        noDataMessage.style.display = "block";
    } else {
        noDataMessage.style.display = "none";

        vehicles.forEach((vehicle, index) => {
            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${vehicle.type}</td>
                <td>${vehicle.vehicleNumber}</td>
                <td>${vehicle.ownerName}</td>
                <td>${vehicle.color}</td>
                <td>
                    <div class="action-buttons">
                        <button type="button" onclick="editVehicle(${index})">Edit</button>
                        <button type="button" class="danger-btn" onclick="deleteVehicle(${index})">Delete</button>
                    </div>
                </td>
            `;

            vehicleTableBody.appendChild(row);
        });
    }
}

function deleteVehicle(index) {
    let vehicles = JSON.parse(localStorage.getItem("vehicles")) || [];

    const confirmed = confirm("Are you sure you want to delete this vehicle?");
    if (!confirmed) {
        return;
    }

    vehicles.splice(index, 1);
    localStorage.setItem("vehicles", JSON.stringify(vehicles));

    loadVehicles();
}

function editVehicle(index) {
    window.location.href = `editVehicle.html?index=${index}`;
}

function loadVehicleForEdit() {
    const params = new URLSearchParams(window.location.search);
    const index = params.get("index");

    let vehicles = JSON.parse(localStorage.getItem("vehicles")) || [];

    if (index === null || vehicles[index] === undefined) {
        return;
    }

    const vehicle = vehicles[index];

    document.getElementById("editType").value = vehicle.type;
    document.getElementById("editVehicleNumber").value = vehicle.vehicleNumber;
    document.getElementById("editOwnerName").value = vehicle.ownerName;
    document.getElementById("editColor").value = vehicle.color;
}