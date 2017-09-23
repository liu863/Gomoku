
function createTable() {
	var tbl = document.getElementById("tbl");
	for (var r = 0; r < 16; r++) {
		var row = document.createElement("tr");
		for (var c = 0; c < 16; c++) {
			var cell = document.createElement("td");
			cell.width = "30px";
			cell.height = "30px";
			row.appendChild(cell);
			if (r == 0 && c == 0) continue;
			if (r === 0) {
				cell.innerHTML = c;
			}
			else if (c === 0) {
				cell.innerHTML = r;
			}
			else {
				cell.innerHTML = "";
			}
		}
		tbl.appendChild(row);
	}
}

var started = false;
var playerFirst = true;
var end = false;
var gameId = -1;
function startGame() {
	if (started) {
		alert("Game already started!");
		return;
	}
	started = true;

	var order = "start";
	if (document.getElementById("player").checked) {
		order += "player";
		document.getElementById("indicator").innerHTML = "Your turn";
		playerFirst = true;
	}
	else {
		order += "AI";
		document.getElementById("indicator").innerHTML = "AI's turn";
		playerFirst = false;
	}

	$.ajax({
		type: 'POST',
		url: order,
		success: function(response) {
			console.log(response);
			if (response.startsWith("move")) {
				var moves = response.substring(4).split(",");
				gameId = parseInt(moves[0]);
				setMove(moves[1], moves[2]);
				document.getElementById("indicator").innerHTML = "Your turn";
			}
			else {
				gameId = parseInt(response.substring(8));
			}
		},
		error: function(response) {
			alert(response);
		}
	});
}

function move() {
	if (!started) {
		alert("Game has not started!");
		return;
	}
	if (end) {
		alert("Refresh the webpage to start a new game");
		return;
	}
	var row = parseInt(document.getElementById("row").value);
	var col = parseInt(document.getElementById("col").value);
	console.log(row + ' ' + col);
	if (row < 1 || row > 15 || col < 1 || col > 15) {
		alert("Position out of bound");
		return;
	}
	var cell = document.getElementById("tbl").rows[row].cells[col];
	if (cell.innerHTML.localeCompare("") !== 0) {
		alert("Invalid position");
		return;
	}
	if (playerFirst) {
		cell.innerHTML = "<img src='black_piece25x25.png'/>";
	}
	else {
		cell.innerHTML = "<img src='white_piece25x25.png'/>";
	}
	document.getElementById("indicator").innerHTML = "AI's turn";

	var move = "move" + gameId + "," + (row - 1) + "," + (col - 1);
	$.ajax({
		type: 'POST',
		url: move,
		success: function(response) {
			console.log(response);
			if (response.startsWith("move")) {
				var moves = response.substring(4).split(",");
				setMove(moves[1], moves[2]);
				document.getElementById("indicator").innerHTML = "Your turn";
			}
			else if (response.startsWith("win")) {
				document.getElementById("indicator").innerHTML = "You Win!";
				end = true;
			}
			else if (response.startsWith("lose")) {
				var moves = response.substring(4).split(",");
				setMove(moves[1], moves[2]);
				document.getElementById("indicator").innerHTML = "You Lose!";
				end = true;
			}
		},
		error: function(response) {
			alert(response);
		}
	});
}

function setMove(r, c) {
	row = parseInt(r) + 1;
	col = parseInt(c) + 1;
	cell = document.getElementById("tbl").rows[row].cells[col];
	if (playerFirst) {
		cell.innerHTML = "<img src='white_piece25x25.png'/>";
	}
	else {
		cell.innerHTML = "<img src='black_piece25x25.png'/>";
	}
}

