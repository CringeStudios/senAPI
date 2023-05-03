const host = 'https://senpai.cringe-studios.com/api/senpai'
const flatHost = 'https://senpai.cringe-studios.com'

function displayResult(obj){
    theTable = document.getElementById("outputTable");
    theadOfTable = theTable.createTHead();
    theadRowOfTable = theadOfTable.insertRow(0);
    theadRowOfTable.insertCell().innerText = "Key";
    theadRowOfTable.insertCell().innerText = "Value";
    tbodyOfTable = theTable.createTBody();
    for(const [key, value] of Object.entries(obj)){
        var row = tbodyOfTable.insertRow();
        row.insertCell().innerText = key;
        row.insertCell().innerText = value;
    };
}

async function senpaiPost(){
    sender = document.getElementById("inputName").value;
    //recipient = document.getElementById("inputSenpaiName").value;
    recipients = Array.from(document.getElementById('senpaiList').childNodes);
    recipients = recipients.filter(reci => (reci.value == null || reci.value == ""));
    if(recipients.length == 0){
        recipients.push("");
    }

    message = document.getElementById("inputMessage").value;

    const response = await fetch(host, {
        method: 'POST',
        body: JSON.stringify({"sender": sender, "recipients": recipients, "message": message}), // string or object
        headers: {
            'Content-Type': 'application/json'
        }
    });
    const myJson = await response.json(); //extract JSON from the http response
    document.getElementById("copyableURL").innerText = flatHost + "?" + myJson["id"];
    displayResult(myJson);
}
async function senpaiGet(){
    id = document.getElementById("inputID").value;
    localHostname = host + "/" + id;
    const response = await fetch(localHostname);
    const myJson = await response.json(); //extract JSON from the http response
    displayResult(myJson);
}
async function senpaiAccept(){ //PUT
    id = document.getElementById("inputID").value;
    localHostname = host + "/" + id + "/accept";
    const response = await fetch(localHostname, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
        //TODO: body
    });
    const myJson = await response.json(); //extract JSON from the http response
    displayResult(myJson);
}
async function senpaiReject(){ //PUT
    id = document.getElementById("inputID").value;
    localHostname = host + "/" + id + "/reject";
    const response = await fetch(localHostname, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
        //TODO: body
    });
    const myJson = await response.json(); //extract JSON from the http response
    displayResult(myJson);
}
function visModeCompile(){
    document.getElementById('inputName').removeAttribute("hidden");
    document.getElementById('senpaiList').removeAttribute("hidden");
    //document.getElementById('inputSenpaiName').removeAttribute("hidden");
    document.getElementById('inputMessage').removeAttribute("hidden");

    document.getElementById('inputID').setAttribute("hidden", "");

    document.getElementById('inputSend').removeAttribute("hidden");
    document.getElementById('outputTable').innerHTML = "";
    document.getElementById('copyableURL').innerHTML = "";
    document.getElementById('inputGet').setAttribute("hidden", "");
    document.getElementById('inputAccept').setAttribute("hidden", "");
    document.getElementById('inputReject').setAttribute("hidden", "");
}

function visModeGet(){
    document.getElementById('inputName').setAttribute("hidden", "");
    //document.getElementById('inputSenpaiName').setAttribute("hidden", "");
    document.getElementById('senpaiList').setAttribute("hidden", "");
    document.getElementById('inputMessage').setAttribute("hidden", "");

    document.getElementById('inputID').removeAttribute("hidden");

    document.getElementById('inputSend').setAttribute("hidden", "");
    document.getElementById('outputTable').innerHTML = "";
    document.getElementById('copyableURL').innerHTML = "";
    document.getElementById('inputGet').removeAttribute("hidden");
    document.getElementById('inputAccept').setAttribute("hidden", "");
    document.getElementById('inputReject').setAttribute("hidden", "");
}

function visModeAccept(){
    document.getElementById('inputName').setAttribute("hidden", "");
    //document.getElementById('inputSenpaiName').setAttribute("hidden", "");
    document.getElementById('senpaiList').setAttribute("hidden", "");
    document.getElementById('inputMessage').setAttribute("hidden", "");

    document.getElementById('inputID').removeAttribute("hidden");

    document.getElementById('inputSend').setAttribute("hidden", "");
    document.getElementById('outputTable').innerHTML = "";
    document.getElementById('copyableURL').innerHTML = "";
    document.getElementById('inputGet').removeAttribute("hidden");
    document.getElementById('inputAccept').removeAttribute("hidden");
    document.getElementById('inputReject').removeAttribute("hidden");
}

function inputSenpaiNameKeyUp(evt){
    targetElement = evt.target;
    theParent = targetElement.parentNode;
    alreadyFoundEmptyNode = false;
    senpaiList = document.getElementById('senpaiList').childNodes;
    for(var i = senpaiList.length - 1; i >= 0; i--){
        if(senpaiList[i].value == null || senpaiList[i].value == ""){
                alreadyFoundEmptyNode = true;
                senpaiList[i].remove();
        }
    }

    clonedElement = document.getElementsByClassName('inputSenpaiNameTemplate')[0].cloneNode();
    clonedElement.setAttribute("class", "inputSenpaiName");
    clonedElement.removeAttribute("hidden");
    theParent.appendChild(clonedElement);
}