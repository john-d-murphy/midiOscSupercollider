LaunchpadLightButton : MidiLightButton {

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



Launchpad : MidiController {
    classvar <layout, <layoutSections;

    *initClass {
        layout =
        Array2D.fromArray(9, 9, [
           ["B", 104], ["B", 105], ["B", 106], ["B", 107], ["B", 108], ["B", 109], ["B", 110], ["B", 111],  ["X", 000],
           ["L", 000], ["L", 001], ["L", 002], ["L", 003], ["L", 004], ["L", 005], ["L", 006], ["L", 007],  ["L", 008],
           ["L", 016], ["L", 017], ["L", 018], ["L", 019], ["L", 020], ["L", 021], ["L", 022], ["L", 023],  ["L", 024],
           ["L", 032], ["L", 033], ["L", 034], ["L", 035], ["L", 036], ["L", 037], ["L", 038], ["L", 039],  ["L", 040],
           ["L", 048], ["L", 049], ["L", 050], ["L", 051], ["L", 052], ["L", 053], ["L", 054], ["L", 055],  ["L", 056],
           ["L", 064], ["L", 065], ["L", 066], ["L", 067], ["L", 068], ["L", 069], ["L", 070], ["L", 071],  ["L", 072],
           ["L", 080], ["L", 081], ["L", 082], ["L", 083], ["L", 084], ["L", 085], ["L", 086], ["L", 087],  ["L", 088],
           ["L", 096], ["L", 097], ["L", 098], ["L", 099], ["L", 100], ["L", 101], ["L", 102], ["L", 103],  ["L", 104],
           ["L", 112], ["L", 113], ["L", 114], ["L", 115], ["L", 116], ["L", 117], ["L", 118], ["L", 119],  ["L", 120],

        ]);

        layoutSections = [];
    }

    *new { |device_number|
        ^super.new("Launchpad", device_number);
    }

    *oscPath{
       ^super.oscPath ++ "/launchpad";
    }

    //////// LAUNCHPAD ELEMENT MODIFIERS ////////
    getButton { | id, channel, type |
        var buttonOscPath = this.oscPath ++ "/" ++ type.asString ++ "/" ++ id;
        ^MidiButton.new(
            midiConnectionDetails.uid,
            type,
            id,
            channel,
            (\in: [\control]),
            buttonOscPath,
            midiControlListener)
    }
}
