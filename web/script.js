document.addEventListener("DOMContentLoaded", function () {
    const vehicleForm = document.getElementById("vehicleForm");
    const searchForm = document.getElementById("searchForm");
    const vehicleTableBody = document.getElementById("vehicleTableBody");
    const editVehicleForm = document.getElementById("editVehicleForm");

   // if (vehicleForm) {
      //  vehicleForm.addEventListener("submit", function (event) {
            //event.preventDefault();

            const type = document.getElementById("type").value.trim();
            const vehicleNumber = document.getElementById("vehicleNumber").value.trim();
            const ownerName = document.getElementById("ownerName").value.trim();
            const color = document.getElementById("color").value.trim();
            const message = document.getElementById("message");

            if (type === "" || vehicleNumber === "" || ownerName === "" || color === "") {
                message.textContent = "Please fill all fields.";
                return;
            }

            let vehicles = JSON.parse(localStorage.getItem("vehicles")) || [];

            const exists = vehicles.some(vehicle =>
                vehicle.vehicleNumber.toLowerCase() === vehicleNumber.toLowerCase()
            );

            if (exists) {
                message.textContent = "Vehicle already exists.";
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

            message.textContent = "Vehicle added successfully.";
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
                searchResult.textContent =
                    `Type: ${foundVehicle.type}, Vehicle Number: ${foundVehicle.vehicleNumber}, Owner Name: ${foundVehicle.ownerName}, Color: ${foundVehicle.color}`;
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
                editMessage.textContent = "Vehicle not found.";
                return;
            }

            const updatedType = document.getElementById("editType").value.trim();
            const updatedVehicleNumber = document.getElementById("editVehicleNumber").value.trim();
            const updatedOwnerName = document.getElementById("editOwnerName").value.trim();
            const updatedColor = document.getElementById("editColor").value.trim();

            if (updatedType === "" || updatedVehicleNumber === "" || updatedOwnerName === "" || updatedColor === "") {
                editMessage.textContent = "Please fill all fields.";
                return;
            }

            const duplicate = vehicles.some((vehicle, i) =>
                i != index &&
                vehicle.vehicleNumber.toLowerCase() === updatedVehicleNumber.toLowerCase()
            );

            if (duplicate) {
                editMessage.textContent = "Another vehicle already uses this vehicle number.";
                return;
            }

            vehicles[index] = {
                type: updatedType,
                vehicleNumber: updatedVehicleNumber,
                ownerName: updatedOwnerName,
                color: updatedColor
            };

            localStorage.setItem("vehicles", JSON.stringify(vehicles));
            editMessage.textContent = "Vehicle updated successfully.";

            setTimeout(() => {
                window.location.href = "viewVehicles.html";
            }, 800);
        });
    }
});

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
                    <button onclick="editVehicle(${index})">Edit</button>
                    <button onclick="deleteVehicle(${index})">Delete</button>
                </td>
            `;

            vehicleTableBody.appendChild(row);
        });
    }
}

function deleteVehicle(index) {
    let vehicles = JSON.parse(localStorage.getItem("vehicles")) || [];

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