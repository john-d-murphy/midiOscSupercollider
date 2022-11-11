APCMiniLightButton : MidiLightButton {

    // For Binary Buttons
    on {
      Error("ON Not Implemented").throw;
    }

    off {
      Error("OFF Not Implemented").throw;
    }

    // For RGB Buttons
    green {
      Error("GREEN Not Implemented").throw;
    }

    greenBlink {
      Error("GREEN BLINK Not Implemented").throw;
    }

    red {
      Error("RED Not Implemented").throw;

    }

    redBlink {
      Error("RED BLINK Not Implemented").throw;
    }

    orange {
      Error("ORANGE Not Implemented").throw;
    }

    orangeBlink {
      Error("ORANGE BLINK Not Implemented").throw;
    }
}

APCMini : MidiController {

    classvar <layout, <layoutSections;

    *initClass {
        layout =
        Array2D.fromArray(10, 9, [
           ["L", 56], ["L", 57], ["L", 58],["L", 59], ["L", 60],["L", 61],["L", 62],["L", 63],  ["L", 82],
           ["L", 48], ["L", 49], ["L", 50],["L", 51], ["L", 52],["L", 53],["L", 54],["L", 55],  ["L", 83],
           ["L", 40], ["L", 41], ["L", 42],["L", 43], ["L", 44],["L", 45],["L", 46],["L", 47],  ["L", 84],
           ["L", 32], ["L", 33], ["L", 34],["L", 35], ["L", 36],["L", 37],["L", 38],["L", 39],  ["L", 85],
           ["L", 24], ["L", 25], ["L", 26],["L", 27], ["L", 28],["L", 29],["L", 29],["L", 31],  ["L", 86],
           ["L", 16], ["L", 17], ["L", 18],["L", 19], ["L", 20],["L", 21],["L", 21],["L", 23],  ["L", 87],
           ["L", 08], ["L", 09], ["L", 10],["L", 11], ["L", 12],["L", 13],["L", 14],["L", 15],  ["L", 88],
           ["L", 00], ["L", 01], ["L", 02],["L", 03], ["L", 04],["L", 05],["L", 06],["L", 07],  ["L", 89],

           ["L", 64], ["L", 65], ["L", 66],["L", 67], ["L", 68],["L", 69],["L", 70],["L", 71],  ["L", 90],

           ["S", 48], ["S", 49], ["S", 50],["S", 51], ["S", 52],["S", 53],["S", 54],["S", 55],  ["S", 91]
        ]);

        layoutSections = [];
    }

    *new { |device_number|
        ^super.new("APC MINI", device_number);
    }

    *oscPath{
       ^super.oscPath ++ "/apcmini";
    }

    // Factories for APC Mini Specific Button Types
    getLightButton { | id, type |
        var lightOscPath = this.oscPath ++ "/" ++ type.asString ++ "/" ++ id;
        ^APCMiniLightButton.new(
            type,
            id,
            midiConnectionDetails.uid,
            (\in: [\noteOn, \noteOff]),
            lightOscPath)
    }

}


