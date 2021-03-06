/*
An attempt at giving Tidalcycles a direct interface to SuperDirt,
so you could load samples, load synths, kill the server, etc. all from a Tidalcycles Ide

Currently, the SuperDirt class intercepts osc messages containing parameters
sent from Tidal, and sends them t
*/

DirtInterfaceEvent {

	var <event;
	var soundLibrary;
	var superdirtInstance;

    *new { |event,soundLibrary,superdirtInstance|
				// "in Interface".postln;
        ^super.newCopyArgs(event,soundLibrary,superdirtInstance)
    }

	test {
		// server = ~server.value;

		if(soundLibrary.notNil) 
			{"soundLibrary exists".postln}
			{"no soundLibrary exists".postln};

	}

	parse {
		// if(event[\scMessage] == \loadSoundFiles){this.loadSoundFiles}
		switch(event[\scMessage])
			{\loadSynthDefs} {this.loadSynthDefs}
			{\loadOnly} {this.loadOnly}
			{\loadSoundFileFolder} {this.loadSoundFileFolder}
			{\loadSoundFiles} {this.loadSoundFiles}
			{\loadSoundFile} {this.loadSoundFile}
			{\freeAllSoundFiles} {this.freeAllSoundFiles}
			{\freeSoundFiles} {this.freeSoundFiles}
			{\postSampleInfo} {this.postSampleInfo}
			{\initFreqSynthWindow} {this.initFreqSynthWindow}
			{\sendToTidal} {this.sendToTidal}
	}

	sendToTidal { |args|
		"testing sendToTidal".postln;
		~dirt.sendToTidal(event[\path])
		}

	loadSynthDefs{
		// "in loadSynthDefs".postln;
		// this is stupid but
		if(event[\filePath].notNil)
			{
            var filepath; // basically copied the loadSynthDefs command from SuperDirt.sc because the filepath wasn't parsed correctly by pathMatch(standardizePath())
    		filepath = event[\filePath].asString;
    		if(filepath.splitext.last == "scd") {
    				(dirt:superdirtInstance).use { filepath.load }; "loading synthdefs in %\n".postf(filepath)
    		 }
		}
			{"error: no path passed to loadSynthDefs"};
	}

	loadOnly {
		// "in loadonly".postln;
		if(event[\filePath].notNil)
			{soundLibrary.loadOnly(event[\filePath].asString)}
			{"error: no path passed to loadOnly"};
	}

	loadSoundFileFolder {
	// "in loadSoundFileFolder".postln;
		if(event[\filePath].notNil)
			{soundLibrary.loadSoundFileFolder(event[\filePath].asString)}
			{"error: no path passed to loadSoundFileFolder"};
	}

	loadSoundFiles {
		"in loadSoundFiles".postln;
		event[\filePath].asString.postln;
		if(event[\filePath].notNil)
			{soundLibrary.loadSoundFiles(event[\filePath].asString)}
			{"error: no path passed to loadSoundFiles"};
	}

	loadSoundFile { 
	// "in loadSoundFile".postln;
		if(event[\filePath].notNil)
			{soundLibrary.loadSoundFile(event[\filePath].asString)}
			{"error: no path passed to loadSoundFile"};
	}

	freeAllSoundFiles {
	// "in freeAllSoundFiles".postln;
	soundLibrary.freeAllSoundFiles;
	}

	freeSoundFiles {
	// "in freeSoundFiles".postln;
		if(event[\names].notNil)
			{soundLibrary.freeSoundFiles(event[\names])}
			{"error: no names passed to freeSoundFiles"};
	}

	postSampleInfo {
	// "in postSampleInfo".postln;
	soundLibrary.postSampleInfo;
	}

	initFreqSynthWindow{
		"initalizing frequency scope window".postln;
		FreqScopeWindow().new;
	}

	debug { 
		// event.postln;
		this.parse;
	}


	*predefinedServerParameters{
		// not complete, but avoids obvious collisions
		^#[\load, \free];
	}



}

