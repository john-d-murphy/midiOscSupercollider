OSCElement : Element { }

OSCKnob : OSCElement {
   classvar <controlType = \knob;
}

OSCSlider : OSCElement {
   classvar <controlType = \slider;
}

OSCButton : OSCElement {
   classvar <controlType = \button;
}

OSCLightButton : OSCElement {
   classvar <controlType = \light;
}

OSCMapping : Mapping { }
///
/// OSCController : Controller {
///     // Information about Current Page
///     // Array of Buttons/Knobs/Sliders
///     // OSC Listener / OSC Path
///     var <name, <layout, oscPath;
///
///     *new { |name_|
///         ^super.new.init(name_);
///     }
///
///     init { |name_|
///         name = name_;
///     }
///
///
///   // *new { | name, oscPath, layout | }
///
/// }
///
