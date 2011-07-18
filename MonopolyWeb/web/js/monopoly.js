/*
    monopoly.js:

*/

if (typeof GAME == "undefined") {
    GAME = {
	    version: "1.0.1",
            build: 1
    };
}

GAME.AJAX_URL = "MonopolyServlet"; // URL of Ajax call
GAME.ACTION = {
    CREATE: "create",
    JOIN: "join",
    GET_BOARD: "board",
    GET_STATE:  "state",
    BUY_ANSWER: "buy",
    CLIENT_DICES: "dices",
    RESIGN: "resign"
};

GAME.UPDATE_INTERVAL = 500; // how often we check for state update (in milisec)
GAME.MAX_PLAYERS = 6; // support 2 to 6 players
GAME.MOVEMENT_DONE = "movementdone"; // event movementdone is fiered after all anomations are done

// create a private scope and map $ to jQuery
(function($){

    // Player object
    GAME.Player = function(id){
        this.id = id;
        this.cell = 1;
        this.alive = false;
        this.element = $("<div id='player" + this.id + "' class='player-elem hidden'></div>");
        this.element.appendTo($("#monopoly-frame"));
        this.element.css({top: 0, left: 0});
    };

    //reset visual presentation
    GAME.Player.prototype.reset = function(){
        var assetClassName = "player" + this.id + "-asset";
        // remove assets
        $("." + assetClassName).removeClass(assetClassName).removeClass("house3").removeClass("house2").removeClass("house1");
    };

    GAME.Player.prototype.isAlive = function(){
        return this.alive;
    };


    // update the player state
    GAME.Player.prototype.update = function(oState){
        var player = this,
            position,
            parkingCount,
            targetCell,
            unregister = function(){
                GAME.monopoly.registerAmimation(-1);
            };

            if(oState.playing == "yes")
            {
                $("#monopoly-frame #player" + this.id).removeClass("hidden");
                this.alive = true;
            }
        /*$.each(oState.assets, function(){
            $("#cell" + this.address).addClass("player" + player.id + "-asset");
            if (this.houses){
                $("#cell" + this.address).addClass("house" + this.houses);
            }
        });*/
        var boardPosition = oState.position + 1;

        // is there someone allready parked on our cell? if so, add a small offset
        occupiedCellsMap = GAME.monopoly.getOccupiedCellsMap ();
        parkingCount = occupiedCellsMap[boardPosition] ? occupiedCellsMap[boardPosition] : 0;
        occupiedCellsMap[boardPosition] = parkingCount + 1;

        targetCell =  $("#cell" + boardPosition);
        position  = targetCell.position();
        this.element.css({top: position.top + (targetCell.height() / 2), left: position.left + (targetCell.width() / 2) + (parkingCount * 10) - 20});
        /*if(this.cell !== boardPosition){
            GAME.monopoly.registerAmimation(1);
            while (this.cell !== boardPosition){
                this.cell = this.cell % 36 + 1;
                targetCell =  $("#cell" + this.cell);
                position  = targetCell.position();
                this.element.animate({top: position.top + (targetCell.height() / 2), left: position.left + (targetCell.width() / 2) + (parkingCount * 10) - 20},
                        200,  this.cell === boardPosition ? unregister : $.noop);
            }
        }*/

        if(oState.name)
        {
            this.element.attr('title', oState.name);
        }
    };

    GAME.Player.prototype.legend = function(oState, legend){
        $("<li class='player" + oState.id + "'><strong>" + oState.name + "</strong>, " + oState.money + " Shekels</li>").appendTo($("ul", legend));
    };

}(jQuery));

// make sure the $ is maped to the jQuery function at least inside our function

