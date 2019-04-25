package org.austin.mergeInstruments.rule;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.austin.mergeInstruments.instrument.InstrumentContent;

public interface Rule {
	public static final Map<String, String> mapIns = new HashMap<>();
	String parse(InstrumentContent trade) throws IOException;
}
