/*
    monopoly_no_animation.js:

*/

if (typeof GAME == "undefined") {
    GAME = {
	    version: "1.0.1",
            build: 1
    };
}

GAME.AJAX_URL = "MonopolyAjaxServlet"; // URL of Ajax call
GAME.ACTION = {
    GET_STATE:  "state",
    BUY_ANSWER: "buy",
    CLIENT_DICES: "dices"
};

GAME.UPDATE_INTERVAL = 10000; // how often we check for state update (in milisec)
GAME.MAX_PLAYERS = 6; // support 2 to 6 players
GAME.MOVEMENT_DONE = "movementdone"; // event movementdone is fiered after all anomations are done

// create a private scope and map $ to jQuery
(function($){

    // Player object
    GAME.Player = function(id){
        this.id = id;
        this.cell = 1;
        this.element = $("<div id='player" + this.id + "' class='player-elem'></div>");
        this.element.appendTo($("#cell" + this.cell));
        this.element.css({top: 0, left: 0});
    };

    //reset visual presentation
    GAME.Player.prototype.reset = function(){
        var     player = "player" + this.id,
                    assetClassName = player + "-asset";
         // remove player from old position
        $("." + player).removeClass(player);
        // remove assets
        $("." + assetClassName).removeClass(assetClassName).removeClass("house3").removeClass("house2").removeClass("house1");
    };


    // update the player state
    GAME.Player.prototype.update = function(oState){
        var player = this,
            position,
            parkingCount,
            targetCell;

        $.each(oState.assets, function(){
            $("#cell" + this.address).addClass("player" + player.id + "-asset");
            if (this.houses){
                $("#cell" + this.address).addClass("house" + this.houses);
            }
        });

        // is there someone allready parked on our cell? if so, add a small offset
        occupiedCellsMap = GAME.monopoly.getOccupiedCellsMap ();
        parkingCount = occupiedCellsMap[oState.location] ? occupiedCellsMap[oState.location] : 0;
        occupiedCellsMap[oState.location] = parkingCount + 1;

        // set player position
        this.cell = oState.location;
        targetCell = $("#cell" + this.cell);
        targetCell.addClass("player" + player.id);

//        this.element.css({top: (targetCell.height() / 2 - 16), left: (targetCell.width() / 2) + (parkingCount * 10) - 20 });
//        this.element.appendTo(targetCell);
//        // set tooltip
//        this.element.attr('title', oState.name);
    };

    GAME.Player.prototype.legend = function(oState, legend){
        $("<li class='player" + oState.id + "'><strong>" + oState.name + "</strong>, " + oState.fonds + " Shekels</li>").appendTo($("ul", legend));
    };

}(jQuery));

// make sure the $ is maped to the jQuery function at least inside our function

GAME.monopoly = function($){
    var players = [], // hold reference to all players
        occupiedCellsMap = [], // which cells are occupied - to calculate position in cell
        legend,
        dialog,
        dices,
        pendingAjaxCall;

        // updatePlayer
        updatePlayer = function(n, player){
            players[player.id].update(player);
        },

        // updateLegend
        updateLegend = function(n, player){
            players[player.id].legend(player, legend);
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

        //display a dialog and ask a question
        ask = function(quest){
            dialog.find(".question").text(quest.question).end()
                    .find("#btn1").text(quest.option1).end()
                    .find("#btn2").text(quest.option2)
                    .end();
            showDialog(dialog, true);
        },

        //display a throw dices dialog
        throwDices = function(){
            dices.find("#cube1").val("").end().find("#cube2").val("");
            showDialog(dices, true);
        },

        // handle server update
        handleServerUpdate = function(o){
            var i;

            //console.log("ver " + o.ver);
            pendingAjaxCall = null;

            // reset previous state
            occupiedCellsMap = [];
            for (i = 0; i < GAME.MAX_PLAYERS; i++){
                players[i].reset();
            }
            $("ul", legend).empty();

            // render the new state
            $.each(o.players, updatePlayer);
            $.each(o.players, updateLegend);

            // handle buy dialog / dices / wait for next state update
            if (o.dialog){
                 ask(o.dialog);
            } else if (o.dices){
                 throwDices();
            } else {
                 setTimeout(updateState, GAME.UPDATE_INTERVAL);
            }

         },
         
        // ask server for updates
        updateState = function(){
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
                setTimeout(updateState, GAME.UPDATE_INTERVAL);
                showDialog(dialog, false);
            }
        };

    return {
        init:function(){
            var i;
            
            // setup 3 layers inside each cube
            $('#monopoly-frame>div').addClass("level0").html("<div class='level1'><div class='level2'><div class='level3'></div></div></div>");

            // create players legend
            legend = $("<div id='playersLegend'><ul></ul></div>");
            legend.appendTo($("#monopoly-frame"));

            // create a dialog for later server questions
            dialog = $("<div id='questDialog' class='dialog hidden'><div class='question'></div><div class='ft'><button id='btn1'>option1</button><button id='btn2'>option2</button></div></div>");
            dialog.appendTo($("#monopoly-frame"));
            $("#btn1").click(postBuyReply("yes"));
            $("#btn2").click(postBuyReply("no"));

            // create a dialog for manual dices input
            dices = $("<div id='dicesDialog' class='dialog hidden'><div class='question'>Please throw the dices" +
                      "<div><input type='text' id='cube1' /><input type='text' id='cube2' /></div></div><div class='ft'><button id='btn3'>OK</button></div></div>");
            dices.appendTo($("#monopoly-frame"));
            $("#btn3").click(function(){
                $.post(GAME.AJAX_URL, $.param({action: GAME.ACTION.CLIENT_DICES, cube1: $("#cube1").val() ,cube2: $("#cube2").val()}));
                setTimeout(updateState, GAME.UPDATE_INTERVAL);
                showDialog(dices, false);
            });

            // build players array
            for (i = 0; i < GAME.MAX_PLAYERS; i++){
                players[i] = new GAME.Player(i);
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



















