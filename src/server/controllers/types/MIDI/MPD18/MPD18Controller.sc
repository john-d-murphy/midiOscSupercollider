MPD18 : MidiController {
    classvar <layout, <layoutSections;
    *initClass {
        // MPD has 3 banks. We are treating this as if it has one bank only (Bank A) for now.
        // If needed, we can add the additional banks, as they are just different midi notes
        // starting at 64, and 80, respectively
        layout =
        Array2D.fromArray(4, 5, [
           ["P", 48], ["P", 49], ["P", 50], ["P", 51], ["S", 01],
           ["P", 44], ["P", 45], ["P", 46], ["P", 47], ["X", 00],
           ["P", 40], ["P", 41], ["P", 42], ["P", 43], ["X", 00],
           ["P", 36], ["P", 37], ["P", 38], ["P", 39], ["X", 00],
        ]);

        layoutSections = [
        ];

    }

    *new { |device_number|
        ^super.new("Akai MPD18", device_number);
    }

    *oscPath{
       ^super.oscPath ++ "/mpd18";
    }

}
