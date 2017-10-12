
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
var player_first = true;
var end = false;
var game_id = -1;
function startGame() {
	if (started) {
		alert("Game already started!");
		return;
	}
	started = true;

	var url = "start";
	var params = {};
	if (document.getElementById("player").checked) {
		params["first"] = "player";
		document.getElementById("indicator").innerHTML = "Your turn";
		player_first = true;
	}
	else {
		params["first"] = "ai";
		document.getElementById("indicator").innerHTML = "AI's turn";
		player_first = false;
	}

	console.log(params);

	$.post(
	    "start",
	    params,
	    function(data, status) {
	        // console.log(status);
	        // console.log(data);
	        game_id = data["game_id"];
	        console.log("game_id: " + game_id);
	        if (!player_first) {
                setMove(data["row"], data["col"], true);
	        }
	    }
    );
}

function move() {
	if (!started) {
		alert("Game has not started!");
		return;
	}
	if (end) {
		alert("Refresh to start a new game");
		return;
	}
	var row = parseInt(document.getElementById("row").value) - 1;
	var col = parseInt(document.getElementById("col").value) - 1;
	console.log("row: " + row + " col: " + col);
	if (row < 0 || row > 14 || col < 0 || col > 14) {
		alert("Position out of bound");
		return;
	}
	if (!setMove(row, col, player_first)) {
	    alert("Invalid position");
        return;
	}
	document.getElementById("indicator").innerHTML = "AI's turn";

    var params = {
        "game_id": game_id,
        "row": row,
        "col": col
    };
	$.post(
	    "move",
        params,
        function(data, status) {
            // console.log(status);
            if (data["status"] != "win")
                console.log("row: " + data["row"] + " col: " + data["col"]);
            switch (data["status"]) {
                case "win" :
                    document.getElementById("indicator").innerHTML = "You Win!";
                    end = true;
                    break;
                case "lose" :
                    setMove(data["row"], data["col"], !player_first);
                    document.getElementById("indicator").innerHTML = "You Lose!";
                    end = true;
                    break;
                case "move" :
                    setMove(data["row"], data["col"], !player_first);
                    document.getElementById("indicator").innerHTML = "Your turn";
                    break;
                default :
                    // Unexpected status
            }
        }
	);
}

function setMove(r, c, is_black) {
	row = parseInt(r) + 1;
	col = parseInt(c) + 1;
	cell = document.getElementById("tbl").rows[row].cells[col];
	if (cell.innerHTML.localeCompare("") !== 0) return false;
	if (is_black) {
		cell.innerHTML = "<img src='img/black_piece25x25.png'/>";
	}
	else {
		cell.innerHTML = "<img src='img/white_piece25x25.png'/>";
	}
	return true;
}

