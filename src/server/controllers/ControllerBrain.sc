// Needs to be a singleton
ControllerBrain : Singleton {

  var <controllers, osc_connection;
  var masterKey, masterTempo;

  // *new {
  //   ^super.new.init();
  // }

  init {
    "CALLING SINGLETON CONSTRUCTOR".postln;
    controllers = Dictionary();
    osc_connection = NetAddr("127.0.0.1", NetAddr.langPort);
  }

  // Private Methods
  prAddController { |controller|
  //   controllers.put(controller.name, controller);
  }

  prRemoveControllerByName { |name|
  //   controllers.removeAt(uid);
  }

  prGetControllerByName { |name|
  //   ^controllers.at(uid);
  }

  addAlgorithmToController{ |algorithmControls|
    // Find Available Space On Controller
  }

  removeAlgorithmFromController{ |algorithmControls|
    // Remove And Disable Lights/etc.
  }
}
