Controller {

    classvar <availableFunctions;
    classvar <layout, <layoutSections; // must be overridden by child classes
    var <name, <elements, <oscPath, connection, in, out;

    // Semantics around the layout of the controller.
    // Some controllers, like the APC mini, have light
    // buttons that behave in the same way as other light
    // buttons but look different and have different default
    // semantics.

    *oscPath{
       ^"/controller";
    }

    // Layout Sections
    *getLayoutSection { |layoutSection|
        ^layout.getSub(layoutSection.rowStart, layoutSection.colStart, layoutSection.shape).asArray;
    }

    // Factories for Element Types
    getKnob { | id, channel, type |
        Error("getKnob Not Implemented").throw;
    }

    getEndlessKnob { | id, channel, type |
        Error("getEndlessKnob Not Implemented").throw;
    }

    getSlider { | id, channel, type |
        Error("getSlider Not Implemented").throw;
    }

    getButton { | id, channel, type |
        Error("getButton Not Implemented").throw;
    }

    getLightButton { | id, channel, type |
        Error("getLightButton Not Implemented").throw;
    }

    getNote { | id, channel, type |
        Error("getNote Not Implemented").throw;
    }

    getKeyboard { | id, channel, type |
        Error("getPianoKey Not Implemented").throw;
    }

    getPitchBend{ | id, channel, type |
        Error("getPitchBend Not Implemented").throw;
    }
}
