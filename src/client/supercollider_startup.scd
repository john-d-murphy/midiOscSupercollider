(
var systemPluginsDirectory,localPluginsDirectory, quarksDirectory;
Server.killAll;

// Set the environment for the plugins
localPluginsDirectory="env | grep LOCAL_PLUGINS_DIRECTORY | cut -f2 -d=".unixCmdGetStdOut.replace("\n","");
systemPluginsDirectory="env | grep SYSTEM_PLUGINS_DIRECTORY | cut -f2 -d=".unixCmdGetStdOut.replace("\n","");
s.options.ugenPluginsPath = [ systemPluginsDirectory, localPluginsDirectory];

// Set the environment for the quarks
quarksDirectory="env | grep QUARKS_DIRECTORY | grep -v QUARKS_DIRECTORY_SED | cut -f2 -d=".unixCmdGetStdOut.replace("\n","");
Quarks.updateFolderPath(quarksDirectory);

s.reboot {

        // server options are only updated on reboot
	// configure the sound server: here you could add hardware specific options
	// see http://doc.sccode.org/Classes/ServerOptions.html
        s.options.numBuffers = 1024 * 256; // increase this if you need to load more samples
	s.options.memSize = 8192 * 32; // increase this if you get "alloc failed" messages
	s.options.numWireBufs = 64; // increase this if you get "exceeded number of interconnect buffers" messages
	s.options.maxNodes = 1024 * 32; // increase this if you are getting drop outs and the message "too many nodes"
	s.options.numOutputBusChannels = 12; // set this to your hardware output channel size, if necessary
	s.options.numInputBusChannels = 14; // set this to your hardware output channel size, if necessary

        // boot the server and start the limiter
	s.waitForBoot {
		s.sync; // optionally: wait for samples to be read

                // Analysis Tools
                s.plotTree;
                s.meter;
                FreqScope.new;
                Server.local.makeWindow;

                StageLimiter.activate(numChannels: 2);
                s.volume.volume = -45;
                //~localLaunchpad = Launchpad.new(1);
                ~localMk2 = MiniLabMKII.new(1);
	};


        // Initialize Midi
        MIDIClient.init;
        MIDIIn.connectAll;
        MIDIOut.connectAll;

	s.latency = 1; // increase this if you get "late" messages
}
);