GAME.monopoly = function($){
    var players = [], // hold reference to all players
        occupiedCellsMap = [], // which cells are occupied - to calculate position in cell
        legend,
        wait_dialog,
        question_dialog,
        message_dialog,
        error_dialog,
        create_dialog,
        join_dialog,
        dices_dialog,
        pendingAjaxCall,
        pendingAnimations = 0;
    var waitingForPlayers = false;
    var gameOver = false;

        // updatePlayer
        updateCells = function(n, cell)
        {
            //$("#cell" + cell.id + " div.level1").css({background: "'url(" + cell.img + ") no-repeat top left'"});
            if(((cell.id - 1) % 9) == 0)
            {
                $("#cell" + cell.id + " div.level1").css({'background': 'url(' + cell.img+ ') no-repeat top left'}); 
            }
            else
            {
                $("#cell" + cell.id + " div.top").css({'background': 'url(' + cell.img+ ') no-repeat top left'}); 
                $("#cell" + cell.id + " div.title").html(cell.title);
            }
            
        },

        // updatePlayer
        updatePlayer = function(n, player)
        {
            if (player)
            {
                players[player.id - 1].update(player);
            }
        },

        // updateLegend
        updateLegend = function(n, player){
            if (players[player.id - 1].isAlive()){
                //players[player.id - 1].legend(player, legend);
                $("<li class='player" + player.id + "'><span id='playerName'><strong>" + player.name + "</strong></span>, <span id='money'>" + player.money + "</span> Shekels</li>").appendTo($("ul", legend));
            }
        },

        // display the dialog that was passed
        showDialog = function(dialogWrap, isOn){
            var collapsedWidth = 200,
                collapsedHeight = 200,
                targetTop,
                targetLeft,
                targetWidth,
                targetHeight,
                targetOpacity;

            if (isOn){
                dialogWrap.css({
                        opacity: 'hide',
                        top: ($(window).height() - collapsedHeight) / 2,
                        left:($(window).width() - collapsedWidth) / 2,
                        width: collapsedWidth,
                        height: collapsedHeight
                     });
                targetWidth =  $(window).width() / 2;
                targetHeight =  $(window).height() / 2;
                targetTop =  $(window).height() / 4;
                targetLeft =  $(window).width() / 4;
                targetOpacity = 1;
            } else {
                targetWidth =  collapsedWidth;
                targetHeight = collapsedHeight;
                targetTop = ($(window).height() - collapsedHeight) / 2;
                targetLeft = ($(window).width() - collapsedWidth) / 2;
                targetOpacity = 0;
            }

            dialogWrap.removeClass("hidden")
                     .animate(
                         {
                             opacity: targetOpacity,
                             width: targetWidth,
                             height: targetHeight,
                             top: targetTop,
                             left: targetLeft
                         },
                         200,
                         function(){
                            if (!isOn){
                                dialogWrap.addClass("hidden");    
                            }
                         }
                     );

        },

        updateWaitDialog = function(dialog)
        {
            wait_dialog.find(".message").text(dialog.message).end();
        },

        prompt_dialog = function(dialog, promptDialog){
            promptDialog.find(".message").text(dialog.message).end()
                .find(".error").text(dialog.error).end();
            showDialog(promptDialog, true);
        },

        show_error = function(failed)
        {
            error_dialog.find(".error").text(failed.message).end();
            showDialog(error_dialog, true);
        },

        show_message = function(oState)
        {
            message_dialog.find(".message").text(oState.message).end();
            showDialog(message_dialog, true);
        },

        //display a dialog and ask a question
        ask = function(quest){
            question_dialog.find(".question").text(quest.question).end()
                    .find("#btn1").text(quest.option1).end()
                    .find("#btn2").text(quest.option2)
                    .end();
            showDialog(question_dialog, true);
        },

        //display a throw dices dialog
        throwDices = function(oState){
            dices_dialog.find("#cube1").val("").end().find("#cube2").val("").end().find(".error").val(oState.error).end();
            showDialog(dices_dialog, true);
        },

        _registerAmimation = function(i){
            pendingAnimations += i;
            if (i < 0 && pendingAnimations === 0){
                $("#monopoly-frame").trigger(GAME.MOVEMENT_DONE);
            }
        },

        // handle server update
        handleServerUpdate = function(o)
        {
            var setTimer = true;
            var updateInterval = GAME.UPDATE_INTERVAL;

            if(o == null)
            {
                setTimeout(updateState, updateInterval);
                return;
            }
            
            if (waitingForPlayers)
            {
                if (!o.wait)
                {
                    showDialog(wait_dialog, false);
                    waitingForPlayers = false;
                }
            }

            pendingAjaxCall = null;

            if(o.failed)
            {
                show_error(o.failed);
                setTimer = false;
            }
            else if (o.create)
            {
                prompt_dialog(o.create, create_dialog);
                setTimer = false;
            }
            else if(o.join)
            {
                prompt_dialog(o.join, join_dialog);
                setTimer = false;
            }
            else if(o.wait)
            {
                if (!waitingForPlayers)
                {
                    updateWaitDialog(o.wait);
                    showDialog(wait_dialog, true);
                    waitingForPlayers = true;
                }
                else
                {
                    updateWaitDialog(o.wait);
                }
                updateInterval = 1000;
            }
            else if(o.board)
            {
                $.each(o.board, updateCells);
            }
            else if(o.players)
            {
                //pendingAnimations = 1;

                $.each(o.players, updatePlayer);
                $.each(o.players, updateLegend);
                $("#resign").removeClass("hidden");

                // register for a "one time" event
                //$("#monopoly-frame").one(GAME.MOVEMENT_DONE , updateState);
                //setTimer = false;
            }
            else if(o.game_over)
            {
                gameOver = true;
                show_message(o.game_over);
                setTimer = false;
            }
            else if(o.winner)
            {
                setTimer = false;
                show_message(o.winner);
            }
            else if(o.lost)
            {
                setTimer = false;
                show_message(o.lost);
                players[o.lost.player-1].reset(); // remove player from board
            }
            else if(o.turn)
            {
                // TODO: remove previous turn
                // indicate it's o.turn.player's turn!
                $("#playersLegend .player" + o.turn.player + " div.playerName").addClass("playing");
            }
            else if(o.dices)
            {
                setTimer = false;
                throwDices(o.dices);
            }
            else if(o.dices_set)
            {
                $("#dice1").css({'background': 'url(css/images/die' + o.dices_set.dice1+ '.gif)'});
                $("#dice2").css({'background': 'url(css/images/die' + o.dices_set.dice2+ '.gif)'});
            }
            else if(o.move)
            {
                //pendingAnimations = 1;
                updatePlayer(0, o.move);

                // register for a "one time" event
                //$("#monopoly-frame").one(GAME.MOVEMENT_DONE , updateState);
                //setTimer = false;
            }
            else if(o.info)
            {
                setTimer = false;
                show_message(o.info);
            }
            else if(o.balance)
            {
                $("#playersLegend .player" + o.balance.player + " #money").html(o.balance.money);
            }
            else if(o.buy)
            {
                setTimer = false;
                ask(o.buy);
            }
            else if(o.build)
            {
                setTimer = false;
                ask(o.buy);
            }
            else if(o.house)
            {
            }
            else if(o.bought)
            {
            }

            if (setTimer)
            {
                setTimeout(updateState, updateInterval);
            }

            //_registerAmimation(-1);
         },

//         handlePrompt = function(o, status)
//         {
//             alert("boom");
//            if(o.join)
//            {
//                alert("here");
//                prompt_dialog(o.join, join_dialog);
//            }
//         },
         
        // ask server for updates
        updateState = function()
        {
            pendingAjaxCall = $.getJSON(
                GAME.AJAX_URL,
                "action=" + GAME.ACTION.GET_STATE,
                handleServerUpdate
            );
        };

        postBuyReply = function(answerToSend) {
            return function() {
                $.post(
                    GAME.AJAX_URL,
                    $.param({answer: answerToSend, action: GAME.ACTION.BUY_ANSWER})
                );
                setTimeout(updateState, 500);
                showDialog(question_dialog, false);
            }
        };

    return {
        init:function(){
            var i;
            // setup 3 layers inside each cube
            $('#monopoly-frame>div').addClass("level0").html("<div class='level1'><div class='title'/><div class='top'/><div class='center'/><div class='bottom'/></div>");

            var resignButton = $('<div id="resign" class="hidden"><button id="leave">Resign</button></div>');
            resignButton.appendTo($("#monopoly-frame"));
            $("#leave").click(function()
            {
                alert("Goodbye!");
                 $.post(GAME.AJAX_URL, $.param({action: GAME.ACTION.RESIGN}));
            });

            var dicePanel = $('<div id="dice1" class="diceFrame"/><div id="dice2" class="diceFrame"/>');
            dicePanel.appendTo($("#monopoly-frame"));

            // create players legend
            legend = $("<div id='playersLegend'><ul></ul></div>");
            legend.appendTo($("#monopoly-frame"));

            // create a dialog for errors
            error_dialog = $("<div id='errorDialog' class='dialog hidden'><div class='dialogMessage'><div class='error'></div></div></div>");
            error_dialog.appendTo($("#monopoly-frame"));

            message_dialog = $("<div id='messageDialog' class='dialog hidden'><div class='dialogMessage'><div class='message'></div></br></br><button id='ok'>OK</button></div></div>");
            message_dialog.appendTo($("#monopoly-frame"));
            $("#ok").click(function()
            {
                showDialog(message_dialog, false);
                if (!gameOver)
                {
                    setTimeout(updateState, 500);
                }
            });

            // create a dialog for game creation
            create_dialog = $("<div id='createDialog' class='dialog hidden'><div class='dialogMessage'><div class='message'></div><br/><div class='error'></div><div class='options'><table><tr><td>Game Name</td><td><input type='text' id='gameName' /></td></tr><tr><td>Number of players</td><td><input type='text' id='playersNum' /></td></tr><tr><td>Number of human players</td><td><input type='text' id='humansNum' /></td></tr><tr><td>Automate Dice?</td><td><input type='checkbox' id='autoDice' checked='checked'/></td></tr><tr><td><button id='create'>Create Game</button></td></tr></table></div></div></div>");
            create_dialog.appendTo($("#monopoly-frame"));
            $("#create").click(function()
            {
                showDialog(create_dialog, false);
                $.post(GAME.AJAX_URL, $.param({action: GAME.ACTION.CREATE,
                    gameName: $("#gameName").val() ,
                    playersNum: $("#playersNum").val(),
                    humansNum: $("#humansNum").val(),
                    autoDice: $("#createDialog input:checked").length}),
                    handleServerUpdate);
                //setTimeout(updateState, 500);
                
            });

            // create a dialog for joining a game
            join_dialog = $("<div id='joinDialog' class='dialog hidden'><div class='dialogMessage'><div class='message'></div><br/><div class='error'></div><div class='options'><table><tr><td>Player Name</td><td><input type='text' id='playerName' /></td></tr><tr><td><button id='join'>Join</button></td></tr></table></div></div></div>");
            join_dialog.appendTo($("#monopoly-frame"));
            $("#join").click(function()
            {
                showDialog(join_dialog, false);
                $.post(GAME.AJAX_URL, $.param({action: GAME.ACTION.JOIN, playerName: $("#playerName").val()}), handleServerUpdate);
                //setTimeout(updateState, 500);
            });

            // create a dialog for waiting on players to join
            wait_dialog = $("<div id='waitDialog' class='dialog hidden'><div class='dialogMessage'><div class='message'></div></div></div></div>");
            wait_dialog.appendTo($("#monopoly-frame"));

            // create a dialog for later server questions
            question_dialog = $("<div id='questDialog' class='dialog hidden'><div class='question'></div><div class='ft'><button id='btn1'>option1</button><button id='btn2'>option2</button></div></div>");
            question_dialog.appendTo($("#monopoly-frame"));
            $("#btn1").click(postBuyReply("yes"));
            $("#btn2").click(postBuyReply("no"));

            // create a dialog for manual dices input
            dices_dialog = $("<div id='dicesDialog' class='dialog hidden'><div class='question'>Please throw the dices" +
                      "<br><div class='error'></div><br>" +
                      "<div><input type='text' id='cube1' /><input type='text' id='cube2' /></div></div><div class='ft'><button id='btn3'>OK</button></div></div>");
            dices_dialog.appendTo($("#monopoly-frame"));
            $("#btn3").click(function(){
                $.post(GAME.AJAX_URL, $.param({action: GAME.ACTION.CLIENT_DICES, cube1: $("#cube1").val() ,cube2: $("#cube2").val()}));
                setTimeout(updateState, 500);
                showDialog(dices_dialog, false);
            });

            // build players array
            for (i = 0; i < GAME.MAX_PLAYERS; i++){
                players[i] = new GAME.Player(i + 1);
            }

            $.ajaxSetup ({
                // Disable caching of AJAX responses
                cache: false
            });
            
            // start checking for state update
            updateState();
        },

        registerAmimation: function(i){
            _registerAmimation(i);
        },

        getOccupiedCellsMap : function() {
            return occupiedCellsMap;
        }

    };

}(jQuery);

// on document ready - call init
jQuery(GAME.monopoly.init);


