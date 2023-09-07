
// This is the command queue that the user will add commands to and then execute.
commandQueue = [];

function commandSelectEvent(event) {
    document.getElementById("place-options").style.opacity = event.target.value === "PLACE" ? "1" : "0";
    document.getElementById("add-command").disabled = event.target.value === "";
}

function updateCommandQueueView() {
    const isEmpty = commandQueue.length === 0
    document.getElementById("command-queue-block").innerText = isEmpty ? "" : JSON.stringify(commandQueue, null, 2);
    document.getElementById("execute-command-queue").disabled = isEmpty;
    document.getElementById("clear-command-queue").disabled = isEmpty;
}

function addCommandEvent(event) {
    let cmd = { };
    cmd.command = document.getElementById("command-select").value;
    if (cmd.command === "PLACE") {
        cmd.x = document.getElementById("x-select").value;
        cmd.y = document.getElementById("y-select").value;
        cmd.facing = document.getElementById("facing-select").value;
    }
    commandQueue.push(cmd);
    updateCommandQueueView();
}

function clearCommandQueue() {
    commandQueue = [];
    updateCommandQueueView();
}

function executeCommandQueue() {
    fetch('robotapi/', {
        method: 'POST',
        body: JSON.stringify(commandQueue),
        headers: {
            'Content-type': 'application/json; charset=UTF-8'
        }
    }).then(function (response) {
        if (response.ok) {
            return response.json();
        }
        return Promise.reject(response);
    }).then(function (data) {
        const resultsBlock = document.getElementById("results-block");
        resultsBlock.innerText = JSON.stringify(data, null, 2);
    }).catch(function (error) {
        const resultsBlock = document.getElementById("results-block");
        resultsBlock.innerText = "An unexpected error has occurred";
    });

}

function init() {
    // Init some UI state
    document.getElementById("place-options").style.opacity = "0";
    document.getElementById("add-command").disabled = true;
    updateCommandQueueView();

    // Hook some events
    document.getElementById("command-select").addEventListener("change", commandSelectEvent);
    document.getElementById("add-command").addEventListener("click", addCommandEvent);
    document.getElementById("execute-command-queue").addEventListener("click", executeCommandQueue);
    document.getElementById("clear-command-queue").addEventListener("click", clearCommandQueue);
}

document.addEventListener("DOMContentLoaded", function() {
    init();
});