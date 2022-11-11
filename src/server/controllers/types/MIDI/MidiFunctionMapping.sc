MidiFunctionNoteOn : FunctionMapping {
    *new { |name|
      ^super.newCopyArgs("noteOn");
    }
}

MidiFunctionNoteOff : FunctionMapping {
    *new { |name|
      ^super.newCopyArgs("noteOff");
    }
}

MidiFunctionPolytouch : FunctionMapping {
    *new { |name|
      ^super.newCopyArgs("polytouch");
    }
}

MidiFunctionControl : FunctionMapping {
    *new { |name|
      ^super.newCopyArgs("control");
    }
}

MidiFunctionProgram : FunctionMapping {
    *new { |name|
      ^super.newCopyArgs("program");
    }
}

MidiFunctionTouch : FunctionMapping {
    *new { |name|
      ^super.newCopyArgs("touch");
    }
}

MidiFunctionBend : FunctionMapping {
    *new { |name|
      ^super.newCopyArgs("bend");
    }
}

MidiFunctionSysex : FunctionMapping {
    *new { |name|
      ^super.newCopyArgs("sysex");
    }
}

MidiFunctionSysrt : FunctionMapping {
    *new { |name|
      ^super.newCopyArgs("sysrt");
    }
}

MidiFunctionSMPTE : FunctionMapping {
    *new { |name|
      ^super.newCopyArgs("smpte");
    }
}
